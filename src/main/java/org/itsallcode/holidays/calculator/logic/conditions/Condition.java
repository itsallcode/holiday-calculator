package org.itsallcode.holidays.calculator.logic.conditions;

import java.time.MonthDay;
import java.time.Year;

public abstract class Condition {

	public static final Condition APPLIES_ALWAYS = new ConstantCondition(true);

	public static final Condition not(Condition other) {
		return other.copy().negate();
	}

	public abstract boolean applies(Year year);

	public abstract String toString(String prefix);

	protected abstract Condition copy();

	protected boolean negate;

	public Condition(boolean negate) {
		this.negate = negate;
	}

	public Condition negate() {
		negate = !negate;
		return this;
	}

	public Condition withPivotDate(MonthDay pivot) {
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (negate ? 1231 : 1237);
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
		final Condition other = (Condition) obj;
		if (negate != other.negate) {
			return false;
		}
		return true;
	}
}
