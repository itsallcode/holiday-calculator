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
import org.itsallcode.holidays.calculator.logic.parser.DayOfWeekParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HolidayCalculationTest {

	DayOfWeekParser dayOfWeekParser = new DayOfWeekParser();

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
				dayOfWeekParser.getDayOfWeek(dayOfWeek),
				Direction.parse(direction),
				month, day);
		assertThat(holiday.of(year)).isEqualTo(expected);
	}

	/**
	 * sources:
	 * <ul>
	 * <li>[1] http://www.maa.clell.de/StarDate/feiertage.html
	 * <li>[2] https://de.wikipedia.org/wiki/Osterdatum
	 * </ul>
	 */
	@ParameterizedTest(name = "Parsing ''{0}'' returns {1}")
	@CsvSource(value = {
			"1901, 1901-04-07",
			"1902, 1902-03-30",
			"1903, 1903-04-12",
			"1904, 1904-04-03",
			"1905, 1905-04-23",
			"1906, 1906-04-15",
			"1907, 1907-03-31",
			"1908, 1908-04-19",
			"1909, 1909-04-11",
			"1910, 1910-03-27",
			"1911, 1911-04-16",
			"1912, 1912-04-07",
			"1913, 1913-03-23",
			"1914, 1914-04-12",
			"1915, 1915-04-04",
			"1916, 1916-04-23",
			"1917, 1917-04-08",
			"1918, 1918-03-31",
			"1919, 1919-04-20",
			"1920, 1920-04-04",
			"1921, 1921-03-27",
			"1922, 1922-04-16",
			"1923, 1923-04-01",
			"1924, 1924-04-20",
			"1925, 1925-04-12",
			"1926, 1926-04-04",
			"1927, 1927-04-17",
			"1928, 1928-04-08",
			"1929, 1929-03-31",
			"1930, 1930-04-20",
			"1931, 1931-04-05",
			"1932, 1932-03-27",
			"1933, 1933-04-16",
			"1934, 1934-04-01",
			"1935, 1935-04-21",
			"1936, 1936-04-12",
			"1937, 1937-03-28",
			"1938, 1938-04-17",
			"1939, 1939-04-09",
			"1940, 1940-03-24",
			"1941, 1941-04-13",
			"1942, 1942-04-05",
			"1943, 1943-04-25",
			"1944, 1944-04-09",
			"1945, 1945-04-01",
			"1946, 1946-04-21",
			"1947, 1947-04-06",
			"1948, 1948-03-28",
			"1949, 1949-04-17",
			"1950, 1950-04-09",
			"1951, 1951-03-25",
			"1952, 1952-04-13",
			"1953, 1953-04-05",
			"1954, 1954-04-18",
			"1955, 1955-04-10",
			"1956, 1956-04-01",
			"1957, 1957-04-21",
			"1958, 1958-04-06",
			"1959, 1959-03-29",
			"1960, 1960-04-17",
			"1961, 1961-04-02",
			"1962, 1962-04-22",
			"1963, 1963-04-14",
			"1964, 1964-03-29",
			"1965, 1965-04-18",
			"1966, 1966-04-10",
			"1967, 1967-03-26",
			"1968, 1968-04-14",
			"1969, 1969-04-06",
			"1970, 1970-03-29",
			"1971, 1971-04-11",
			"1972, 1972-04-02",
			"1973, 1973-04-22",
			"1974, 1974-04-14",
			"1975, 1975-03-30",
			"1976, 1976-04-18",
			"1977, 1977-04-10",
			"1978, 1978-03-26",
			"1979, 1979-04-15",
			"1980, 1980-04-06",
			"1981, 1981-04-19",
			"1982, 1982-04-11",
			"1983, 1983-04-03",
			"1984, 1984-04-22",
			"1985, 1985-04-07",
			"1986, 1986-03-30",
			"1987, 1987-04-19",
			"1988, 1988-04-03",
			"1989, 1989-03-26",
			"1990, 1990-04-15",
			"1991, 1991-03-31",
			"1992, 1992-04-19",
			"1993, 1993-04-11",
			"1994, 1994-04-03",
			"1995, 1995-04-16",
			"1996, 1996-04-07",
			"1997, 1997-03-30",
			"1998, 1998-04-12",
			"1999, 1999-04-04",
			"2000, 2000-04-23",
			"2001, 2001-04-15",
			"2002, 2002-03-31",
			"2003, 2003-04-20",
			"2004, 2004-04-11",
			"2005, 2005-03-27",
			"2006, 2006-04-16",
			"2007, 2007-04-08",
			"2008, 2008-03-23",
			"2009, 2009-04-12",
			"2010, 2010-04-04",
			"2011, 2011-04-24",
			"2012, 2012-04-08",
			"2013, 2013-03-31",
			"2014, 2014-04-20",
			"2015, 2015-04-05",
			"2016, 2016-03-27",
			"2017, 2017-04-16",
			"2018, 2018-04-01",
			"2019, 2019-04-21",
			"2020, 2020-04-12",
			"2021, 2021-04-04",
			"2022, 2022-04-17",
			"2023, 2023-04-09",
			"2024, 2024-03-31",
			"2025, 2025-04-20",
			"2026, 2026-04-05",
			"2027, 2027-03-28",
			"2028, 2028-04-16",
			"2029, 2029-04-01",
			"2030, 2030-04-21",
			"2031, 2031-04-13",
			"2032, 2032-03-28",
			"2033, 2033-04-17",
			"2034, 2034-04-09",
			"2035, 2035-03-25",
			"2036, 2036-04-13",
			"2037, 2037-04-05",
			"2038, 2038-04-25",
			"2039, 2039-04-10",
			"2040, 2040-04-01",
			"2041, 2041-04-21",
			"2042, 2042-04-06",
			"2043, 2043-03-29",
			"2044, 2044-04-17",
			"2045, 2045-04-09",
			"2046, 2046-03-25",
			"2047, 2047-04-14",
			"2048, 2048-04-05",
			"2049, 2049-04-18",
			"2050, 2050-04-10",
			"2051, 2051-04-02",
			"2052, 2052-04-21",
			"2053, 2053-04-06",
			"2054, 2054-03-29",
			"2055, 2055-04-18",
			"2056, 2056-04-02",
			"2057, 2057-04-22",
			"2058, 2058-04-14",
			"2059, 2059-03-30",
			"2060, 2060-04-18",
			"2061, 2061-04-10",
			"2062, 2062-03-26",
			"2063, 2063-04-15",
			"2064, 2064-04-06",
			"2065, 2065-03-29",
			"2066, 2066-04-11",
			"2067, 2067-04-03",
			"2068, 2068-04-22",
			"2069, 2069-04-14",
			"2070, 2070-03-30",
			"2071, 2071-04-19",
			"2072, 2072-04-10",
			"2073, 2073-03-26",
			"2074, 2074-04-15",
			"2075, 2075-04-07",
			"2076, 2076-04-19",
			"2077, 2077-04-11",
			"2078, 2078-04-03",
	}, nullValues = "NULL")
	void easterDates(int year, LocalDate expectedResult) {
		assertThat(Easter.gauss(year)).isEqualTo(expectedResult);
	}

}
