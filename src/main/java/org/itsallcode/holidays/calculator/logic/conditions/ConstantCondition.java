package org.itsallcode.holidays.calculator.logic.conditions;

import java.time.Year;
import java.util.Objects;

/**
 * This class represents a constant condition that is always {@code true}
 * or{@code false}.
 */
public class ConstantCondition extends Condition {

	private final boolean value;

	/**
	 * Create a new constant condition
	 *
	 * @param value value for the constant condition
	 */
	public ConstantCondition(final boolean value) {
		this.value = value;
	}

	@Override
	public boolean applies(final Year year) {
		return value;
	}

	/**
	 * Get the value of the constant condition.
	 *
	 * @return the value of the constant condition.
	 */
	public boolean isValue() {
		return value;
	}

	@Override
	public String toString(final String prefix, final boolean negated) {
		return ((negated ? !value : value) ? "" : "never");
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ConstantCondition other = (ConstantCondition) obj;
		return value == other.value;
	}

}
