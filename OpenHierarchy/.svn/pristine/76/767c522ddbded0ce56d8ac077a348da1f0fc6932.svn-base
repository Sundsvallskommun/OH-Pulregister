package se.unlogic.hierarchy.core.utils;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionCacheListener;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.standardutils.collections.KeyAlreadyCachedException;
import se.unlogic.standardutils.collections.KeyNotCachedException;

/**
 * @author Robert "Unlogic" Olofsson
 *
 * This class finds and keeps track of any started foreground modules implementing the given class or interface
 *
 * @param <T>
 */
public class MultiForegroundModuleTracker<T> implements ForegroundModuleCacheListener, SectionCacheListener, SystemStartupListener {

	protected final Class<T> targetClass;
	protected final SectionInterface baseSection;
	protected final SystemInterface systemInterface;
	protected final boolean recursive;
	protected final boolean assignable;
	protected final ConcurrentHashMap<ForegroundModuleDescriptor, T> moduleMap = new ConcurrentHashMap<ForegroundModuleDescriptor, T>();
	protected CopyOnWriteArrayList<ForegroundModuleTrackerListener<T>> listeners;
	
	public MultiForegroundModuleTracker(Class<T> targetClass, SystemInterface systemInterface, SectionInterface baseSection, boolean recursive, boolean assignable) {

		this(targetClass, systemInterface, baseSection, recursive, assignable, null);
	}

	public MultiForegroundModuleTracker(Class<T> targetClass, SystemInterface systemInterface, SectionInterface baseSection, boolean recursive, boolean assignable, ForegroundModuleTrackerListener<T> listener) {

		this.targetClass = targetClass;
		this.systemInterface = systemInterface;
		this.baseSection = baseSection;
		this.recursive = recursive;
		this.assignable = assignable;
		
		if(listener != null){
			
			addListener(listener);
		}

		if(systemInterface.getSystemStatus() == SystemStatus.STARTING){

			systemInterface.addStartupListener(this);

		}else{

			systemStarted();
		}
	}

	@Override
	public void systemStarted() {

		//Scan all loaded foreground modules
		ModuleUtils.findForegroundModules(targetClass, recursive, assignable, baseSection, moduleMap);

		if (!moduleMap.isEmpty() && listeners != null) {
			
			for (Entry<ForegroundModuleDescriptor, T> moduleEntry : moduleMap.entrySet()) {
				for(ForegroundModuleTrackerListener<T> listener : listeners){
					listener.moduleCached(moduleEntry.getKey(), moduleEntry.getValue());
				}
			}
		}
		
		//Add required listener
		if(recursive){
			
			if(baseSection == systemInterface.getRootSection()){

				systemInterface.addForegroundModuleCacheListener(this);
				
			}else{
				
				addListeners(baseSection);
			}
			
			
		}else{
			
			baseSection.getForegroundModuleCache().addCacheListener(this);
		}
	}

	private void addListeners(SectionInterface sectionInterface) {

		for(Section section : sectionInterface.getSectionCache().getSections()){
			
			addListeners(section);
		}
		
		sectionInterface.getForegroundModuleCache().addCacheListener(this);
		sectionInterface.getSectionCache().addCacheListener(this);		
	}

	@Override
	@SuppressWarnings("unchecked")
	public void moduleCached(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

		if(moduleInstance.getClass().equals(targetClass) || (assignable && targetClass.isAssignableFrom(moduleInstance.getClass()))){

			moduleMap.put(moduleDescriptor, (T)moduleInstance);
			
			if(listeners != null){
				
				for(ForegroundModuleTrackerListener<T> listener : listeners){
					
					listener.moduleCached(moduleDescriptor, (T)moduleInstance);
				}
			}
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public void moduleUpdated(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

		if(moduleMap.contains(moduleDescriptor)){

			//Update the map with the latest module descriptor
			moduleMap.remove(moduleDescriptor, moduleInstance);
			moduleMap.put(moduleDescriptor, (T)moduleInstance);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void moduleUnloaded(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {
		
		if(this.moduleMap.remove(moduleDescriptor) != null){

			moduleMap.put(moduleDescriptor, (T)moduleInstance);
			
			if(listeners != null){
				
				for(ForegroundModuleTrackerListener<T> listener : listeners){
					
					listener.moduleCached(moduleDescriptor, (T)moduleInstance);
				}
			}
		}
	}

	public void shutdown() {

		systemInterface.removeForegroundModuleCacheListener(this);
		this.moduleMap.clear();
	}

	public Set<ForegroundModuleDescriptor> getDescriptors(){

		return moduleMap.keySet();
	}

	public Collection<T> getInstances(){

		return moduleMap.values();
	}

	public Set<Entry<ForegroundModuleDescriptor, T>> getEntries(){

		return moduleMap.entrySet();
	}

	public boolean isEmpty() {

		return moduleMap.isEmpty();
	}

	public int size() {

		return moduleMap.size();
	}
	
	@Override
	public String toString(){
		
		return this.getClass().getSimpleName() + " tracking " + targetClass.getSimpleName() + " in section " + baseSection.getSectionDescriptor() + " (recursive: " + recursive + ")";
	}

	@Override
	public void sectionCached(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyAlreadyCachedException {

		sectionInstance.getForegroundModuleCache().addCacheListener(this);
		sectionInstance.getSectionCache().addCacheListener(this);			
	}

	@Override
	public void sectionUpdated(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {}

	@Override
	public void sectionUnloaded(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {}
	
	public synchronized void addListener(ForegroundModuleTrackerListener<T> listener){
		
		if(this.listeners == null){
			
			listeners = new CopyOnWriteArrayList<ForegroundModuleTrackerListener<T>>();
		}
		
		listeners.add(listener);	
	}
	
	public void removeListener(ForegroundModuleTrackerListener<T> listener){
		
		if(this.listeners != null){
						
			listeners.remove(listener);
			
			if(listeners.isEmpty()){
				
				listeners = null;
			}
		}
	}	
}
