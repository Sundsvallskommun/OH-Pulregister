package se.unlogic.hierarchy.foregroundmodules.lucenesearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.events.SearchableItemAddEvent;
import se.unlogic.hierarchy.core.events.SearchableItemClearEvent;
import se.unlogic.hierarchy.core.events.SearchableItemDeleteEvent;
import se.unlogic.hierarchy.core.events.SearchableItemUpdateEvent;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.EventListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableSettingHandler;
import se.unlogic.hierarchy.core.interfaces.Searchable;
import se.unlogic.hierarchy.core.interfaces.SearchableItem;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans.NumberCriteria;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans.ObjectCriteria;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans.RangeCriteria;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans.SearchCriterias;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.events.AddEvent;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.events.ClearModuleEvent;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.events.DeleteEvent;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.events.QueuedSearchEvent;
import se.unlogic.hierarchy.foregroundmodules.lucenesearch.events.UpdateEvent;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class SearchModule extends AnnotatedForegroundModule implements ForegroundModuleCacheListener, SystemStartupListener {

	private static final String[] WILDCARD_EXCLUSIONS = new String[] { "?", "\"", "+", " ", "~","*", "-", "&&", "||", "^", ":"};
	private static final String[] SEARCH_FIELDS = new String[] { Constants.TITLE_FIELD, Constants.CONTENT_FIELD };

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Thread pool size", description = "The size of the thread pool used for indexing. The default size is determined by (Runtime.getRuntime().availableProcessors()).", formatValidator = PositiveStringIntegerValidator.class)
	protected int poolSize = Runtime.getRuntime().availableProcessors();

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max hits", description = "Maximum number of hits to get from index", formatValidator = PositiveStringIntegerValidator.class)
	protected int maxHitCount = 100;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Register in instance handler", description = "Controls if this module should register itself in the global instance handler.")
	boolean registerInInstanceHandler = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Auto convert to wildcard", description = "Auto convert searches to use wildcard where possible.")
	boolean autoCovertToWildcard = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Log item parsing", description = "Controls if parsing of searchable items should be logged or not")
	boolean logItemParsing = true;

	private EventListener<SearchableItemAddEvent> addEventListener = new AddEventListener();
	private EventListener<SearchableItemUpdateEvent> updateEventListener = new UpdateEventListener();
	private EventListener<SearchableItemDeleteEvent> deleteEventListener = new DeleteEventListener();
	private EventListener<SearchableItemClearEvent> clearEventListener = new ClearEventListener();

	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
	private Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;
	private AutoDetectParser parser = new AutoDetectParser();

	private LinkedBlockingQueue<QueuedSearchEvent> eventQueue = new LinkedBlockingQueue<QueuedSearchEvent>();

	private CallbackThreadPoolExecutor threadPoolExecutor;

	private ForegroundModuleDescriptor currentEventSource;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		indexWriter = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_44, analyzer));

		Map<ForegroundModuleDescriptor, Searchable> moduleMap = ModuleUtils.findForegroundModules(Searchable.class, true, true, systemInterface.getRootSection());

		for (Entry<ForegroundModuleDescriptor, Searchable> entry : moduleMap.entrySet()) {

			moduleCached(entry.getKey(), (ForegroundModule) entry.getValue());
		}

		systemInterface.getEventHandler().addEventListener(SearchableItem.class, SearchableItemAddEvent.class, addEventListener);
		systemInterface.getEventHandler().addEventListener(SearchableItem.class, SearchableItemUpdateEvent.class, updateEventListener);
		systemInterface.getEventHandler().addEventListener(SearchableItem.class, SearchableItemDeleteEvent.class, deleteEventListener);
		systemInterface.getEventHandler().addEventListener(SearchableItem.class, SearchableItemClearEvent.class, clearEventListener);

		systemInterface.addForegroundModuleCacheListener(this);

		if (systemInterface.getSystemStatus() != SystemStatus.STARTED) {

			systemInterface.addStartupListener(this);
		}

		if (registerInInstanceHandler) {

			if (!systemInterface.getInstanceHandler().addInstance(SearchModule.class, this)) {

				log.warn("Another instance has already been registered in instance handler for class " + SearchModule.class.getName());
			}
		}
	}

	@Override
	public void unload() throws Exception {

		if (registerInInstanceHandler) {

			if (systemInterface.getInstanceHandler().getInstance(SearchModule.class) == this) {

				systemInterface.getInstanceHandler().removeInstance(SearchModule.class);
			}
		}

		systemInterface.removeForegroundModuleCacheListener(this);

		systemInterface.getEventHandler().removeEventListener(SearchableItem.class, SearchableItemAddEvent.class, addEventListener);
		systemInterface.getEventHandler().removeEventListener(SearchableItem.class, SearchableItemUpdateEvent.class, updateEventListener);
		systemInterface.getEventHandler().removeEventListener(SearchableItem.class, SearchableItemDeleteEvent.class, deleteEventListener);
		systemInterface.getEventHandler().removeEventListener(SearchableItem.class, SearchableItemClearEvent.class, clearEventListener);

		threadPoolExecutor.shutdownNow();
		threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS);

		this.eventQueue.clear();
		threadPoolExecutor.purge();

		try {
			indexWriter.close();
		} catch (IOException e) {
			log.warn("Error closing index writer", e);
		}

		try {
			index.close();
		} catch (IOException e) {
			log.warn("Error closing index", e);
		}

		super.unload();
	}

	@Override
	protected void parseSettings(MutableSettingHandler mutableSettingHandler) throws Exception {

		super.parseSettings(mutableSettingHandler);

		if (threadPoolExecutor == null) {

			threadPoolExecutor = new CallbackThreadPoolExecutor(poolSize, poolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), this);

		} else {
			threadPoolExecutor.setMaximumPoolSize(poolSize);
			threadPoolExecutor.setCorePoolSize(poolSize);
		}
	}

	public List<String> getMatchingSearchItemIDs(ForegroundModuleDescriptor moduleDescriptor, String queryString, int maxHitCount) throws ParseException, IOException {

		//Check if the index contains any documents
		if (indexReader == null || indexReader.numDocs() == 0) {

			return null;
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

		queryString = rewriteQueryString(queryString);

		Query textQuery = parser.parse(queryString);

		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(textQuery, Occur.MUST);
		booleanQuery.add(new TermQuery(new Term(Constants.DESCRIPTOR_HASHCODE_FIELD, String.valueOf(moduleDescriptor.hashCode()))), Occur.MUST);

		TopDocs results = searcher.search(booleanQuery, maxHitCount);

		if (results.scoreDocs.length == 0) {

			return null;
		}

		List<String> itemIDs = new ArrayList<String>(results.scoreDocs.length);

		for (ScoreDoc scoreDoc : results.scoreDocs) {

			Document doc = searcher.doc(scoreDoc.doc);

			itemIDs.add(doc.get(Constants.ITEM_ID_FIELD));
		}

		return itemIDs;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws IOException {

		org.w3c.dom.Document xmlDocument = createDocument(req, uriParser, user);

		Element searchElement = xmlDocument.createElement("Search");
		xmlDocument.getDocumentElement().appendChild(searchElement);

		String queryString = req.getParameter("q");

		//Check if query string is empty
		if (StringUtils.isEmpty(queryString)) {

			return new SimpleForegroundModuleResponse(xmlDocument, getDefaultBreadcrumb());
		}

		String rewrittenQueryString = rewriteQueryString(queryString);

		log.info("User " + user + " searching for: " + StringUtils.toLogFormat(queryString, 50));

		XMLUtils.appendNewCDATAElement(xmlDocument, searchElement, "QueryString", queryString);

		//Check if the index contains any documents
		if (indexReader == null || indexReader.numDocs() == 0) {

			searchElement.appendChild(xmlDocument.createElement("NoHits"));

			return new SimpleForegroundModuleResponse(xmlDocument, getDefaultBreadcrumb());
		}
		
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

		Query query;

		try {
			query = parser.parse(rewrittenQueryString);

		} catch (ParseException e) {

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(rewrittenQueryString, 50) + " requsted by user " + user + " accessing from " + req.getRemoteAddr());

			searchElement.appendChild(xmlDocument.createElement("UnableToParseQueryString"));

			return new SimpleForegroundModuleResponse(xmlDocument, getDefaultBreadcrumb());
		}

		SearchCriterias searchCriterias = getSearchCriterias(req, user, uriParser);
		
		List<Hit> filteredHits;
		
		if(searchCriterias == null){
			
			filteredHits = search(query, user);
			
		} else {
			
			filteredHits = search(query, user, searchCriterias);
		}
		
		if (filteredHits == null) {

			searchElement.appendChild(xmlDocument.createElement("NoHits"));

			return new SimpleForegroundModuleResponse(xmlDocument, getDefaultBreadcrumb());
		}

		XMLUtils.appendNewElement(xmlDocument, searchElement, "HitCount", filteredHits.size());
		XMLUtils.append(xmlDocument, searchElement, "Hits", filteredHits);

		return new SimpleForegroundModuleResponse(xmlDocument, getDefaultBreadcrumb());
	}

	protected SearchCriterias getSearchCriterias(HttpServletRequest req, User user, URIParser uriParser) {

		return null;
	}

	public List<Hit> search(String queryString, User user, Map<String, String> requiredAttributes) throws ParseException, IOException{
		
		//Check if the index contains any documents
		if (indexReader == null || indexReader.numDocs() == 0) {

			return null;
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

		Query query = parser.parse(rewriteQueryString(queryString));
		
		if(!CollectionUtils.isEmpty(requiredAttributes)){
			
			BooleanQuery attributeQuery = new BooleanQuery();
			
			for(Entry<String, String> attributeEntry : requiredAttributes.entrySet()){
				
				attributeQuery.add(new TermQuery(new Term(Constants.ATTRIBUTE_FIELD_PREFIX + attributeEntry.getKey(), attributeEntry.getValue())), Occur.MUST);
			}
			
			BooleanQuery combinedQuery = new BooleanQuery();
			
			combinedQuery.add(query, Occur.MUST);
			combinedQuery.add(attributeQuery, Occur.MUST);
			
			query = combinedQuery;
		}
		
		return search(query, user);
	}
	
	public List<Hit> search(String queryString, User user, SearchCriterias searchCriterias) throws ParseException, IOException {
		
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

		Query query = parser.parse(rewriteQueryString(queryString));
		
		return search(query, user, searchCriterias);
	}
	
	public List<Hit> search(Query query, User user, SearchCriterias searchCriterias) throws IOException {

		//Check if the index contains any documents
		if (indexReader == null || indexReader.numDocs() == 0) {

			return null;
		}

		if (searchCriterias != null) {

			BooleanQuery combinedQuery = new BooleanQuery();

			combinedQuery.add(query, Occur.MUST);

			if (!CollectionUtils.isEmpty(searchCriterias.getEqualCriterias())) {

				for (ObjectCriteria objectCriteria : searchCriterias.getEqualCriterias()) {

					Query criteriaQuery = getEqualQuery(Constants.ATTRIBUTE_FIELD_PREFIX + objectCriteria.getAttributeName(), objectCriteria.getParameter());

					if (objectCriteria.isRequired()) {

						combinedQuery.add(criteriaQuery, Occur.MUST);

					} else {

						combinedQuery.add(criteriaQuery, Occur.SHOULD);
					}
				}
			}

			if (!CollectionUtils.isEmpty(searchCriterias.getEqualCriterias())) {

				for (ObjectCriteria objectCriteria : searchCriterias.getEqualCriterias()) {

					BooleanQuery criteriaQuery = new BooleanQuery();
					criteriaQuery.add(getEqualQuery(Constants.ATTRIBUTE_FIELD_PREFIX + objectCriteria.getAttributeName(), objectCriteria.getParameter()), Occur.MUST_NOT);

					if (objectCriteria.isRequired()) {

						combinedQuery.add(criteriaQuery, Occur.MUST);

					} else {

						combinedQuery.add(criteriaQuery, Occur.SHOULD);
					}
				}
			}

			if (!CollectionUtils.isEmpty(searchCriterias.getSmallerThanCriterias())) {

				for (NumberCriteria numberCriteria : searchCriterias.getSmallerThanCriterias()) {

					Query criteriaQuery = getRangeQuery(Constants.ATTRIBUTE_FIELD_PREFIX + numberCriteria.getAttributeName(), null, numberCriteria.getParameter());

					if (numberCriteria.isRequired()) {

						combinedQuery.add(criteriaQuery, Occur.MUST);

					} else {

						combinedQuery.add(criteriaQuery, Occur.SHOULD);
					}
				}
			}

			if (!CollectionUtils.isEmpty(searchCriterias.getLargerThanCriterias())) {

				for (NumberCriteria numberCriteria : searchCriterias.getLargerThanCriterias()) {

					Query criteriaQuery = getRangeQuery(Constants.ATTRIBUTE_FIELD_PREFIX + numberCriteria.getAttributeName(), numberCriteria.getParameter(), null);

					if (numberCriteria.isRequired()) {

						combinedQuery.add(criteriaQuery, Occur.MUST);

					} else {

						combinedQuery.add(criteriaQuery, Occur.SHOULD);
					}
				}
			}

			if (!CollectionUtils.isEmpty(searchCriterias.getRangeCriterias())) {

				for (RangeCriteria rangeCriteria : searchCriterias.getRangeCriterias()) {

					Query criteriaQuery = getRangeQuery(Constants.ATTRIBUTE_FIELD_PREFIX + rangeCriteria.getAttributeName(), rangeCriteria.getParameterLow(), rangeCriteria.getparameterHigh());

					if (rangeCriteria.isRequired()) {

						combinedQuery.add(criteriaQuery, Occur.MUST);

					} else {

						combinedQuery.add(criteriaQuery, Occur.SHOULD);
					}
				}
			}

			query = combinedQuery;
		}

		return search(query, user);
	}

	private Query getEqualQuery(String attributeName, Object value) {

		if (value instanceof Integer) {

			Integer val = (Integer) value;
			return NumericRangeQuery.newIntRange(attributeName, val, val, true, true);

		} else if (value instanceof Long) {

			Long val = (Long) value;
			return NumericRangeQuery.newLongRange(attributeName, val, val, true, true);

		} else if (value instanceof Float) {

			Float val = (Float) value;
			return NumericRangeQuery.newFloatRange(attributeName, val, val, true, true);

		} else if (value instanceof Double) {

			Double val = (Double) value;
			return NumericRangeQuery.newDoubleRange(attributeName, val, val, true, true);

		} else {

			return new TermQuery(new Term(attributeName, value.toString()));
		}
	}

	private Query getRangeQuery(String attributeName, Number low, Number high) {

		if (low instanceof Integer || high instanceof Integer) {

			Integer lowVal = (Integer) low;
			Integer highVal = (Integer) high;
			return NumericRangeQuery.newIntRange(attributeName, lowVal, highVal, true, true);

		} else if (low instanceof Long || high instanceof Long) {

			Long lowVal = (Long) low;
			Long highVal = (Long) high;
			return NumericRangeQuery.newLongRange(attributeName, lowVal, highVal, true, true);

		} else if (low instanceof Float || high instanceof Float) {

			Float lowVal = (Float) low;
			Float highVal = (Float) high;
			return NumericRangeQuery.newFloatRange(attributeName, lowVal, highVal, true, true);

		} else if (low instanceof Double || high instanceof Long) {

			Double lowVal = (Double) low;
			Double highVal = (Double) high;
			return NumericRangeQuery.newDoubleRange(attributeName, lowVal, highVal, true, true);

		} else {

			throw new InvalidParameterException("Unsupported number types");
		}
	}
	
	private List<Hit> search(Query query, User user) throws IOException {

		Filter filter = getFilter(user);

		TopDocs results = searcher.search(query, filter, maxHitCount);

		if (results.scoreDocs.length == 0) {

			return null;
		}

		QueryScorer scorer = new QueryScorer(query, Constants.CONTENT_FIELD);
		Highlighter highlighter = new Highlighter(scorer);

		List<Hit> filteredHits = new ArrayList<Hit>(results.scoreDocs.length);

		for (ScoreDoc scoreDoc : results.scoreDocs) {

			Document doc = searcher.doc(scoreDoc.doc);

			int sectionID = NumberUtils.toInt(doc.get(Constants.SECTION_ID_FIELD));

			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface == null) {

				continue;
			}

			int descriptorHashCode = NumberUtils.toInt(doc.get(Constants.DESCRIPTOR_HASHCODE_FIELD));

			ForegroundModuleDescriptor moduleDescriptor = sectionInterface.getForegroundModuleCache().getDescriptorByHashCode(descriptorHashCode);

			if (moduleDescriptor == null || !AccessUtils.checkAccess(user, moduleDescriptor) || !AccessUtils.checkRecursiveModuleAccess(user, sectionInterface)) {

				continue;
			}

			String title = doc.get(Constants.TITLE_FIELD);
			String alias = sectionInterface.getSectionDescriptor().getFullAlias() + "/" + moduleDescriptor.getAlias() + "/" + doc.get(Constants.ALIAS_FIELD);

			//Get fragment
			TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, Constants.CONTENT_FIELD, doc, analyzer);

			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);

			highlighter.setTextFragmenter(fragmenter);

			String storedField = doc.get(Constants.CONTENT_FIELD);

			String fragment;

			try {
				fragment = highlighter.getBestFragment(stream, storedField);

			} catch (InvalidTokenOffsetsException e) {

				fragment = null;
			}

			filteredHits.add(new Hit(alias, title, fragment));
		}

		if(filteredHits.isEmpty()){
			
			return null;
		}
		
		return filteredHits;
	}
	
	protected Filter getFilter(User user) {

		BooleanQuery booleanQuery = new BooleanQuery();

		if(user == null){

			booleanQuery.add(new TermQuery(new Term(Constants.ANONYMOUS_ACCESS_FIELD, "true")), Occur.SHOULD);

		}else{

			booleanQuery.add(new TermQuery(new Term(Constants.USER_ACCESS_FIELD, "true")), Occur.SHOULD);

			if(user.getUserID() != null){

				//booleanQuery.add(new TermQuery(new Term(Constants.ALLOWED_USER_FIELD, user.getUserID().toString())), Occur.SHOULD);
				booleanQuery.add(NumericRangeQuery.newIntRange(Constants.ALLOWED_USER_FIELD, user.getUserID(), user.getUserID(), true, true), Occur.SHOULD);
			}

			if(user.getGroups() != null){

				for(Group group : user.getGroups()){

					if(group.getGroupID() != null){

						//booleanQuery.add(new TermQuery(new Term(Constants.ALLOWED_GROUP_FIELD, group.getGroupID().toString())), Occur.SHOULD);
						booleanQuery.add(NumericRangeQuery.newIntRange(Constants.ALLOWED_GROUP_FIELD, group.getGroupID(), group.getGroupID(), true, true), Occur.SHOULD);
					}
				}
			}
		}

		return new QueryWrapperFilter(booleanQuery);
	}

	protected String rewriteQueryString(String queryString) {

		if(autoCovertToWildcard){

			for(String exclusion : WILDCARD_EXCLUSIONS){

				if(queryString.contains(exclusion)){

					return queryString;
				}
			}

			return queryString + "*";
		}

		return queryString;
	}

	@Override
	public void moduleCached(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule instance) {

		if (instance instanceof Searchable) {

			try {
				//Add module items to index
				List<? extends SearchableItem> items = ((Searchable) instance).getSearchableItems();

				if (CollectionUtils.isEmpty(items)) {

					log.debug("Searchable module " + moduleDescriptor + " returned no items, skipping.");

					return;
				}

				queueAddEvent(moduleDescriptor, items);

			} catch (Exception e) {

				log.error("Error getting searchable items from module " + moduleDescriptor, e);
			}
		}
	}

	@Override
	public void moduleUpdated(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

	}

	@Override
	public void moduleUnloaded(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule instance) {

		if (systemInterface.getSystemStatus() == SystemStatus.STOPPING) {

			//Don't bother clearing the index if the system is stopping

			return;
		}

		if (instance instanceof Searchable) {

			queueClearEvent(moduleDescriptor);
		}
	}

	public void onAddEvent(SearchableItemAddEvent event) {

		queueAddEvent(event.getModuleDescriptor(), event.getItems());
	}

	public void onUpdateEvent(SearchableItemUpdateEvent event) {

		log.info("Queued update event from module " + event.getModuleDescriptor() + " containing " + event.getItems().size() + " items.");
		eventQueue.add(new UpdateEvent(event.getModuleDescriptor(), event.getItems()));
		checkQueueState(false);
	}

	public void onDeleteEvent(SearchableItemDeleteEvent event) {

		log.info("Queued delete event from module " + event.getModuleDescriptor() + " containing " + event.getItemIDs().size() + " item ID's.");
		eventQueue.add(new DeleteEvent(event.getModuleDescriptor(), event.getItemIDs()));
		checkQueueState(false);
	}

	public void onClearEvent(SearchableItemClearEvent event) {

		queueClearEvent(event.getModuleDescriptor());
	}

	private void queueAddEvent(ForegroundModuleDescriptor moduleDescriptor, List<? extends SearchableItem> items) {

		log.info("Queued add event from module " + moduleDescriptor + " containing " + items.size() + " items.");
		eventQueue.add(new AddEvent(moduleDescriptor, items));
		checkQueueState(false);
	}

	private synchronized void queueClearEvent(ForegroundModuleDescriptor moduleDescriptor) {

		//Clear any previously queued events from this module
		Iterator<QueuedSearchEvent> iterator = eventQueue.iterator();

		while (iterator.hasNext()) {

			if (iterator.next().getModuleDescriptor().equals(moduleDescriptor)) {

				iterator.remove();
			}
		}

		if (currentEventSource != null && currentEventSource.equals(moduleDescriptor)) {

			threadPoolExecutor.getQueue().clear();
		}

		log.info("Queued clear event from module " + moduleDescriptor);
		eventQueue.add(new ClearModuleEvent(moduleDescriptor));
		checkQueueState(false);
	}

	public Document parseItem(ForegroundModuleDescriptor moduleDescriptor, SearchableItem item) throws Exception {

		if(logItemParsing){

			log.info("Parsing item " + item.getTitle() + " (ID: " + item.getID() + ") from module " + moduleDescriptor);
		}

		StringWriter writer = new StringWriter();
		ContentHandler contentHandler = new BodyContentHandler(writer);

		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, item.getTitle());
		metadata.set(Metadata.CONTENT_TYPE, item.getContentType());

		InputStream inputStream = null;

		try {
			inputStream = item.getData();

			if (inputStream == null) {

				return null;
			}

			parser.parse(inputStream, contentHandler, metadata);

			Document doc = new Document();
			doc.add(new TextField(Constants.TITLE_FIELD, item.getTitle(), Field.Store.YES));
			doc.add(new LongField(Constants.LAST_MODIFIED_FIELD, item.getLastModified(), Field.Store.YES));
			doc.add(new TextField(Constants.CONTENT_FIELD, StringUtils.replaceUTF8Quotes(writer.toString()), Field.Store.YES));
			doc.add(new StringField(Constants.ITEM_ID_FIELD, item.getID(), Field.Store.YES));
			doc.add(new StringField(Constants.DESCRIPTOR_HASHCODE_FIELD, Integer.toString(moduleDescriptor.hashCode()), Field.Store.YES));
			doc.add(new IntField(Constants.SECTION_ID_FIELD, moduleDescriptor.getSectionID(), Field.Store.YES));
			doc.add(new StringField(Constants.ALIAS_FIELD, item.getAlias(), Field.Store.YES));
			
			if(item.getAttributes() != null){
				
				for(Entry<String, ? extends Object> attributeEntry : item.getAttributes().entrySet()){
					
					Object value = attributeEntry.getValue();
					
					if(value instanceof Long){
						
						doc.add(new LongField(Constants.ATTRIBUTE_FIELD_PREFIX + attributeEntry.getKey(), (Long) value, Field.Store.YES));
						
					} else if(value instanceof Integer){
						
						doc.add(new IntField(Constants.ATTRIBUTE_FIELD_PREFIX + attributeEntry.getKey(), (Integer) value, Field.Store.YES));
						
					} else if(value instanceof Float){
						
						doc.add(new FloatField(Constants.ATTRIBUTE_FIELD_PREFIX + attributeEntry.getKey(), (Float) value, Field.Store.YES));
						
					} else if(value instanceof Double){
						
						doc.add(new DoubleField(Constants.ATTRIBUTE_FIELD_PREFIX + attributeEntry.getKey(), (Double) value, Field.Store.YES));
						
					} else {
						
						doc.add(new StringField(Constants.ATTRIBUTE_FIELD_PREFIX + attributeEntry.getKey(), value.toString(), Field.Store.YES));
					}
				}
			}
			
			AccessInterface accessInterface = item.getAccessInterface();

			if (accessInterface == null) {

				//Allow access for all users
				doc.add(new StringField(Constants.ANONYMOUS_ACCESS_FIELD, "true", Field.Store.YES));
				doc.add(new StringField(Constants.USER_ACCESS_FIELD, "true", Field.Store.YES));

			}else{

				doc.add(new StringField(Constants.ANONYMOUS_ACCESS_FIELD, Boolean.toString(accessInterface.allowsAnonymousAccess()), Field.Store.YES));
				doc.add(new StringField(Constants.USER_ACCESS_FIELD, Boolean.toString(accessInterface.allowsUserAccess()), Field.Store.YES));

				if(accessInterface.getAllowedGroupIDs() != null){

					for(Integer groupID : accessInterface.getAllowedGroupIDs()){

						doc.add(new IntField(Constants.ALLOWED_GROUP_FIELD, groupID, Field.Store.YES));
					}
				}

				if(accessInterface.getAllowedUserIDs() != null){

					for(Integer userID : accessInterface.getAllowedUserIDs()){

						doc.add(new IntField(Constants.ALLOWED_USER_FIELD, userID, Field.Store.YES));
					}
				}
			}

			return doc;

		} finally {

			StreamUtils.closeStream(inputStream);
		}
	}

	protected class AddEventListener implements EventListener<SearchableItemAddEvent> {

		@Override
		public void processEvent(SearchableItemAddEvent event, EventSource source) {

			SearchModule.this.onAddEvent(event);
		}

		@Override
		public int getPriority() {

			return 0;
		}
	}

	protected class UpdateEventListener implements EventListener<SearchableItemUpdateEvent> {

		@Override
		public void processEvent(SearchableItemUpdateEvent event, EventSource source) {

			SearchModule.this.onUpdateEvent(event);
		}

		@Override
		public int getPriority() {

			return 0;
		}
	}

	protected class DeleteEventListener implements EventListener<SearchableItemDeleteEvent> {

		@Override
		public void processEvent(SearchableItemDeleteEvent event, EventSource source) {

			SearchModule.this.onDeleteEvent(event);
		}

		@Override
		public int getPriority() {

			return 0;
		}
	}

	protected class ClearEventListener implements EventListener<SearchableItemClearEvent> {

		@Override
		public void processEvent(SearchableItemClearEvent event, EventSource source) {

			SearchModule.this.onClearEvent(event);
		}

		@Override
		public int getPriority() {

			return 0;
		}
	}

	public synchronized void checkQueueState(boolean commit) {

		if (systemInterface.getSystemStatus() != SystemStatus.STARTED) {

			return;
		}

		synchronized (this) {

			if (threadPoolExecutor.getExecutingThreadCount() == 0 && threadPoolExecutor.getQueue().isEmpty()) {

				try {
					if (commit) {

						log.info("Committing index changes from last event.");
						this.indexWriter.commit();
						this.indexReader = DirectoryReader.open(index);
						this.searcher = new IndexSearcher(indexReader);
					}

					QueuedSearchEvent nextEvent = eventQueue.poll();

					if (nextEvent == null) {

						currentEventSource = null;
						log.info("No queued search events found, thread pool idle.");
						return;
					}

					log.info("Processing " + nextEvent);
					currentEventSource = nextEvent.getModuleDescriptor();

					nextEvent.queueTasks(threadPoolExecutor, this);

				} catch (IOException e) {
					log.error("Unable to commit index", e);
				}
			}
		}
	}

	public void addDocument(Iterable<? extends IndexableField> doc) throws IOException {

		indexWriter.addDocument(doc);
	}

	public void deleteDocuments(Term... terms) throws IOException {

		BooleanQuery query = new BooleanQuery();

		for (Term term : terms) {

			query.add(new TermQuery(term), Occur.MUST);
		}

		indexWriter.deleteDocuments(query);
	}

	public org.w3c.dom.Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		org.w3c.dom.Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));
		XMLUtils.appendNewCDATAElement(doc, documentElement, "FullAlias", getFullAlias());
		XMLUtils.appendNewCDATAElement(doc, documentElement, "ContextPath", req.getContextPath());
		XMLUtils.appendNewCDATAElement(doc, documentElement, "ServerURL", RequestUtils.getServerURL(req));
		doc.appendChild(documentElement);
		return doc;
	}

	@Override
	public void systemStarted() {

		checkQueueState(false);
	}

	public boolean isValidSystemState() {

		return systemInterface.getSystemStatus() == SystemStatus.STARTED;
	}
}
