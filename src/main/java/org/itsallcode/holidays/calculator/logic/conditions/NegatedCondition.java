/**
 * holiday-calculator
 * Copyright (C) 2022 itsallcode <github@kuhnke.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.itsallcode.holidays.calculator.logic.conditions;

import java.time.Year;
import java.util.Objects;

/**
 * Negated variant of the initial condition.
 *
 * Whenever the initial condition is {@code true} the negated condition will
 * {@code false} and vice-versa.
 */
public class NegatedCondition extends Condition {

	private final Condition other;

	/**
	 * Create a condition negating the initial condition in parameter {@code other}.
	 *
	 * Whenever the initial condition is {@code true} the negated condition will
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
