package se.unlogic.hierarchy.core.cache;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import se.unlogic.standardutils.xsl.XSLVariableReader;


public class XSLVariableReaderCache {
	
	private static final ReferenceQueue<XSLVariableReader> REFERENCE_QUEUE = new ReferenceQueue<XSLVariableReader>();
	
	private static final ConcurrentHashMap<URI, ValueReference> CACHE_MAP = new ConcurrentHashMap<URI, ValueReference>();
	
	public static XSLVariableReader getTemplates(URI uri) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, URISyntaxException {
		
		processReferenceQueue();
		
		ValueReference reference = CACHE_MAP.get(uri);
		
		XSLVariableReader variableReader;
		
		if(reference != null){
		
			variableReader = reference.get();
			
			if(variableReader != null){
				
				//System.out.println("Cache hit for URI: " + uri);
				
				return variableReader;
			}			
		}
		
		//System.out.println("Caching URI: " + uri);
		
		variableReader = new XSLVariableReader(uri);

		CACHE_MAP.put(uri, new ValueReference(uri ,variableReader, REFERENCE_QUEUE));
		
		return variableReader;
	}

	private static void processReferenceQueue() {

		Reference<? extends XSLVariableReader> reference;
		
		while((reference = REFERENCE_QUEUE.poll()) != null){
			
			URI uri = ((ValueReference)reference).getURI();
			
			CACHE_MAP.remove(uri);
			
			//System.out.println("GC:ed URI: " + uri);
		}
	}
	
	private static final class ValueReference extends SoftReference<XSLVariableReader>{

		private final URI uri;
		
		public ValueReference(URI uri, XSLVariableReader variableReader, ReferenceQueue<XSLVariableReader> queue) {

			super(variableReader, queue);

			this.uri = uri;
		}
		
		public URI getURI() {
	
			return uri;
		}
	}
}
