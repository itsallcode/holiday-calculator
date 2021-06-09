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

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.regex.Matcher;

import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Day;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Direction;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

public class FloatingDateMatcher extends HolidayMatcher {
	public FloatingDateMatcher() {
		super(Patterns.FLOATING_HOLIDAY);
	}

	@Override
	Holiday createHoliday(Matcher matcher) {
		final DayOfWeek dayOfWeek = dayOfWeek(matcher.group(Patterns.DAY_OF_WEEK_GROUP));
		if (dayOfWeek == null) {
			return null;
		}

		final String category = matcher.group(Patterns.CATEGORY_GROUP);
		final String name = matcher.group(Patterns.NAME_GROUP);
		final int offset = Integer.parseInt(matcher.group(Patterns.OFFSET_GROUP));
		final Direction direction = Direction.parse(matcher.group(Patterns.DIRECTION_GROUP));
		final int month = monthNumber(matcher.group(Patterns.MONTH_GROUP));
		final String day = matcher.group(Patterns.DAY_GROUP);

		if (Patterns.LAST_DAY.equals(day)) {
			return new FloatingHoliday(category, name, offset, dayOfWeek, direction, month, Day.LAST);
		} else {
			return new FloatingHoliday(category, name, offset, dayOfWeek, direction,
					MonthDay.of(month, Integer.parseInt(day)));
		}
	}
}