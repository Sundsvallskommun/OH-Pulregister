package se.unlogic.hierarchy.core.settings;

import java.util.List;


public class MultiDropDownSetting extends MultiValueSetting {

	public MultiDropDownSetting(String id, String name, String description, FormElement formElement, List<Alternative> alternatives, List<String> defaultValues, boolean required) {

		this(id, name, description, formElement, alternatives, defaultValues, required, false);
	}
	
	public MultiDropDownSetting(String id, String name, String description, FormElement formElement, List<Alternative> alternatives, List<String> defaultValues, boolean required, boolean disabled) {

		super(id, name, description, FormElement.CHECK, alternatives, defaultValues, required, disabled);
	}

}
