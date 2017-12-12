package se.unlogic.hierarchy.core.settings;

import java.util.Collections;
import java.util.List;

import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;


class SingleValueMultiAlternativesSetting extends SingleValueSetting {

	private final List<Alternative> alternatives;
	
	public SingleValueMultiAlternativesSetting(String id, String name, String description, FormElement formElement, String defaultValue, List<Alternative> alternatives, boolean required) {

		this(id, name, description, formElement, defaultValue, alternatives, required, false);
	}
	
	public SingleValueMultiAlternativesSetting(String id, String name, String description, FormElement formElement, String defaultValue, List<Alternative> alternatives, boolean required, boolean disabled) {

		super(id, name, description, formElement, defaultValue, required, disabled);
		
		if(CollectionUtils.isEmpty(alternatives)){
			
			throw new RuntimeException("alternatives cannot be empty");
		}
		
		this.alternatives = alternatives;
		
		if(defaultValue != null){
			
			boolean matchingAlernativeFound = false;
			
			for(Alternative alternative : alternatives){
				
				if(alternative.getValue().equals(defaultValue)){
					
					matchingAlernativeFound = true;
					break;
				}
			}
			
			if(!matchingAlernativeFound){
			
				throw new RuntimeException("Unable to find default value " + defaultValue + " in list of alternatives");
			}			
		}
	}

	@Override
	protected List<Alternative> getAlternatives() {

		return alternatives;
	}

	@Override
	public List<String> parseAndValidate(List<String> values, List<ValidationError> validationErrors) {

		String value = values.get(0);
		
		for(Alternative alternative : alternatives){
			
			if(alternative.getValue().equals(value)){
				
				return Collections.singletonList(value);
			}
		}
		
		validationErrors.add(new ValidationError("setting-" + getId(), getName(), ValidationErrorType.InvalidFormat));
		return null;
	}
}
