package se.unlogic.hierarchy.foregroundmodules.lucenesearch;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class Hit extends GeneratedElementable {

	@XMLElement(fixCase = true)
	private final String fullAlias;

	@XMLElement(fixCase = true)
	private final String title;

	@XMLElement(fixCase = true)
	private final String fragment;

	public Hit(String fullAlias, String title, String fragment) {

		super();
		this.fullAlias = fullAlias;
		this.title = title;
		this.fragment = fragment;
	}

	public String getFullAlias() {

		return fullAlias;
	}

	public String getTitle() {

		return title;
	}

	public String getFragment() {

		return fragment;
	}

	@Override
	public String toString() {

		return title + "( " + fullAlias + ")";
	}

}
