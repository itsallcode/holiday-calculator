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
package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.Objects;

public abstract class Holiday {

	public abstract LocalDate of(int year);

	private final String category;
	private final String name;
	protected int offsetInDays = 0;

	/**
	 * @param category Arbitrary category that may be evaluated by the application
	 *                 processing the holiday.
	 * @param name     Name of holiday.
	 */
	protected Holiday(String category, String name) {
		this.category = category;
		this.name = name;
	}

	public MonthDay getMonthDay() {
		return null;
	}

	public Holiday withOffsetInDays(int offsetInDays) {
		this.offsetInDays = offsetInDays;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public LocalDate of(Year year) {
		return of(year.getValue());
	}

	@Override
	public String toString() {
		return toString("");
	}

	/**
	 * @param pivot pivot holiday, for which the current holiday is an alternative
	 * @return string representation of current holiday as alternative to pivot
	 *         holiday
	 */
	protected String toString(Holiday pivot) {
		return "";
	}

	protected String toString(String condition) {
		return "";
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, name, offsetInDays);
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
		final Holiday other = (Holiday) obj;
		return Objects.equals(category, other.category) && Objects.equals(name, other.name)
				&& offsetInDays == other.offsetInDays;
	}

}
