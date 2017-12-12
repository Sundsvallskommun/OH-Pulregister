package se.unlogic.hierarchy.foregroundmodules.lucenesearch.tasks;

import java.io.IOException;

import org.apache.lucene.document.Document;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SearchableItem;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.SearchModule;

public class AddTask extends BaseTask implements Runnable {

	protected final SearchableItem item;

	public AddTask(ForegroundModuleDescriptor moduleDescriptor, SearchableItem item, SearchModule searchModule) {

		super(moduleDescriptor, searchModule);
		this.item = item;
	}

	@Override
	public void run() {

		if(!searchModule.isValidSystemState()){
			
			return;
		}
		
		Document doc;

		try{
			doc = searchModule.parseItem(moduleDescriptor, item);

		}catch(Throwable e){

			log.error("Error parsing item " + item + " from module " + moduleDescriptor,e);
			return;
		}

		if(doc == null){

			//Item no longer available skip it
			log.info("Item " + item + " from module " + moduleDescriptor + " no longer available, skipping.");
			return;
		}

		try{
			searchModule.addDocument(doc);
		}catch(IOException e){

			log.error("Error adding document to index. Dcoument generated from item " + item + " from module " + moduleDescriptor, e);
		}
	}

}
