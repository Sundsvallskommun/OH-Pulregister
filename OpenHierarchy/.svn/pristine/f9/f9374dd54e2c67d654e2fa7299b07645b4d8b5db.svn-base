package se.unlogic.hierarchy.backgroundmodules.clonemodule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.CachePreconditionException;
import se.unlogic.hierarchy.core.exceptions.ModuleAlreadyBeingCachedException;
import se.unlogic.hierarchy.core.exceptions.ModuleAlreadyCachedException;
import se.unlogic.hierarchy.core.exceptions.ModuleInitializationException;
import se.unlogic.hierarchy.core.exceptions.ModuleInstasiationException;
import se.unlogic.hierarchy.core.handlers.SimpleSettingHandler;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.validationerrors.UnableToParseFileValidationError;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class CloneBackgroundModule extends AnnotatedBackgroundModule {

	@ModuleSetting(allowsNull = false)
	@TextFieldSettingDescriptor(name = "Path to descriptor", description = "Path to descriptor that should be copied")
	protected String moduleDescriptorPath;

	protected WeakReference<SimpleForegroundModuleDescriptor> virtualForegroundModule = null;

	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		if (!new File(moduleDescriptorPath).exists()){

			log.warn("Module descriptor not found");

			return null;
		}

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));

		return new SimpleBackgroundModuleResponse(doc);
	}

	@Override
	public void init(BackgroundModuleDescriptor descriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(descriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(CloneBackgroundModule.class, this)){

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + CloneBackgroundModule.class.getName());
		}

		createVirtualForegroundModule();
	}

	@Override
	public void update(BackgroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		createVirtualForegroundModule();
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		SimpleForegroundModuleDescriptor foregroundModuleDescriptor = null;

		if (virtualForegroundModule != null && (foregroundModuleDescriptor = virtualForegroundModule.get()) != null){

			try{
				sectionInterface.getForegroundModuleCache().unload(foregroundModuleDescriptor);
			}catch(Exception e){
				log.error("Error unloading foreground module " + foregroundModuleDescriptor + " while unloading module " + moduleDescriptor, e);
			}
		}
		
		systemInterface.getInstanceHandler().removeInstance(CloneBackgroundModule.class, this);
	}

	private void createVirtualForegroundModule() throws Exception {

		boolean update = false;
		SimpleForegroundModuleDescriptor descriptor = null;

		if (virtualForegroundModule != null && (descriptor = virtualForegroundModule.get()) != null){
			update = true;
		}else{
			descriptor = new SimpleForegroundModuleDescriptor();
		}

		descriptor.setAlias("clonebackgroundmodule");
		descriptor.setName(this.moduleDescriptor.getName());
		descriptor.setDescription(this.moduleDescriptor.getName());
		descriptor.setClassname(CloneForegroundModule.class.getName());
		descriptor.setAdminAccess(this.moduleDescriptor.allowsAdminAccess());
		descriptor.setUserAccess(this.moduleDescriptor.allowsUserAccess());
		descriptor.setAnonymousAccess(this.moduleDescriptor.allowsAnonymousAccess());
		descriptor.setEnabled(true);
		descriptor.setSectionID(sectionInterface.getSectionDescriptor().getSectionID());
		descriptor.setStaticContentPackage("staticcontent");
		descriptor.setDataSourceID(this.moduleDescriptor.getDataSourceID());
		descriptor.setMutableSettingHandler(new SimpleSettingHandler());
		
		if (update){
			sectionInterface.getForegroundModuleCache().update(descriptor);
		}else{
			sectionInterface.getForegroundModuleCache().cache(descriptor);
			virtualForegroundModule = new WeakReference<SimpleForegroundModuleDescriptor>(descriptor);
		}
	}

	public void createInstance(User user) throws InstantiationException, IllegalAccessException, SQLException, ModuleAlreadyCachedException, ModuleAlreadyBeingCachedException, ModuleInstasiationException, ModuleInitializationException, CachePreconditionException {
		
		File file = new File(moduleDescriptorPath);
		
		if (file.getName().endsWith(".bgmodule")) {

			try {

				SimpleBackgroundModuleDescriptor descriptor = parseModuleDescriptor(file, SimpleBackgroundModuleDescriptor.class);
				systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().add(descriptor);
				sectionInterface.getBackgroundModuleCache().cache(descriptor);
				
				log.info("User " + user + " created new instance of " + descriptor);

			} catch (ValidationException e) {

				log.error("Bad backgroundmodule descriptor");
			}

		} else {

			log.error("Invalid descriptor extension");
		}
	}
	
	private <T extends XMLParserPopulateable> T parseModuleDescriptor(File file, Class<T> descriptorClazz) throws ValidationException, InstantiationException, IllegalAccessException {

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(file);

			Document doc = XMLUtils.parseXML(inputStream, false, false);

			T descriptor = descriptorClazz.newInstance();

			Element docElement = doc.getDocumentElement();

			if (!docElement.getTagName().equals("module")) {

				log.info("Error parsing descriptor " + file.getName() + ", unable to find module element");

				throw new ValidationException(new UnableToParseFileValidationError(file.getName()));
			}

			descriptor.populate(new XMLParser(docElement));

			return descriptor;

		} catch (SAXException e) {

			log.info("Error parsing descriptor " + file.getName(), e);

			throw new ValidationException(new UnableToParseFileValidationError(file.getName()));

		} catch (IOException e) {

			log.info("Error parsing descriptor " + file.getName(), e);

			throw new ValidationException(new UnableToParseFileValidationError(file.getName()));

		} catch (ParserConfigurationException e) {

			log.info("Error parsing descriptor " + file.getName(), e);

			throw new ValidationException(new UnableToParseFileValidationError(file.getName()));

		} catch (ValidationException e) {

			log.info("Error parsing descriptor " + file.getName(), e);

			throw new ValidationException(new UnableToParseFileValidationError(file.getName()));

		} finally {

			StreamUtils.closeStream(inputStream);
		}
	}
}