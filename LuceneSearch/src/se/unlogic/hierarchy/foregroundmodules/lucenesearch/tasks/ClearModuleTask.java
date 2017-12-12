package se.unlogic.hierarchy.foregroundmodules.lucenesearch.tasks;

import java.io.IOException;

import org.apache.lucene.index.Term;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.Constants;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;


public class ClearModuleTask extends BaseTask {

	public ClearModuleTask(ForegroundModuleDescriptor moduleDescriptor, SearchModule searchModule) {

		super(moduleDescriptor, searchModule);
	}

	@Override
	public void run() {

		try{
			searchModule.deleteDocuments(new Term(Constants.DESCRIPTOR_HASHCODE_FIELD, String.valueOf(moduleDescriptor.hashCode())));

		}catch(IOException e){

			log.error("Error deleteing documents module ID " + moduleDescriptor.getModuleID() + " from index", e);

			return;
		}
	}

}
