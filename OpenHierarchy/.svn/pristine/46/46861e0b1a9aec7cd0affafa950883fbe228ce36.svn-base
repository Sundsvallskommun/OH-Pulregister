package se.unlogic.hierarchy.core.settings;

import java.util.List;

import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;

abstract class MultiValueSetting extends Setting {

	private final List<String> defaultValues;

	private final List<Alternative> alternatives;

	public MultiValueSetting(String id, String name, String description, FormElement formElement, List<Alternative> alternatives, List<String> defaultValues, boolean required) {

		this(id, name, description, formElement, alternatives, defaultValues, required, false);
	}
	
	public MultiValueSetting(String id, String name, String description, FormElement formElement, List<Alternative> alternatives, List<String> defaultValues, boolean required, boolean disabled) {

		super(id, name, description, formElement, required, disabled);

		if (CollectionUtils.isEmpty(alternatives)) {

			throw new RuntimeException("alternatives cannot be empty");
		}

		this.alternatives = alternatives;
		this.defaultValues = defaultValues;
	}

	@Override
	public List<String> getDefaultValues() {

		return defaultValues;
	}

	@Override
	protected List<Alternative> getAlternatives() {

		return alternatives;
	}

	@Override
	public List<String> parseAndValidate(List<String> values, List<ValidationError> validationErrors) {

		boolean errors = false;

		outer: for (String value : values) {

			for (Alternative alternative : alternatives) {

				if (alternative.getValue().equals(value)) {

					continue outer;
				}
			}

			validationErrors.add(new ValidationError("setting-" + getId(), getName(), ValidationErrorType.InvalidFormat));
			errors = true;
		}

		if (errors) {

			return null;
		}

		return values;
	}
}
