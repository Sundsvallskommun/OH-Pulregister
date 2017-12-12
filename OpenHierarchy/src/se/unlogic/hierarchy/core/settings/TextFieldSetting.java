package se.unlogic.hierarchy.core.settings;

import se.unlogic.standardutils.validation.StringFormatValidator;


public class TextFieldSetting extends SingleValueNoAlternativesSetting {

	public TextFieldSetting(String id, String name, String description, String defaultValue, boolean required) {

		this(id, name, description, defaultValue, required, false);
	}	
	
	public TextFieldSetting(String id, String name, String description, String defaultValue, boolean required, StringFormatValidator formatValidator) {

		this(id, name, description, defaultValue, required, false, formatValidator);
	}
	
	public TextFieldSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled) {

		super(id, name, description, FormElement.TEXT_FIELD, defaultValue, required, disabled, null);
	}	
	
	public TextFieldSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled, StringFormatValidator formatValidator) {

		super(id, name, description, FormElement.TEXT_FIELD, defaultValue, required, disabled, formatValidator);
	}

}
