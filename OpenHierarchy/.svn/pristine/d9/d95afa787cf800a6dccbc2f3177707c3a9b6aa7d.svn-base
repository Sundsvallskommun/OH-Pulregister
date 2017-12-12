package se.unlogic.hierarchy.core.settings;

import se.unlogic.standardutils.validation.StringFormatValidator;


public class PasswordFieldSetting extends SingleValueNoAlternativesSetting {

	public PasswordFieldSetting(String id, String name, String description, String defaultValue, boolean required) {

		this(id, name, description, defaultValue, required, false);
	}	
	
	public PasswordFieldSetting(String id, String name, String description, String defaultValue, boolean required, StringFormatValidator formatValidator) {

		this(id, name, description, defaultValue, required, false, formatValidator);
	}
	
	public PasswordFieldSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled) {

		super(id, name, description, FormElement.PASSWORD, defaultValue, required, disabled, null);
	}	
	
	public PasswordFieldSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled, StringFormatValidator formatValidator) {

		super(id, name, description, FormElement.PASSWORD, defaultValue, required, disabled, formatValidator);
	}

}
