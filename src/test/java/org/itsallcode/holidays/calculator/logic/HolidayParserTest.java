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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;

import org.itsallcode.holidays.calculator.logic.FloatingHoliday.Direction;
import org.itsallcode.holidays.calculator.logic.parser.AbbreviationParser.AmbigueAbbreviationException;
import org.itsallcode.holidays.calculator.logic.parser.AbbreviationParser.InvalidAbbreviationException;
import org.itsallcode.holidays.calculator.logic.parser.HolidayParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HolidayParserTest {

	private HolidayParser holidayParser;

	@BeforeEach
	private void setup() {
		holidayParser = new HolidayParser();
	}

	@Test
	void category() {
		assertThat(holidayParser.parse("birthday fixed 7 31 My Birthday"))
				.isEqualTo(new FixedDateHoliday("birthday", "My Birthday", 7, 31));
		assertThat(holidayParser.parse("birthday fixed 7 31 My Birthday"))
				.isNotEqualTo(new FixedDateHoliday("holiday", "My Birthday", 7, 31));
	}

	@Test
	void dayOfWeek() {
		// two letters
		assertThat(holidayParser.parse("holiday float 4 SU before 12 24 1. Advent"))
				.isEqualTo(new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
		// full
		assertThat(holidayParser.parse("holiday float 4 SUNDAY before 12 24 1. Advent"))
				.isEqualTo(new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
		// lowercase
		assertThat(holidayParser.parse("holiday float 4 sunday before 12 24 1. Advent"))
				.isEqualTo(new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
	}

	@Test
	void whitespace() {
		assertThat(holidayParser.parse("   holiday    float  4 SUN\tbefore \t 12 24 1. Advent     "))
				.isEqualTo(new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
	}

	@Test
	void invalidPivotDate() {
		assertNull(holidayParser.parse("holiday float 4 SU before 13 1 Famous Februar, 30th"));
		assertNull(holidayParser.parse("holiday float 4 SU before 01 32 Famous Februar, 30th"));
	}

	@Test
	void illegalType() {
		assertNull(holidayParser.parse("holiday illegaType -4 SU 12 24 1. Advent"));
	}

	@Test
	void ambigueDayOfWeekAbbreviation() {
		assertThrows(AmbigueAbbreviationException.class,
				() -> holidayParser.parse("holiday float 1 S before 1 1 Ambigue January S-day"));
		assertThrows(AmbigueAbbreviationException.class,
				() -> holidayParser.parse("holiday float 1 T before 1 1 Ambigue January T-day"));
	}

	@Test
	void invalidDayOfWeekAbbreviation() {
		assertThrows(InvalidAbbreviationException.class,
				() -> holidayParser.parse("holiday float 1 Sonntag before 1 1 Invalid week day"));
	}

	@Test
	void nonAmbigueDayOfWeekAbbreviation() {
		assertThat(holidayParser.parse("holiday float 1 M after 1 1 Non-ambigue M-day"))
				.isEqualTo(new FloatingHoliday("holiday", "Non-ambigue M-day", 1, DayOfWeek.MONDAY, Direction.AFTER, 1,
						1));
		assertThat(holidayParser.parse("holiday float 1 W after 1 1 Non-ambigue W-day"))
				.isEqualTo(new FloatingHoliday("holiday", "Non-ambigue W-day", 1, DayOfWeek.WEDNESDAY, Direction.AFTER,
						1, 1));
		assertThat(holidayParser.parse("holiday float 1 W after 1 1 Non-ambigue F-day"))
				.isEqualTo(new FloatingHoliday("holiday", "Non-ambigue F-day", 1, DayOfWeek.WEDNESDAY, Direction.AFTER,
						1, 1));
	}

	@Test
	void offsetTooLarge() {
		assertNull(holidayParser.parse("holiday float 213 SU before 12 24 1. Advent"));
	}

	@Test
	void notEqualFixed() {
		// day
		assertThat(holidayParser.parse("holiday fixed 1 1 Neujahr"))
				.isNotEqualTo(new FixedDateHoliday("holiday", "Neujahr", 1, 2));
		// name
		assertThat(holidayParser.parse("holiday fixed 1 1 Neujahr"))
				.isNotEqualTo(new FixedDateHoliday("holiday", "xxx", 1, 1));
	}

	@Test
	void notEqualFLoating() {
		// name
		assertThat(holidayParser.parse("holiday float 4 SUN before 12 24 1. Advent"))
				.isNotEqualTo(new FloatingHoliday("holiday", "1. Advent AAA", 4, DayOfWeek.SUNDAY, Direction.BEFORE,
						12, 24));
		// month
		assertThat(holidayParser.parse("holiday float 4 SUN before 12 24 1. Advent"))
				.isNotEqualTo(
						new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 11, 24));
		// day
		assertThat(holidayParser.parse("holiday float 4 SUN before 12 24 1. Advent"))
				.isNotEqualTo(
						new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 23));
		// day of week
		assertThat(holidayParser.parse("holiday float 4 SUN before 12 24 1. Advent"))
				.isNotEqualTo(
						new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.MONDAY, Direction.BEFORE, 12, 24));
		// offset
		assertThat(holidayParser.parse("holiday float 4 SUN before 12 24 1. Advent"))
				.isNotEqualTo(
						new FloatingHoliday("holiday", "1. Advent", 2, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
	}

	@Test
	void leadingZeros() {
		assertThat(holidayParser.parse("holiday fixed 01 01 Neujahr"))
				.isEqualTo(new FixedDateHoliday("holiday", "Neujahr", 1, 1));
		assertThat(holidayParser.parse("holiday float 1 MON before 01 01 Fictional New Year's Monday"))
				.isEqualTo(new FloatingHoliday("holiday", "Fictional New Year's Monday", 1, DayOfWeek.MONDAY,
						Direction.BEFORE, 1, 1));
	}

	@Test
	void monthNames() {
		assertThat(holidayParser.parse("holiday fixed Jan 1 Neujahr"))
				.isEqualTo(new FixedDateHoliday("holiday", "Neujahr", 1, 1));
		assertThat(holidayParser.parse("holiday fixed D 24 XMas Eve"))
				.isEqualTo(new FixedDateHoliday("holiday", "XMas Eve", 12, 24));
		assertThat(holidayParser.parse("holiday float 4 SUN before Decem 24 1. Advent"))
				.isEqualTo(new FloatingHoliday(
						"holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
	}

	@Test
	void ambigueMonthName() {
		assertThrows(AmbigueAbbreviationException.class,
				() -> holidayParser.parse("holiday fixed M 1 Ambigue M-Month 1"));
		assertThrows(AmbigueAbbreviationException.class,
				() -> holidayParser.parse("holiday fixed j 30 Ambigue J-Month 30"));
	}

	@Test
	void invalidMonthAbbreviation() {
		assertThrows(InvalidAbbreviationException.class,
				() -> holidayParser.parse("holiday fixed Me 1 Ambigue Me-Month 1"));
	}

	@Test
	void fixedDate() {
		assertThat(holidayParser.parse("holiday fixed 1 1 Neujahr"))
				.isEqualTo(new FixedDateHoliday("holiday", "Neujahr", 1, 1));
	}

	@Test
	void floatingDate() {
		assertThat(holidayParser.parse("holiday float 4 SUN before 12 24 1. Advent"))
				.isEqualTo(new FloatingHoliday(
						"holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
	}

	@Test
	void easter() {
		assertThat(holidayParser.parse("holiday easter +39 Christi Himmelfahrt"))
				.isEqualTo(
						new EasterBasedHoliday("holiday", "Christi Himmelfahrt", +39));
	}

	@Test
	void orthodoxEaster() {
		assertThat(holidayParser.parse("holiday orthodox-easter -2 Orthodox Good Friday"))
				.isEqualTo(
						new OrthodoxEasterBasedHoliday("holiday", "Orthodox Good Friday", -2));
	}

	@Test
	void successfulParsing() {
		assertThat(holidayParser.parse("holiday fixed 1 1 Neujahr"))
				.isEqualTo(new FixedDateHoliday("holiday", "Neujahr", 1, 1));
		assertThat(holidayParser.parse("holiday fixed 1 6 Heilige Drei Könige"))
				.isEqualTo(new FixedDateHoliday("holiday", "Heilige Drei Könige", 1, 6));
		assertThat(holidayParser.parse("holiday fixed 5 1 1. Mai"))
				.isEqualTo(new FixedDateHoliday("holiday", "1. Mai", 5, 1));
		assertThat(holidayParser.parse("holiday fixed 10 3 Tag der Deutschen Einheit"))
				.isEqualTo(new FixedDateHoliday("holiday", "Tag der Deutschen Einheit", 10, 3));

		assertThat(holidayParser.parse("holiday float 4 SUN before 12 24 1. Advent"))
				.isEqualTo(new FloatingHoliday("holiday", "1. Advent", 4, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
		assertThat(holidayParser.parse("holiday float 3 SUN before 12 24 2. Advent"))
				.isEqualTo(new FloatingHoliday("holiday", "2. Advent", 3, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
		assertThat(holidayParser.parse("holiday float 2 SUN before 12 24 3. Advent"))
				.isEqualTo(new FloatingHoliday("holiday", "3. Advent", 2, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
		assertThat(holidayParser.parse("holiday float 1 SUN before 12 24 4. Advent"))
				.isEqualTo(new FloatingHoliday("holiday", "4. Advent", 1, DayOfWeek.SUNDAY, Direction.BEFORE, 12, 24));
		assertThat(holidayParser.parse("holiday fixed 12 25 1. Weihnachtstag"))
				.isEqualTo(new FixedDateHoliday("holiday", "1. Weihnachtstag", 12, 25));
		assertThat(holidayParser.parse("holiday fixed 12 26 2. Weihnachtstag"))
				.isEqualTo(new FixedDateHoliday("holiday", "2. Weihnachtstag", 12, 26));

		assertThat(holidayParser.parse("holiday easter -48 Rosenmontag"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Rosenmontag", -48));
		assertThat(holidayParser.parse("holiday easter -2 Karfreitag"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Karfreitag", -2));
		assertThat(holidayParser.parse("holiday easter 0 Ostersonntag"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Ostersonntag", 0));
		assertThat(holidayParser.parse("holiday easter +1 Ostermontag"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Ostermontag", +1));

		assertThat(holidayParser.parse("holiday easter +39 Christi Himmelfahrt"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Christi Himmelfahrt", +39));
		assertThat(holidayParser.parse("holiday easter +49 Pfingstsonntag"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Pfingstsonntag", +49));
		assertThat(holidayParser.parse("holiday easter +50 Pfingstmontag"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Pfingstmontag", +50));
		assertThat(holidayParser.parse("holiday easter +60 Fronleichnam"))
				.isEqualTo(new EasterBasedHoliday("holiday", "Fronleichnam", +60));
		assertThat(holidayParser.parse("holiday fixed 8 15 Mariae Himmelfahrt"))
				.isEqualTo(new FixedDateHoliday("holiday", "Mariae Himmelfahrt", 8, 15));
		assertThat(holidayParser.parse("holiday fixed 11 1 Allerheiligen"))
				.isEqualTo(new FixedDateHoliday("holiday", "Allerheiligen", 11, 1));
		assertThat(holidayParser.parse("holiday float 1 SUN after 11 20 Totensonntag"))
				.isEqualTo(new FloatingHoliday(
						"holiday", "Totensonntag", 1, DayOfWeek.SUNDAY, Direction.AFTER, 11, 20));
	}

}
