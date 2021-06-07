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

import org.itsallcode.holidays.calculator.logic.conditions.Condition;

public abstract class Holiday {
	private static final int PIVOT_YEAR = 2000;

	public abstract LocalDate of(int year);

	private final String category;
	private final String name;
	protected Condition condition = Condition.APPLIES_ALWAYS;

	/**
	 * @param category Arbitrary category that may be evaluated by the application
	 *                 processing the holiday.
	 * @param name     Name of holiday.
	 */
	protected Holiday(String category, String name) {
		this.category = category;
		this.name = name;
	}

	public Holiday withCondition(Condition condition) {
		this.condition = condition;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public Condition getCondition() {
		return condition;
	}

	/**
	 * Ensure date can be valid, at least in a leap year
	 *
	 * @param month Month
	 * @param day   Day
	 * @return local date if valid
	 */
	protected LocalDate ensureValidDate(int month, int day) {
		return LocalDate.of(PIVOT_YEAR, month, day);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		final Holiday other = (Holiday) obj;
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		if (condition == null) {
			if (other.condition != null) {
				return false;
			}
		} else if (!condition.equals(other.condition)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		return (name.equals(other.name));
	}

}
