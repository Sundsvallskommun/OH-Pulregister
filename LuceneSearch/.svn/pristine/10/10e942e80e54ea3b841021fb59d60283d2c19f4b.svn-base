package se.unlogic.hierarchy.foregroundmodules.lucenesearch.events;

import java.util.concurrent.ThreadPoolExecutor;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;

public abstract class QueuedSearchEvent {

	protected final ForegroundModuleDescriptor moduleDescriptor;

	public QueuedSearchEvent(ForegroundModuleDescriptor moduleDescriptor) {

		super();
		this.moduleDescriptor = moduleDescriptor;
	}

	public abstract void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule);

	public abstract int getTaskCount();

	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}
}
