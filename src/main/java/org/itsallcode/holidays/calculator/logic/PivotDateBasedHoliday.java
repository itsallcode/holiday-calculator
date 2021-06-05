/**
 * holiday-calculator
 * Copyright (C) 2021 itsallcode <github@kuhnke.net>
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
package org.itsallcode.holidays.calculator.logic;

import javax.annotation.processing.Generated;

public abstract class PivotDateBasedHoliday extends Holiday {

	protected final String pivotDateName;
	protected final int offsetInDays;

	protected PivotDateBasedHoliday(String pivotDateName, String category, String name, int offsetInDays) {
		super(category, name);
		this.offsetInDays = offsetInDays;
		this.pivotDateName = pivotDateName;
	}

	@Override
	public String toString() {
		return String.format("%s(%s %s: %d day%s %s %s)",
				this.getClass().getSimpleName(), getCategory(),
				getName(), Math.abs(offsetInDays),
				(Math.abs(offsetInDays) == 1 ? "" : "s"),
				(offsetInDays < 0 ? "before" : "after"),
				pivotDateName);
	}

	@Override
	@Generated("Eclipse IDE")
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + offsetInDays;
		result = prime * result + ((pivotDateName == null) ? 0 : pivotDateName.hashCode());
		return result;
	}

	@Override
	@Generated("Eclipse IDE")
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
		final PivotDateBasedHoliday other = (PivotDateBasedHoliday) obj;
		if (offsetInDays != other.offsetInDays) {
			return false;
		}
		if (pivotDateName == null) {
			if (other.pivotDateName != null) {
				return false;
			}
		} else if (!pivotDateName.equals(other.pivotDateName)) {
			return false;
		}
		return true;
	}
}
