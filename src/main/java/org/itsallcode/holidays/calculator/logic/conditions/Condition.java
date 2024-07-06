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

import java.time.MonthDay;
import java.time.Year;

/**
 * This class represents a condition that applies to a specific holiday.
 */
public abstract class Condition {

	/**
	 * Constant condition that is always {@code true}.
	 */
	public static final Condition APPLIES_ALWAYS = new ConstantCondition(true);

	/**
	 * Create a new condition that negates the specified condition.
	 *
	 * @param other other condition to be negated
	 * @return new condition
	 */
	public static final Condition not(Condition other) {
		return new NegatedCondition(other);
	}

	/**
	 * Check if the current condition applies in the specified year.
	 *
	 * @param year year to evaluate the current condition for.
	 * @return {@code true} if the condition applies, {@code false} otherwise.
	 */
	public abstract boolean applies(Year year);

	/**
	 * Return a string representation of the current condition using the specified
	 * prefix and a flag indicating whether the condition should be negated.
	 *
	 * @param prefix  prefix to prepend to the normal string representation
	 * @param negated flag indicating whether the condition should be negated
	 * @return string representation using the specified prefix and the flag
	 *         {@code negated}
	 */
	public abstract String toString(String prefix, boolean negated);

	/**
	 * Default constructor
	 */
	public Condition() {
	}

	/**
	 * Add a pivot date to the current Condition
	 *
	 * @param pivot pivot date
	 * @return self for fluent programming
	 */
	public Condition withPivotDate(MonthDay pivot) {
		return this;
	}

	@Override
	public String toString() {
		return toString("");
	}

	/**
	 * Return a string representation of the current condition using the specified
	 * prefix.
	 *
	 * @param prefix prefix to prepend to the normal string representation.
	 * @return string representation of the current condition using the specified
	 *         prefix.
	 */
	public String toString(String prefix) {
		return toString(prefix, false);
	}

}
