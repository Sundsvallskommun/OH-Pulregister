package se.unlogic.hierarchy.core.settings;

import java.util.Collections;
import java.util.List;

import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;


class SingleValueNoAlternativesSetting extends SingleValueSetting {

	protected final StringFormatValidator formatValidator;
	
	public SingleValueNoAlternativesSetting(String id, String name, String description, FormElement formElement, String defaultValue, boolean required, StringFormatValidator formatValidator) {

		this(id, name, description, formElement, defaultValue, required, false, formatValidator);
	}
	
	public SingleValueNoAlternativesSetting(String id, String name, String description, FormElement formElement, String defaultValue, boolean required, boolean disabled, StringFormatValidator formatValidator) {

		super(id, name, description, formElement, defaultValue, required, disabled);
		this.formatValidator = formatValidator;
	}

	@Override
	protected final List<Alternative> getAlternatives() {

		return null;
	}

	@Override
	public List<String> parseAndValidate(List<String> values, List<ValidationError> validationErrors) {

		String value = values.get(0);
		
		if(formatValidator != null){
			
			if(!formatValidator.validateFormat(value)){
				
				validationErrors.add(new ValidationError("setting-" + getId(), getName(), ValidationErrorType.InvalidFormat));
				return null;
			}
		}
		
		return Collections.singletonList(value);
	}
}
