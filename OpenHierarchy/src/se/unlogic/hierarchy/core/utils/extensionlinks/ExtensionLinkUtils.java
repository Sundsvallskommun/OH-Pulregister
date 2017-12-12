package se.unlogic.hierarchy.core.utils.extensionlinks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.NameComparator;
import se.unlogic.standardutils.xml.XMLUtils;

public class ExtensionLinkUtils {

	protected static Logger LOG = Logger.getLogger(ExtensionLinkUtils.class);

	public static void appendExtensionLinks(Collection<ExtensionLinkProvider> linkProviders, User user, HttpServletRequest req, Document doc, Element targetElement) {

		appendExtensionLinks(linkProviders, user, req, doc, targetElement, NameComparator.getInstance());
	}

	public static void appendExtensionLinks(Collection<ExtensionLinkProvider> linkProviders, Collection<ExtensionLinksProvider> linksProviders, User user, HttpServletRequest req, Document doc, Element targetElement) {

		appendExtensionLinks(linkProviders, linksProviders, user, req, doc, targetElement, NameComparator.getInstance());
	}

	public static void appendExtensionLinks(Collection<ExtensionLinkProvider> linkProviders, User user, HttpServletRequest req, Document doc, Element targetElement, Comparator<? super ExtensionLink> comparator) {

		appendExtensionLinks(linkProviders, null, user, req, doc, targetElement, comparator);
	}

	public static void appendExtensionLinks(Collection<ExtensionLinkProvider> linkProviders, Collection<ExtensionLinksProvider> linksProviders, User user, HttpServletRequest req, Document doc, Element targetElement, Comparator<? super ExtensionLink> comparator) {

		if (CollectionUtils.isEmpty(linkProviders) && CollectionUtils.isEmpty(linksProviders)) {

			return;
		}

		List<ExtensionLink> extensionLinks = new ArrayList<ExtensionLink>(linkProviders.size());

		if (!CollectionUtils.isEmpty(linkProviders)) {
			for (ExtensionLinkProvider linkProvider : linkProviders) {

				if (linkProvider.getAccessInterface() == null || AccessUtils.checkAccess(user, linkProvider.getAccessInterface())) {

					ExtensionLink link;

					try {
						link = linkProvider.getExtensionLink(user);

					} catch (Exception e) {

						LOG.error("Error getting extension link from provider " + linkProvider, e);

						return;
					}

					if (link != null) {

						extensionLinks.add(link);
					}
				}
			}
		}

		if (!CollectionUtils.isEmpty(linksProviders)) {
			for (ExtensionLinksProvider linksProvider : linksProviders) {

				if (linksProvider.getAccessInterface() == null || AccessUtils.checkAccess(user, linksProvider.getAccessInterface())) {

					Collection<ExtensionLink> links;

					try {
						links = linksProvider.getExtensionLinks(user);

					} catch (Exception e) {

						LOG.error("Error getting extension link from provider " + linksProvider, e);

						return;
					}

					if (links != null) {

						extensionLinks.addAll(links);
					}
				}
			}
		}

		if (extensionLinks.size() > 1 && comparator != null) {

			Collections.sort(extensionLinks, comparator);
		}

		XMLUtils.append(doc, targetElement, extensionLinks);
	}
}
