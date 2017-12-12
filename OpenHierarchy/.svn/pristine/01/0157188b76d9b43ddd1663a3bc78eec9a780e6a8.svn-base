package se.unlogic.hierarchy.core.utils.extensionlinks;

import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class ExtensionLink extends GeneratedElementable implements Named {

	@XMLElement
	private final String name;

	@XMLElement
	private final String url;

	@XMLElement
	private final String icon;

	@XMLElement
	private final String slot;

	public ExtensionLink(String name, String url, String icon, String slot) {

		super();
		this.name = name;
		this.url = url;
		this.icon = icon;
		this.slot = slot;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ExtensionLink other = (ExtensionLink) obj;
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

	public String getName() {

		return name;
	}

	public String getUrl() {

		return url;
	}

	public String getIcon() {

		return icon;
	}

	public String getSlot() {

		return slot;
	}
}
