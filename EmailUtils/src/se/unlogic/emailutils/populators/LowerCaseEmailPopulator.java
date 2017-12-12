package se.unlogic.emailutils.populators;

import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.standardutils.populators.BaseStringPopulator;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.validation.StringFormatValidator;


public class LowerCaseEmailPopulator  extends BaseStringPopulator<String> implements BeanStringPopulator<String>{
	
	private static final LowerCaseEmailPopulator POPULATOR = new LowerCaseEmailPopulator();

	public static LowerCaseEmailPopulator getPopulator(){
		return POPULATOR;
	}

	public LowerCaseEmailPopulator() {
		super("email");
	}

	public LowerCaseEmailPopulator(String populatorID) {
		super(populatorID);
	}

	public LowerCaseEmailPopulator(String populatorID, StringFormatValidator formatValidator) {
		super(populatorID, formatValidator);
	}

	public String getValue(String value) {
		return value.toLowerCase();
	}

	@Override
	public boolean validateDefaultFormat(String value) {
		return EmailUtils.isValidEmailAddress(value);
	}

	public Class<? extends String> getType() {
		return String.class;
	}
}
