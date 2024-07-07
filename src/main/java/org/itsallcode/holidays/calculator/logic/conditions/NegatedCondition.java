package org.itsallcode.holidays.calculator.logic.conditions;

import java.time.Year;
import java.util.Objects;

/**
 * Negated variant of the initial condition.
 *
 * Whenever the initial condition is {@code true} the negated condition will be
 * {@code false} and vice-versa.
 */
public class NegatedCondition extends Condition {

	private final Condition other;

	/**
	 * Create a condition negating the initial condition in parameter {@code other}.
	 *
	 * Whenever the initial condition is {@code true} the negated condition will be
	 * {@code false} and vice-versa.
	 *
	 * @param other condition to be negated.
	 */
	public NegatedCondition(Condition other) {
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
		return Objects.hash(other);
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
		return Objects.equals(this.other, other.other);
	}

}
