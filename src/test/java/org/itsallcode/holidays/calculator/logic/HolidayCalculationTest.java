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
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;
import org.itsallcode.holidays.calculator.logic.parser.AbbreviationParser;
import org.itsallcode.holidays.calculator.logic.variants.ConditionalHoliday;
import org.itsallcode.holidays.calculator.logic.variants.EasterBasedHoliday;
import org.itsallcode.holidays.calculator.logic.variants.FixedDateHoliday;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Day;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Direction;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;
import org.itsallcode.holidays.calculator.logic.variants.HolidayWithAlternative;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HolidayCalculationTest {

	static final AbbreviationParser<DayOfWeek> DAY_OF_WEEK_PARSER = new AbbreviationParser<>(DayOfWeek.class);

	static final Holiday BANK_HOLIDAY_DEC_27;
	static final Holiday BANK_HOLIDAY_DEC_28;

	static final Holiday KONINGSDAG;
	static final Holiday KONINGSDAG_WITH_NEGATED_DAYS_OF_WEEK;

	static final HolidaySet BANK_HOLIDAYS_DECEMBER;
	static final Holiday CONDITIONAL_HOLIDAY_WITH_NEGATED_DAYS_OF_WEEK;
	static final HolidaySet NEGATED_DAYS_OF_WEEK_HOLIDAYS;

	static final Holiday MIDSOMMARAFTON;

	static {
		final ConditionBuilder dec25SatSun = new ConditionBuilder()
				.withDaysOfWeek(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
				.withPivotDate(MonthDay.of(12, 25));
		final Holiday bankHolidayDec27 = new FixedDateHoliday("holiday", "Bank Holiday 1", MonthDay.of(12, 27));
		BANK_HOLIDAY_DEC_27 = new ConditionalHoliday(dec25SatSun, bankHolidayDec27);

		final ConditionBuilder dec26SatSun = new ConditionBuilder()
				.withDaysOfWeek(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
				.withPivotDate(MonthDay.of(12, 26));
		final Holiday bankHolidayDec28 = new FixedDateHoliday("holiday", "Bank Holiday 2", MonthDay.of(12, 28));
		BANK_HOLIDAY_DEC_28 = new ConditionalHoliday(dec26SatSun, bankHolidayDec28);

		final Holiday defaultKoningsdag = new FixedDateHoliday("holiday", "Koningsdag", MonthDay.of(4, 27));
		final ConditionBuilder isSunday = new ConditionBuilder().withDaysOfWeek(DayOfWeek.SUNDAY);
		KONINGSDAG = new HolidayWithAlternative(defaultKoningsdag, isSunday, MonthDay.of(4, 26));

		final ConditionBuilder isAnyDayButSunday = new ConditionBuilder().withDaysOfWeek(
				DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
				DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

		KONINGSDAG_WITH_NEGATED_DAYS_OF_WEEK = new HolidayWithAlternative(
				defaultKoningsdag, isAnyDayButSunday.negated(), MonthDay.of(4, 26));

		BANK_HOLIDAYS_DECEMBER = new HolidaySet(Arrays.asList(BANK_HOLIDAY_DEC_27, BANK_HOLIDAY_DEC_28));

		final ConditionBuilder dec25FriSat = new ConditionBuilder()
				.withDaysOfWeek(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
				.withPivotDate(MonthDay.of(12, 25));
		final Holiday boxingDay = new FixedDateHoliday("holiday", "Boxing day is extra day off", MonthDay.of(12, 26));
		CONDITIONAL_HOLIDAY_WITH_NEGATED_DAYS_OF_WEEK = new ConditionalHoliday(dec25FriSat.negated(),
				boxingDay);

		NEGATED_DAYS_OF_WEEK_HOLIDAYS = new HolidaySet(
				Arrays.asList(CONDITIONAL_HOLIDAY_WITH_NEGATED_DAYS_OF_WEEK));

		MIDSOMMARAFTON = new FloatingHoliday("holiday", "Midsommarafton",
				1, DayOfWeek.SATURDAY, Direction.BEFORE, MonthDay.of(6, 26)).withOffsetInDays(-1);
	}

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
		assertThat(BANK_HOLIDAY_DEC_27)
				.hasToString("FixedDateHoliday(holiday Bank Holiday 1: DEC 27 only if DEC 25 is Sat,Sun)");

		final Holiday negatedDaysOfWeekHoliday = NEGATED_DAYS_OF_WEEK_HOLIDAYS.getDefinitions().get(0);
		assertThat(negatedDaysOfWeekHoliday).hasToString(
				"FixedDateHoliday(holiday Boxing day is extra day off: DEC 26 only if DEC 25 is not Fri,Sat)");

		assertThat(KONINGSDAG).hasToString(
				"FixedDateHoliday(holiday Koningsdag: APR 27 or if APR 27 is Sun then APR 26)");
		assertThat(KONINGSDAG_WITH_NEGATED_DAYS_OF_WEEK).hasToString(
				"FixedDateHoliday(holiday Koningsdag: APR 27 or if APR 27 is not Mon,Tue,Wed,Thu,Fri,Sat then APR 26)");

		assertThat(MIDSOMMARAFTON).hasToString(
				"FloatingHoliday(holiday Midsommarafton: 1 day before 1st Saturday before JUN 26)");
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

	@Test
	void alternativeDateHoliday() {
		assertThat(KONINGSDAG.of(2014)).isEqualTo(LocalDate.of(2014, 4, 26));
		assertThat(KONINGSDAG.of(2021)).isEqualTo(LocalDate.of(2021, 4, 27));

		assertThat(KONINGSDAG_WITH_NEGATED_DAYS_OF_WEEK.of(2014)).isEqualTo(LocalDate.of(2014, 4, 26));
		assertThat(KONINGSDAG_WITH_NEGATED_DAYS_OF_WEEK.of(2021)).isEqualTo(LocalDate.of(2021, 4, 27));
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
	void negatedDaysOfWeekHolidays(int year, String holiday) {
		final YearMonth yearMonth = YearMonth.of(year, 12);

		final List<Holiday> instances = NEGATED_DAYS_OF_WEEK_HOLIDAYS.instances(yearMonth.atDay(26));
		if (holiday.equals("-")) {
			assertThat(instances).isEmpty();
		} else {
			assertThat(instances).isNotEmpty();
			assertThat(instances.get(0).getName()).isEqualTo("Boxing day is extra day off");
		}
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
					DAY_OF_WEEK_PARSER.getEnumFor(dayOfWeek),
					Direction.parse(direction),
					month,
					Day.LAST);
		} else {
			holiday = new FloatingHoliday(
					"holiday", name, offset,
					DAY_OF_WEEK_PARSER.getEnumFor(dayOfWeek),
					Direction.parse(direction),
					MonthDay.of(month, Integer.parseInt(dayString)));
		}

		assertThat(holiday.of(year)).isEqualTo(expected);
	}

	@Test
	void floatingHolidayWithNegativeOffset() {
		assertThrows(java.lang.IllegalArgumentException.class,
				() -> new FloatingHoliday("holiday", "Negative offset", -1, DayOfWeek.SATURDAY, Direction.AFTER,
						MonthDay.of(6, 26)));
	}

	@ParameterizedTest(name = "Midsommarafton {0} June {1}")
	@CsvSource(value = {
			"2010, 25", "2011, 24", "2012, 22", "2013, 21", "2014, 20",
			"2015, 19", "2016, 24", "2017, 23", "2018, 22", "2019, 21",
			"2020, 19", "2021, 25"
	})
	void midsommarAfton_floatingHolidayWithOffsetInDays(int year, int day) {
		assertThat(MIDSOMMARAFTON.of(year)).isEqualTo(LocalDate.of(year, 6, day));
	}
}
