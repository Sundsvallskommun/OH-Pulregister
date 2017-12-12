/*******************************************************************************
 * Copyright (c) 2010 Robert "Unlogic" Olofsson (unlogic@unlogic.se).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 ******************************************************************************/
package se.unlogic.hierarchy.core.cache;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.log4j.Logger;

import se.unlogic.hierarchy.core.enums.PathType;
import se.unlogic.hierarchy.core.exceptions.ResourceNotFoundException;
import se.unlogic.hierarchy.core.interfaces.BackgroundModule;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ModuleTransformerCache;
import se.unlogic.standardutils.threads.MutexKeyProvider;
import se.unlogic.standardutils.xml.ClassPathURIResolver;
import se.unlogic.standardutils.xsl.FileXSLTransformer;
import se.unlogic.standardutils.xsl.URIXSLTransformer;
import se.unlogic.standardutils.xsl.XSLTransformer;

public class BackgroundModuleXSLTCache implements BackgroundModuleCacheListener, ModuleTransformerCache<BackgroundModuleDescriptor> {

	private final ConcurrentHashMap<BackgroundModuleDescriptor, XSLTransformer> xslMap = new ConcurrentHashMap<BackgroundModuleDescriptor, XSLTransformer>();
	private final MutexKeyProvider<BackgroundModuleDescriptor> mutexKeyProvider = new MutexKeyProvider<BackgroundModuleDescriptor>();
	private final String applicationFileSystemPath;
	private final Logger log = Logger.getLogger(this.getClass());

	public BackgroundModuleXSLTCache(String applicationFileSystemPath) {

		this.applicationFileSystemPath = applicationFileSystemPath;
	}

	public void clearModules() {

		log.debug("Clearing module XSL cache");
		this.xslMap.clear();
	}

	@Override
	public void moduleCached(BackgroundModuleDescriptor moduleDescriptor, BackgroundModule moduleInstance) {

		synchronized (mutexKeyProvider.getKey(moduleDescriptor)) {

			try {
				XSLTransformer xsltCacher = this.getCachedXslStyleSheet(moduleDescriptor);

				if (xsltCacher != null) {
					log.debug("Caching XSL stylesheet for " + moduleDescriptor);
					this.xslMap.put(moduleDescriptor, xsltCacher);
				}
			} catch (TransformerConfigurationException e) {
				log.error("Error caching XSL stylesheet for " + moduleDescriptor + ", " + e);
			} catch (ClassNotFoundException e) {
				log.error("Error caching XSL stylesheet for " + moduleDescriptor + ", " + e);
			} catch (URISyntaxException e) {
				log.error("Error caching XSL stylesheet for " + moduleDescriptor + ", " + e);
			} catch (ResourceNotFoundException e) {
				log.error("Error caching XSL stylesheet for " + moduleDescriptor + ", " + e);
			}
		}
	}

	@Override
	public void moduleUnloaded(BackgroundModuleDescriptor moduleDescriptor, BackgroundModule moduleInstance) {

		synchronized (mutexKeyProvider.getKey(moduleDescriptor)) {
			
			if (this.xslMap.containsKey(moduleDescriptor)) {
				log.debug("Unloading XSL stylesheet for " + moduleDescriptor);
				this.xslMap.remove(moduleDescriptor);
			}
		}
	}

	@Override
	public void moduleUpdated(BackgroundModuleDescriptor moduleDescriptor, BackgroundModule moduleInstance) {

		synchronized (mutexKeyProvider.getKey(moduleDescriptor)) {
			
			if (moduleDescriptor.hasStyleSheet()) {
				if (this.xslMap.containsKey(moduleDescriptor)) {
					BackgroundModuleDescriptor oldmb = null;

					for (BackgroundModuleDescriptor mbx : this.xslMap.keySet()) {
						if (mbx.equals(moduleDescriptor)) {
							oldmb = mbx;
							break;
						}
					}

					if (oldmb.getXslPathType().equals(moduleDescriptor.getXslPathType()) && oldmb.getXslPath().equals(moduleDescriptor.getXslPath())) {
						try {
							log.debug("Reloading XSL stylesheet for " + moduleDescriptor);
							this.xslMap.get(moduleDescriptor).reloadStyleSheet();
						} catch (TransformerConfigurationException e) {
							log.error("Error reloading XSL stylesheet for " + moduleDescriptor + ", " + e);
						}
					} else {
						this.moduleCached(moduleDescriptor, moduleInstance);
					}
				} else {
					this.moduleCached(moduleDescriptor, moduleInstance);
				}
			} else {
				this.moduleUnloaded(moduleDescriptor, moduleInstance);
			}
		}

	}

	private XSLTransformer getCachedXslStyleSheet(BackgroundModuleDescriptor mb) throws TransformerConfigurationException, ClassNotFoundException, URISyntaxException, ResourceNotFoundException {

		if (mb.getXslPath() != null && mb.getXslPathType() != null) {

			if (mb.getXslPathType() == PathType.Filesystem) {

				return new FileXSLTransformer(new File(mb.getXslPath()), ClassPathURIResolver.getInstance(), false);

			} else if (mb.getXslPathType() == PathType.RealtiveFilesystem) {

				return new FileXSLTransformer(new File(this.applicationFileSystemPath + mb.getXslPath()), ClassPathURIResolver.getInstance(), false);

			} else if (mb.getXslPathType() == PathType.Classpath) {

				URL styleSheetURL = Class.forName(mb.getClassname()).getResource(mb.getXslPath());

				if (styleSheetURL == null) {

					throw new ResourceNotFoundException(mb.getClassname(), mb.getXslPath());

				} else {

					return new URIXSLTransformer(Class.forName(mb.getClassname()).getResource(mb.getXslPath()).toURI(), ClassPathURIResolver.getInstance(), true);
				}
			}
		}
		return null;
	}

	public Transformer getModuleTranformer(BackgroundModuleDescriptor mb) throws TransformerConfigurationException {

		XSLTransformer xsltCacher = this.xslMap.get(mb);

		if (xsltCacher != null) {
			return xsltCacher.getTransformer();
		} else {
			return null;
		}
	}
}
