/*******************************************************************************
 * Copyright (c) 2010 Robert "Unlogic" Olofsson (unlogic@unlogic.se).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 ******************************************************************************/
package se.unlogic.hierarchy.foregroundmodules.invitation.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.hierarchy.core.handlers.SourceAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.AttributeSource;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "user_invitations")
@XMLElement(name = "Invitation")
public class SimpleInvitation extends BaseInvitation implements AttributeSource {

	public static final Field ATTRIBUTES_RELATION = ReflectionUtils.getField(SimpleInvitation.class, "attributes");
	
	@DAOManaged(columnName = "invitationTypeID")
	@ManyToOne(autoGet = true)
	@XMLElement
	private SimpleInvitationType simpleInvitationType;

	@DAOManaged
	@OneToMany
	private List<SimpleInvitationAttribute> attributes;

	private SourceAttributeHandler attributeHandler;

	@SuppressWarnings("unchecked")
	@Override
	public SimpleInvitationType getInvitationType() {

		return simpleInvitationType;
	}

	public void setInvitationType(SimpleInvitationType simpleInvitationType) {

		this.simpleInvitationType = simpleInvitationType;
	}

	@Override
	public MutableAttributeHandler getAttributeHandler() {

		if (attributeHandler == null) {

			attributeHandler = new SourceAttributeHandler(this, 255, 4096);
		}

		return attributeHandler;
	}

	@Override
	public List<SimpleInvitationAttribute> getAttributes() {

		return attributes;
	}

	public void setAttributes(List<SimpleInvitationAttribute> attributes) {

		this.attributes = attributes;
	}

	@Override
	public void addAttribute(String name, String value) {

		if (this.attributes == null) {

			attributes = new ArrayList<SimpleInvitationAttribute>();
		}

		attributes.add(new SimpleInvitationAttribute(name, value));
	}

}
