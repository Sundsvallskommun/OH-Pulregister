package se.unlogic.hierarchy.foregroundmodules.lucenesearch.beans;

import java.util.ArrayList;
import java.util.List;

public class SearchCriterias {

	private List<ObjectCriteria> equalCriterias;
	private List<ObjectCriteria> notEqualCriterias;
	private List<NumberCriteria> smallerThanCriterias;
	private List<NumberCriteria> largerThanCriterias;
	private List<RangeCriteria> rangeCriterias;

	public List<ObjectCriteria> getEqualCriterias() {

		return equalCriterias;
	}

	public List<ObjectCriteria> getNotEqualCriterias() {

		return notEqualCriterias;
	}

	public List<NumberCriteria> getSmallerThanCriterias() {

		return smallerThanCriterias;
	}

	public List<NumberCriteria> getLargerThanCriterias() {

		return largerThanCriterias;
	}

	public List<RangeCriteria> getRangeCriterias() {

		return rangeCriterias;
	}

	public void addEqualsCriteria(String attributeName, Object value, boolean required) {

		if (equalCriterias == null) {

			equalCriterias = new ArrayList<ObjectCriteria>();
		}

		equalCriterias.add(new ObjectCriteria(attributeName, value));
	}

	public void addNotEqualsCriteria(String attributeName, Object value, boolean required) {

		if (notEqualCriterias == null) {

			notEqualCriterias = new ArrayList<ObjectCriteria>();
		}

		notEqualCriterias.add(new ObjectCriteria(attributeName, value));
	}

	public void addSmallerThanCriteria(String attributeName, Number value, boolean required) {

		if (smallerThanCriterias == null) {

			smallerThanCriterias = new ArrayList<NumberCriteria>();
		}

		smallerThanCriterias.add(new NumberCriteria(attributeName, value));
	}

	public void addLargerThanCriteria(String attributeName, Number value, boolean required) {

		if (largerThanCriterias == null) {

			largerThanCriterias = new ArrayList<NumberCriteria>();
		}

		largerThanCriterias.add(new NumberCriteria(attributeName, value));
	}

	// Inclusive
	public void addInRangeCriteria(String attributeName, Number lowerEnd, Number upperEnd, boolean required) {

		if (rangeCriterias == null) {

			rangeCriterias = new ArrayList<RangeCriteria>();
		}

		rangeCriterias.add(new RangeCriteria(attributeName, lowerEnd, upperEnd));
	}

	public void addEqualsCriteria(String attributeName, Object value) {

		addEqualsCriteria(attributeName, value, true);
	}

	public void addNotEqualsCriteria(String attributeName, Object value) {

		addNotEqualsCriteria(attributeName, value, true);
	}

	public void addSmallerThanCriteria(String attributeName, Number value) {

		addSmallerThanCriteria(attributeName, value, true);
	}

	public void addLargerThanCriteria(String attributeName, Number value) {

		addLargerThanCriteria(attributeName, value, true);
	}

	// Inclusive
	public void addInRangeCriteria(String attributeName, Number lowerEnd, Number upperEnd) {

		addInRangeCriteria(attributeName, lowerEnd, upperEnd, true);
	}

}
