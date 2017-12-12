package se.unlogic.hierarchy.foregroundmodules.lucenesearch.events;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SearchableItem;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.tasks.AddTask;


public class AddEvent extends QueuedSearchEvent {

	protected final List<? extends SearchableItem> items;

	public AddEvent(ForegroundModuleDescriptor moduleDescriptor, List<? extends SearchableItem> items) {

		super(moduleDescriptor);
		this.items = items;
	}

	@Override
	public void queueTasks(ThreadPoolExecutor executor, SearchModule searchModule) {

		for(SearchableItem item : items){

			try {
				executor.execute(new AddTask(moduleDescriptor, item, searchModule));
			} catch (RejectedExecutionException e) {}
		}
	}

	@Override
	public int getTaskCount() {

		return items.size();
	}

	@Override
	public String toString() {

		return "add event with " + items.size() + " items from module " + moduleDescriptor;
	}
}
