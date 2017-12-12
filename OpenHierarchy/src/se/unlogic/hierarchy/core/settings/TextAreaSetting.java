package se.unlogic.hierarchy.core.settings;

import se.unlogic.standardutils.validation.StringFormatValidator;


public class TextAreaSetting extends SingleValueNoAlternativesSetting {

	public TextAreaSetting(String id, String name, String description, String defaultValue, boolean required) {

		this(id, name, description, defaultValue, required, false);
	}	
	
	public TextAreaSetting(String id, String name, String description, String defaultValue, boolean required, StringFormatValidator formatValidator) {

		this(id, name, description, defaultValue, required, false, formatValidator);
	}
	
	public TextAreaSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled) {

		super(id, name, description, FormElement.TEXT_AREA, defaultValue, required, disabled, null);
	}	
	
	public TextAreaSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled, StringFormatValidator formatValidator) {

		super(id, name, description, FormElement.TEXT_AREA, defaultValue, required, disabled, formatValidator);
	}

}
