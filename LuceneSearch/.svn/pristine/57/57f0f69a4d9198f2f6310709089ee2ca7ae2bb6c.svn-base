package se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans;

public class RangeCriteria extends Criteria {

	private final Number parameterLow;
	private final Number parameterHigh;

	public RangeCriteria(String attributeName, Number parameterLow, Number parameterHigh, boolean required) {

		super(attributeName, required);
		
		if(parameterLow == null || parameterHigh == null){
			throw new NullPointerException();
		}
		
		this.parameterLow = parameterLow;
		this.parameterHigh = parameterHigh;
	}

	public RangeCriteria(String attributeName, Number parameterLow, Number parameterHigh) {

		this(attributeName, parameterLow, parameterHigh, true);
	}

	public Number getParameterLow() {

		return parameterLow;
	}

	public Number getparameterHigh() {

		return parameterHigh;
	}

}
