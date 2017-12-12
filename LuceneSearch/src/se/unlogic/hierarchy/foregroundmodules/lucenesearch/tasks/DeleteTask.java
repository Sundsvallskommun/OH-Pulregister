package se.unlogic.hierarchy.foregroundmodules.lucenesearch.tasks;

import java.io.IOException;

import org.apache.lucene.index.Term;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.Constants;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;

public class DeleteTask extends BaseTask implements Runnable {

	private final String itemID;

	public DeleteTask(ForegroundModuleDescriptor moduleDescriptor, String itemID, SearchModule searchModule) {

		super(moduleDescriptor, searchModule);

		this.itemID = itemID;
	}

	@Override
	public void run() {

		try{
			searchModule.deleteDocuments(new Term(Constants.DESCRIPTOR_HASHCODE_FIELD, String.valueOf(moduleDescriptor.hashCode())), new Term(Constants.ITEM_ID_FIELD, itemID));
		}catch(IOException e){

			log.error("Error deleteing document with item ID " + itemID + " from module " + moduleDescriptor + " from index", e);

			return;
		}
	}
}
