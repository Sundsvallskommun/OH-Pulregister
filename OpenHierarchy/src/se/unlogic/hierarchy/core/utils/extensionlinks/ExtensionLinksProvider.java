package se.unlogic.hierarchy.core.utils.extensionlinks;

import java.util.Collection;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;


public interface ExtensionLinksProvider {

	/**
	 * @return null or a {@link AccessInterface}
	 */
	public AccessInterface getAccessInterface();
	
	/**
	 * @param user
	 * @return
	 *  null or an {@link ExtensionLink}
	 */
	public Collection<ExtensionLink> getExtensionLinks(User user);
}
