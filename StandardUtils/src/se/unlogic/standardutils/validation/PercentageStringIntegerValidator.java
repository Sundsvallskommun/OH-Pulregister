package se.unlogic.standardutils.validation;

import se.unlogic.standardutils.validation.StringIntegerValidator;

public class PercentageStringIntegerValidator extends StringIntegerValidator {

	private static final long serialVersionUID = -3200447360287855765L;

	public PercentageStringIntegerValidator() {

		super(0, 100);
	}	
}
