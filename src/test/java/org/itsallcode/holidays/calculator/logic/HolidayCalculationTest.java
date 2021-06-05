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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.itsallcode.holidays.calculator.logic.FloatingHoliday.Direction;
import org.itsallcode.holidays.calculator.logic.parser.AbbreviationParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HolidayCalculationTest {

	AbbreviationParser<DayOfWeek> dayOfWeekParser = new AbbreviationParser<>(DayOfWeek.class);

	@Test
	void invalidDate() {
		assertThrows(java.time.DateTimeException.class,
				() -> new FloatingHoliday("holiday", "Famous Februar, 30th",
						1, DayOfWeek.MONDAY, Direction.BEFORE, 2, 30));
		assertThrows(java.time.DateTimeException.class,
				() -> new FixedDateHoliday("holiday", "Famous Februar, 30th", 2, 30));
	}

	@ParameterizedTest(name = "{0} {1} {2} {3}-{4}-{5} returns {6}")
	@CsvSource(value = {
			"1, SAT, after,  2021, 05,        1, 2021-05-01",
			"6, SAT, after,  2021, 05,        1, 2021-06-05",
			"1, MON, before, 2021, 05, last-day, 2021-05-31",
			"6, MON, before, 2021, 05, last-day, 2021-04-26"
	})
	void floatingHoliday(int offset, String dayOfWeek, String direction, int year, int month, String dayString,
			LocalDate expected) {
		final String name = String.format("%d %s %s %s-%02d-%s",
				offset, dayOfWeek, direction, year, month, dayString);
		int day;
		if (dayString.equals("last-day")) {
			day = FloatingHoliday.LAST_DAY_OF_THE_MONTH;
		} else {
			day = Integer.parseInt(dayString);
		}
		final FloatingHoliday holiday = new FloatingHoliday(
				"holiday", name, offset,
				dayOfWeekParser.getEnumFor(dayOfWeek),
				Direction.parse(direction),
				month, day);
		assertThat(holiday.of(year)).isEqualTo(expected);
	}
}
