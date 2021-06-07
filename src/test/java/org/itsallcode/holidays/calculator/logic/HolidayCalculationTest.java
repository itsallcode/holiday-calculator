/**
 * holidaycalculator
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
import static org.itsallcode.holidays.calculator.logic.conditions.Condition.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.itsallcode.holidays.calculator.logic.FloatingHoliday.Day;
import org.itsallcode.holidays.calculator.logic.FloatingHoliday.Direction;
import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.DayOfWeekCondition;
import org.itsallcode.holidays.calculator.logic.parser.AbbreviationParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HolidayCalculationTest {

	AbbreviationParser<DayOfWeek> dayOfWeekParser = new AbbreviationParser<>(DayOfWeek.class);
	static HolidaySet BANK_HOLIDAYS_DECEMBER = bankHolidaysDecember();
	static HolidaySet NEGATED_HOLIDAY = negatedDaysOfWeekHoliday();

	@Test
	void toStringTest() {
		assertThat(new FixedDateHoliday("birthday", "My Birthday", MonthDay.of(7, 31)))
				.hasToString("FixedDateHoliday(birthday My Birthday: JUL 31)");
		assertThat(new FloatingHoliday(
				"holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, MonthDay.of(12, 24)))
						.hasToString("FloatingHoliday(holiday 1. Advent: 4th Sunday before DEC 24)");
		assertThat(new FloatingHoliday(
				"holiday", "Father's Day", 3, DayOfWeek.SUNDAY, Direction.AFTER, MonthDay.of(6, 1)))
						.hasToString("FloatingHoliday(holiday Father's Day: 3rd Sunday after JUN 1)");
		assertThat(new EasterBasedHoliday("holiday", "Good Friday", -2))
				.hasToString("EasterBasedHoliday(holiday Good Friday: 2 days before Easter)");
		assertThat(new EasterBasedHoliday("holiday", "Easter Monday", +1))
				.hasToString("EasterBasedHoliday(holiday Easter Monday: 1 day after Easter)");
	}

	@Test
	void invalidDate() {
		assertThrows(java.time.DateTimeException.class,
				() -> new FloatingHoliday("holiday", "Famous Februar, 30th",
						1, DayOfWeek.MONDAY, Direction.BEFORE, MonthDay.of(2, 30)));
		assertThrows(java.time.DateTimeException.class,
				() -> new FixedDateHoliday("holiday", "Famous Februar, 30th", MonthDay.of(2, 30)));
	}

	/**
	 * source: https://www.timeanddate.com/calendar/
	 *
	 * @param year        current year
	 * @param expectation string encoding for December days 25 to 28, separated by
	 *                    whitespace
	 */
	@ParameterizedTest(name = "{0} UK Bank Holidays Dec-27: {1} Dec-28: {2}")
	@CsvSource(value = {
			"2010, 1, 2",
			"2011, 1, -",
			"2012, -, -",
			"2013, -, -",
			"2014, -, -",
			"2015, -, 2",
			"2016, 1, -",
			"2017, -, -",
	})
	void conditionalHoliday(int year, String holiday1, String holiday2) {
		final YearMonth yearMonth = YearMonth.of(year, 12);

		List<Holiday> instances = BANK_HOLIDAYS_DECEMBER.instances(yearMonth.atDay(27));
		if (holiday1.equals("-")) {
			assertThat(instances).isEmpty();
		} else {
			assertThat(instances).isNotEmpty();
			assertThat(instances.get(0).getName()).isEqualTo("Bank Holiday " + holiday1);
		}

		instances = BANK_HOLIDAYS_DECEMBER.instances(yearMonth.atDay(28));
		if (holiday2.equals("-")) {
			assertThat(instances).isEmpty();
		} else {
			assertThat(instances).isNotEmpty();
			assertThat(instances.get(0).getName()).isEqualTo("Bank Holiday " + holiday2);
		}
	}

	private static HolidaySet bankHolidaysDecember() {
		final Condition dec25SatSun = new DayOfWeekCondition(
				MonthDay.of(12, 25), DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
		final Holiday bankHoliday1 = new FixedDateHoliday("holiday", "Bank Holiday 1", MonthDay.of(12, 27))
				.withCondition(dec25SatSun);
		final Condition dec26SatSun = new DayOfWeekCondition(
				MonthDay.of(12, 26), DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
		final Holiday bankHoliday2 = new FixedDateHoliday("holiday", "Bank Holiday 2", MonthDay.of(12, 28))
				.withCondition(dec26SatSun);

		return new HolidaySet(
				Arrays.asList(bankHoliday1, bankHoliday2));
	}

	@ParameterizedTest(name = "{0} negated conditional holiday Dec-26: {1}")
	@CsvSource(value = {
			"2010, -",
			"2011, Y",
			"2012, Y",
			"2013, Y",
			"2014, Y",
			"2015, -",
			"2016, Y",
			"2017, Y",
	})
	void negatedHolidays(int year, String holiday) {
		final YearMonth yearMonth = YearMonth.of(year, 12);

		final List<Holiday> instances = NEGATED_HOLIDAY.instances(yearMonth.atDay(26));
		if (holiday.equals("-")) {
			assertThat(instances).isEmpty();
		} else {
			assertThat(instances).isNotEmpty();
			assertThat(instances.get(0).getName()).isEqualTo("Boxing day is extra day off");
		}
	}

	private static HolidaySet negatedDaysOfWeekHoliday() {
		final Condition dec25FriSat = new DayOfWeekCondition(
				MonthDay.of(12, 25), DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
		final Holiday holiday = new FixedDateHoliday("holiday", "Boxing day is extra day off", MonthDay.of(12, 26))
				.withCondition(not(dec25FriSat));

		return new HolidaySet(Arrays.asList(holiday));
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

		final FloatingHoliday holiday;
		if (dayString.equals("last-day")) {

			holiday = new FloatingHoliday(
					"holiday", name, offset,
					dayOfWeekParser.getEnumFor(dayOfWeek),
					Direction.parse(direction),
					month,
					Day.LAST);
		} else {
			holiday = new FloatingHoliday(
					"holiday", name, offset,
					dayOfWeekParser.getEnumFor(dayOfWeek),
					Direction.parse(direction),
					MonthDay.of(month, Integer.parseInt(dayString)));
		}

		assertThat(holiday.of(year)).isEqualTo(expected);
	}
}
