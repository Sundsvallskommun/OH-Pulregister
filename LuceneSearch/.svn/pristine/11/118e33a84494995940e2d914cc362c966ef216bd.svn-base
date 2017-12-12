package se.unlogic.hierarchy.foregroundmodules.lucenesearch.tasks;

import java.io.IOException;

import org.apache.lucene.index.Term;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SearchableItem;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.Constants;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;

public class UpdateTask extends AddTask implements Runnable {

	public UpdateTask(ForegroundModuleDescriptor moduleDescriptor, SearchableItem item, SearchModule searchModule) {

		super(moduleDescriptor, item, searchModule);
	}

	@Override
	public void run() {

		try{
			searchModule.deleteDocuments(new Term(Constants.DESCRIPTOR_HASHCODE_FIELD, String.valueOf(moduleDescriptor.hashCode())), new Term(Constants.ITEM_ID_FIELD, item.getID()));
		}catch(IOException e){

			log.error("Error deleteing document with item ID " + item.getID() + " from module " + moduleDescriptor + " from index", e);

			return;
		}

		super.run();
	}

}
