package se.unlogic.hierarchy.foregroundmodules.lucenesearch.events;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SearchableItem;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.tasks.UpdateTask;

public class UpdateEvent extends AddEvent {

	public UpdateEvent(ForegroundModuleDescriptor moduleDescriptor, List<SearchableItem> items) {

		super(moduleDescriptor, items);
	}

	@Override
	public void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule) {

		for(SearchableItem item : items){

			executor.execute(new UpdateTask(moduleDescriptor, item, searchModule));
		}
	}

	@Override
	public String toString() {

		return "update event with " + items.size() + " items from module " + moduleDescriptor;
	}
}
