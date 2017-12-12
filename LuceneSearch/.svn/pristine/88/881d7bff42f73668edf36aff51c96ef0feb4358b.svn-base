package se.unlogic.hierarchy.foregroundmodules.lucenesearch.events;

import java.util.concurrent.ThreadPoolExecutor;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.tasks.ClearModuleTask;


public class ClearModuleEvent extends QueuedSearchEvent {

	public ClearModuleEvent(ForegroundModuleDescriptor moduleDescriptor) {

		super(moduleDescriptor);
	}

	@Override
	public void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule) {

		executor.execute(new ClearModuleTask(moduleDescriptor, searchModule));
	}

	@Override
	public int getTaskCount() {

		return 1;
	}

	@Override
	public String toString() {

		return "clear event from module " + moduleDescriptor;
	}
}
