package org.itsallcode.holidays.calculator.logic.conditions;

import java.time.Year;

public class ConstantCondition extends Condition {

	private final boolean value;

	public ConstantCondition(boolean value) {
		super(false);
		this.value = value;
	}

	@Override
	public boolean applies(Year year) {
		return value;
	}

	@Override
	protected ConstantCondition copy() {
		final ConstantCondition result = new ConstantCondition(value);
		result.negate = negate;
		return result;
	}

	@Override
	public String toString() {
		return (value ? "" : " never");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (value ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ConstantCondition other = (ConstantCondition) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}

}
