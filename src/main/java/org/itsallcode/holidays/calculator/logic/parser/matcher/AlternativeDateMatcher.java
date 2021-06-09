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
package org.itsallcode.holidays.calculator.logic.parser.matcher;

import java.time.MonthDay;
import java.util.regex.Matcher;

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.DayOfWeekCondition;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

public class AlternativeDateMatcher extends HolidayMatcher {
	public AlternativeDateMatcher() {
		super(Patterns.ALTERNATIVE_DATE_HOLIDAY);
	}

	@Override
	Holiday createHoliday(Matcher matcher) {
		final Condition condition = new DayOfWeekCondition(
				daysOfWeek(matcher.group(Patterns.PIVOT_DAYS_OF_WEEK_GROUP)));
		final MonthDay alternative = monthDay(matcher.group(Patterns.MONTH_GROUP_2),
				matcher.group(Patterns.DAY_GROUP_2));
		return new FixedDateWithConditionMatcher()
				.createHoliday(matcher)
				.withAlternative(condition, alternative);
	}

	public static class NegatedDayOfWeek extends NegatedConditionMatcher {
		public NegatedDayOfWeek() {
			super(new AlternativeDateMatcher(), Patterns.ALTERNATIVE_DATE_HOLIDAY_WITH_NEGATED_DAY_OF_WEEK);
		}
	}
}