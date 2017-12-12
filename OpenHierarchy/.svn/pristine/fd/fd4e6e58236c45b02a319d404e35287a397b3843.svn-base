package se.unlogic.hierarchy.foregroundmodules.invitation.beans;

import se.unlogic.hierarchy.core.beans.SimpleAttribute;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;

@Table(name = "user_invitation_attributes")
public class SimpleInvitationAttribute extends SimpleAttribute {

	private static final long serialVersionUID = 4795024906429229449L;

	@DAOManaged(columnName = "invitationID")
	@Key
	@ManyToOne
	protected SimpleInvitation invitation;

	public SimpleInvitationAttribute() {

	}

	public SimpleInvitationAttribute(String name, String value) {

		super(name, value);
	}

	public SimpleInvitation getInvitation() {

		return invitation;
	}

	public void setInvitation(SimpleInvitation invitation) {

		this.invitation = invitation;
	}

}