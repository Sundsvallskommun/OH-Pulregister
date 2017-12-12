package se.unlogic.hierarchy.core.settings;

import java.util.Collections;
import java.util.List;

import se.unlogic.standardutils.validation.ValidationError;





public class SingleCheckBoxSetting extends SingleValueNoAlternativesSetting {

	public SingleCheckBoxSetting(String id, String name, String description, boolean defaultValue) {

		this(id, name, description, defaultValue, false);
	}
	
	public SingleCheckBoxSetting(String id, String name, String description, boolean defaultValue, boolean disabled) {

		super(id, name, description, FormElement.CHECK, Boolean.toString(true), false, disabled, null);
	}

	@Override
	public boolean validateWithoutValues() {

		return true;
	}

	@Override
	public List<String> parseAndValidate(List<String> values, List<ValidationError> validationErrors) {

		if(values == null){

			return Collections.singletonList("false");
		}

		return Collections.singletonList(Boolean.toString(Boolean.parseBoolean(values.get(0))));
	}
}
