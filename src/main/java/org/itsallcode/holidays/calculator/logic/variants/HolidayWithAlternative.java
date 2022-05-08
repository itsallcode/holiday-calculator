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

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;

public class HolidayWithAlternative extends Holiday {

	private final Holiday defaultHoliday;
	private final Condition condition;
	private final Holiday alternative;

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
}
