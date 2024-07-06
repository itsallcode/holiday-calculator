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

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;

/**
 * Represents a holiday with a default date and an alternative date that applies
 * if the specified condition is {@code true} for a particular year.
 */
public class HolidayWithAlternative extends Holiday {

	private final Holiday defaultHoliday;
	private final Condition condition;
	private final Holiday alternative;

	/**
	 * Create a new instance of an alternative holiday.
	 *
	 * @param defaultHoliday   Default holiday if {@code condition} is {@code false}
	 *                         for a particular year.
	 * @param conditionBuilder Builder for the condition in a given year.
	 * @param alternateDate    Alternative date that applies if the specified
	 *                         {@code condition} is {@code true} for a particular
	 *                         year.
	 */
	public HolidayWithAlternative(Holiday defaultHoliday, ConditionBuilder conditionBuilder, MonthDay alternateDate) {
		super(defaultHoliday.getCategory(), defaultHoliday.getName());
		this.defaultHoliday = defaultHoliday;
		this.condition = conditionBuilder.withOptionalPivotDateFrom(defaultHoliday).build();
		this.alternative = new FixedDateHoliday(defaultHoliday, alternateDate);
	}

	@Override
	public LocalDate of(int year) {
		if (condition.applies(Year.of(year))) {
			return alternative.of(year);
		} else {
			return defaultHoliday.of(year);
		}
	}

	@Override
	public String toString() {
		return defaultHoliday.toString(""
				+ " or " + condition.toString()
				+ " then " + alternative.toString(defaultHoliday));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(alternative, condition, defaultHoliday);
		return result;
	}

	@Override
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
		final HolidayWithAlternative other = (HolidayWithAlternative) obj;
		return Objects.equals(alternative, other.alternative) && Objects.equals(condition, other.condition)
				&& Objects.equals(defaultHoliday, other.defaultHoliday);
	}
}
