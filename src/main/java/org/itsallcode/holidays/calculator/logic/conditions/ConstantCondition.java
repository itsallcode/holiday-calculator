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
	public ConstantCondition(boolean value) {
		this.value = value;
	}

	@Override
	public boolean applies(Year year) {
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
	public String toString(String prefix, boolean negated) {
		return ((negated ? !value : value) ? "" : "never");
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
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
		final ConstantCondition other = (ConstantCondition) obj;
		return value == other.value;
	}

}
