package se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans;

public class NumberCriteria extends Criteria {

	private final Number parameter;

	public NumberCriteria(String attributeName, Number parameter, boolean required) {

		super(attributeName, required);
		
		if(parameter == null){
			throw new NullPointerException();
		}
		
		this.parameter = parameter;
	}

	public NumberCriteria(String attributeName, Number parameter) {

		this(attributeName, parameter, true);
	}

	public Number getParameter() {

		return parameter;
	}

}
