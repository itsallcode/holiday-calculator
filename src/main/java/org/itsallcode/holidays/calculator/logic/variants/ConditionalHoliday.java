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
import java.time.Year;

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;

public class ConditionalHoliday extends Holiday {

	private final Condition condition;
	private final Holiday other;

	public ConditionalHoliday(ConditionBuilder conditionBuilder, Holiday holiday) {
		super(holiday.getCategory(), holiday.getName());
		this.condition = conditionBuilder.build();
		this.other = holiday;
	}

	@Override
	public LocalDate of(int year) {
		if (!condition.applies(Year.of(year))) {
			return null;
		}
		return other.of(year);
	}

	@Override
	public String toString() {
		return other.toString(condition.toString(" only "));
	}

}
