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

public class FixedDateHoliday extends Holiday {
	private final int month;
	private final int day;

	public FixedDateHoliday(String category, String name, int month, int day) {
		super(category, name);
		this.month = month;
		this.day = day;
		ensureValidDate(month, day);
	}

	@Override
	public LocalDate of(int year) {
		return LocalDate.of(year, month, day);
	}

	@Override
	public String toString() {
		return String.format("%s(%s %s: %02d-%02d)", this.getClass().getSimpleName(),
				getCategory(), getName(), month, day);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + day;
		result = prime * result + month;
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
		final FixedDateHoliday other = (FixedDateHoliday) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		return true;
	}

}
