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
import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ModuleTransformerCache;
import se.unlogic.standardutils.threads.MutexKeyProvider;
import se.unlogic.standardutils.xml.ClassPathURIResolver;
import se.unlogic.standardutils.xsl.FileXSLTransformer;
import se.unlogic.standardutils.xsl.URIXSLTransformer;
import se.unlogic.standardutils.xsl.XSLTransformer;

public class ForegroundModuleXSLTCache implements ForegroundModuleCacheListener, ModuleTransformerCache<ForegroundModuleDescriptor> {

	private final ConcurrentHashMap<ForegroundModuleDescriptor, XSLTransformer> xslMap = new ConcurrentHashMap<ForegroundModuleDescriptor, XSLTransformer>();
	private final MutexKeyProvider<ForegroundModuleDescriptor> mutexKeyProvider = new MutexKeyProvider<ForegroundModuleDescriptor>();
	private final String applicationFileSystemPath;
	private final Logger log = Logger.getLogger(this.getClass());

	public ForegroundModuleXSLTCache(String applicationFileSystemPath) {

		this.applicationFileSystemPath = applicationFileSystemPath;
	}

	public void clearModules() {

		log.debug("Clearing module XSL cache");
		this.xslMap.clear();
	}

	@Override
	public void moduleCached(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

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
	public void moduleUnloaded(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

		synchronized (mutexKeyProvider.getKey(moduleDescriptor)) {

			if (this.xslMap.containsKey(moduleDescriptor)) {
				log.debug("Unloading XSL stylesheet for " + moduleDescriptor);
				this.xslMap.remove(moduleDescriptor);
			}
		}
	}

	@Override
	public void moduleUpdated(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

		synchronized (mutexKeyProvider.getKey(moduleDescriptor)) {
			
			if (moduleDescriptor.hasStyleSheet()) {
				
				if (this.xslMap.containsKey(moduleDescriptor)) {
					
					ForegroundModuleDescriptor oldmb = null;

					for (ForegroundModuleDescriptor mbx : this.xslMap.keySet()) {
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

	private XSLTransformer getCachedXslStyleSheet(ForegroundModuleDescriptor moduleDescriptor) throws TransformerConfigurationException, ClassNotFoundException, URISyntaxException, ResourceNotFoundException {

		if (moduleDescriptor.getXslPath() != null && moduleDescriptor.getXslPathType() != null) {

			if (moduleDescriptor.getXslPathType() == PathType.Filesystem) {

				return new FileXSLTransformer(new File(moduleDescriptor.getXslPath()), ClassPathURIResolver.getInstance(), false);

			} else if (moduleDescriptor.getXslPathType() == PathType.RealtiveFilesystem) {

				return new FileXSLTransformer(new File(this.applicationFileSystemPath + moduleDescriptor.getXslPath()), ClassPathURIResolver.getInstance(), false);

			} else if (moduleDescriptor.getXslPathType() == PathType.Classpath) {

				URL styleSheetURL = Class.forName(moduleDescriptor.getClassname()).getResource(moduleDescriptor.getXslPath());

				if (styleSheetURL == null) {

					throw new ResourceNotFoundException(moduleDescriptor.getClassname(), moduleDescriptor.getXslPath());

				} else {

					return new URIXSLTransformer(styleSheetURL.toURI(), ClassPathURIResolver.getInstance(), true);
				}
			}
		}
		return null;
	}

	public Transformer getModuleTranformer(ForegroundModuleDescriptor moduleDescriptor) throws TransformerConfigurationException {

		XSLTransformer xsltCacher = this.xslMap.get(moduleDescriptor);

		if (xsltCacher != null) {
			
			return xsltCacher.getTransformer();
			
		} else {
			
			return null;
		}
	}
}
