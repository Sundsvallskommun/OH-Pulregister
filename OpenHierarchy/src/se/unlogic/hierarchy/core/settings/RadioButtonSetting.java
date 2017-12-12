package se.unlogic.hierarchy.core.settings;

import java.util.List;


public class RadioButtonSetting extends SingleValueMultiAlternativesSetting {

	public RadioButtonSetting(String id, String name, String description, String defaultValue, List<Alternative> alternatives) {

		this(id, name, description, defaultValue, alternatives, false);
	}
	
	public RadioButtonSetting(String id, String name, String description, String defaultValue, List<Alternative> alternatives, boolean disabled) {

		super(id, name, description, FormElement.RADIO, defaultValue, alternatives, true, disabled);
	}

}
