package org.itsallcode.holidays.calculator.logic.conditions;

import java.time.Year;

public class NegatedCondition extends Condition {

	private final Condition other;

	public NegatedCondition(Condition other) {
		super();
		this.other = other;
	}

	@Override
	public boolean applies(Year year) {
		return !other.applies(year);
	}

	@Override
	public String toString(String prefix) {
		return other.toString(prefix, true);
	}

	@Override
	public String toString(String prefix, boolean negated) {
		return other.toString(prefix, !negated);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((other == null) ? 0 : other.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NegatedCondition other = (NegatedCondition) obj;
		if (this.other == null) {
			if (other.other != null) {
				return false;
			}
		}
		return (this.other.equals(other.other));
	}

}
