package se.unlogic.hierarchy.core.settings;

import se.unlogic.standardutils.validation.StringFormatValidator;


public class HTMLEditorSetting extends SingleValueNoAlternativesSetting {

	public HTMLEditorSetting(String id, String name, String description, String defaultValue, boolean required) {

		this(id, name, description, defaultValue, required, false);
	}	
	
	public HTMLEditorSetting(String id, String name, String description, String defaultValue, boolean required, StringFormatValidator formatValidator) {

		this(id, name, description, defaultValue, required, false, formatValidator);
	}
	
	public HTMLEditorSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled) {

		super(id, name, description, FormElement.HTML_EDITOR, defaultValue, required, disabled, null);
	}	
	
	public HTMLEditorSetting(String id, String name, String description, String defaultValue, boolean required, boolean disabled, StringFormatValidator formatValidator) {

		super(id, name, description, FormElement.HTML_EDITOR, defaultValue, required, disabled, formatValidator);
	}

}
