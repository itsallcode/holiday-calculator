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

public class NegatedCondition extends Condition {

	private final Condition other;

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
