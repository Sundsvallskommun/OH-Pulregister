package se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans;

public class ObjectCriteria extends Criteria {

	private final Object parameter;

	public ObjectCriteria(String attributeName, Object parameter, boolean required) {

		super(attributeName, required);
		
		if(parameter == null){
			throw new NullPointerException();
		}
		
		this.parameter = parameter;
	}

	public ObjectCriteria(String attributeName, Object parameter) {

		this(attributeName, parameter, true);
	}

	public Object getParameter() {

		return parameter;
	}

}
