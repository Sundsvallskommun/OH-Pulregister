package se.unlogic.hierarchy.core.tasks;

import org.apache.log4j.Logger;

import se.unlogic.hierarchy.core.cache.BaseModuleCache;
import se.unlogic.hierarchy.core.interfaces.ModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;


public class ModuleStartupTask<DescriptorType extends ModuleDescriptor, ModuleCacheType extends BaseModuleCache<DescriptorType, ?, ?>> implements Runnable {

	protected final Logger log = Logger.getLogger(this.getClass());
	
	private final DescriptorType moduleDescriptor;
	private final ModuleCacheType moduleCache;
	private final SectionDescriptor sectionDescriptor;

	public ModuleStartupTask(DescriptorType moduleDescriptor, ModuleCacheType moduleCache, SectionDescriptor sectionDescriptor) {

		super();
		this.moduleDescriptor = moduleDescriptor;
		this.moduleCache = moduleCache;
		this.sectionDescriptor = sectionDescriptor;
	}

	@Override
	public void run() {

		try {
			log.info("Adding " + moduleDescriptor.getType().getName() + " module " + moduleDescriptor + " to cache" + getSectionString());
			
			moduleCache.cache(moduleDescriptor);
			
		} catch (Throwable t) {
			
			log.error("Error caching " + moduleDescriptor.getType().getName() + " module " + moduleDescriptor + getSectionString(), t);
		}
	}

	private String getSectionString() {

		if(sectionDescriptor != null){
			
			return " in section " + sectionDescriptor.toString();
			
		}else{
			
			return "";
		}
	}
}
