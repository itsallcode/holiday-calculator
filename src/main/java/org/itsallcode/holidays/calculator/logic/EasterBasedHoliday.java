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

import java.time.LocalDate;

public class EasterBasedHoliday extends Holiday {
	private final int offsetInDays;

	public EasterBasedHoliday(String category, String name, int offsetInDays) {
		super(category, name);
		this.offsetInDays = offsetInDays;
	}

	@Override
	public LocalDate of(int year) {
		return GaussEasterCalculator.calculate(year).plusDays(offsetInDays);
	}

	@Override
	public String toString() {
		return String.format("%s(%s %s: %d day%s %s easter)",
				this.getClass().getSimpleName(), getCategory(),
				getName(), Math.abs(offsetInDays),
				(Math.abs(offsetInDays) == 1 ? "" : "s"), (offsetInDays < 0 ? "before" : "after"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + offsetInDays;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EasterBasedHoliday other = (EasterBasedHoliday) obj;
		if (offsetInDays != other.offsetInDays)
			return false;
		return true;
	}
}
