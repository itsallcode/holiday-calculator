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
package org.itsallcode.holidays.calculator.logic.parser.matcher;

import static java.util.stream.Collectors.toList;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;
import org.itsallcode.holidays.calculator.logic.parser.AbbreviationParser;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

/**
 * Abstract class for matchers for holiday specifications.
 */
public abstract class HolidayMatcher {

	/**
	 * Return a list of all available holiday specification matchers.
	 *
	 * @return list of available matchers
	 */
	public static HolidayMatcher[] matchers() {
		return new HolidayMatcher[] {
				new NegatedConditionMatcher(new FixedDateMatcher(),
						Patterns.FIXED_HOLIDAY_CONDITIONAL_NEGATED),
				new FixedDateMatcher.Conditional(),
				new FixedDateMatcher(),
				new FixedDateMatcher.Alternative(Patterns.ALTERNATIVE_DATE_HOLIDAY_NEGATED_DAY_OF_WEEK),
				new FixedDateMatcher.Alternative(Patterns.ALTERNATIVE_DATE_HOLIDAY),
				new FloatingDateMatcher.OffsetMatcher(),
				new FloatingDateMatcher(),
				new EasterBasedMatcher(),
				new OrthodoxEasterBasedMatcher()
		};
	}

	abstract Holiday createHoliday(Matcher matcher);

	private static final Pattern MONTH_NAME_PATTERN = Pattern.compile(
			Patterns.NAME_REGEXP, Pattern.CASE_INSENSITIVE);

	private final AbbreviationParser<Month> monthNameParser = new AbbreviationParser<>(Month.class);
	private final AbbreviationParser<DayOfWeek> dayOfWeekParser = new AbbreviationParser<>(DayOfWeek.class);
	private final Pattern pattern;

	private final HolidayMatcher originalMatcher;

	/**
	 * Create a new instance of a generic holiday matcher based on the provided
	 * pattern.
	 *
	 * @param pattern pattern for detecting the current matcher to be used for
	 *                parsing the holiday from the specification.
	 */
	protected HolidayMatcher(Pattern pattern) {
		this(null, pattern);
	}

	/**
	 * Create a new instance of a generic holiday matcher based on the provided
	 * matcher for the original date of the holiday and a pattern for additional
	 * modifications, e.g. conditions, alternative dates, or time deltas from the
	 * original date.
	 *
	 * @param originalMatcher matcher for the original holiday specification
	 * @param pattern         pattern for detecting the current matcher to be used
	 *                        for parsing the holiday from the specification.
	 */
	protected HolidayMatcher(HolidayMatcher originalMatcher, Pattern pattern) {
		this.originalMatcher = originalMatcher;
		this.pattern = pattern;
	}

	/**
	 * Return the original holiday specified, e.g. if the condition is
	 * {@code false}.
	 *
	 * @param matcher regular expression matcher for retrieving the specification of
	 *                the original holiday
	 * @return the original holiday
	 */
	protected Holiday createOriginalHoliday(Matcher matcher) {
		return originalMatcher.createHoliday(matcher);
	}

	/**
	 * Create a new holiday based on the specification in the current line of input.
	 *
	 * @param line textual specification for the holiday
	 * @return holiday parsed from the specification
	 */
	public Holiday createHoliday(String line) {
		final Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()) {
			return null;
		}
		return createHoliday(matcher);
	}

	/**
	 * @param arg (abbreviated) name of month or number as String
	 * @return number of month as integer
	 */
	protected int monthNumber(String arg) {
		if (MONTH_NAME_PATTERN.matcher(arg).matches()) {
			return monthNameParser.getEnumFor(arg).getValue();
		} else {
			return Integer.parseInt(arg);
		}
	}

	/**
	 * Parse a {@link MonthDay} instance from strings representing the month and the
	 * day.
	 *
	 * @param month number or mnemonic of a month as a string
	 * @param day   day number of the day in the given month as a string
	 * @return {@link MonthDay} instance
	 */
	protected MonthDay monthDay(String month, String day) {
		return MonthDay.of(monthNumber(month), Integer.parseInt(day));
	}

	/**
	 * Parse the day of the week from a string.
	 *
	 * @param prefix potentially abbreviated name of the week day, i.e. all of the
	 *               following variants are possible: "Sa", "Sat", "Satur",
	 *               "Saturday". "T" is illegal, as it is ambiguous and could be
	 *               either "Tuesday" or "Thursday".
	 * @return {@link DayOfWeek} instance
	 */
	protected DayOfWeek dayOfWeek(String prefix) {
		return dayOfWeekParser.getEnumFor(prefix);
	}

	/**
	 * Parse days of the week from a string containing comma-separated potentially
	 * abbreviated names.
	 *
	 * @param commaSeparatedList string to parse the days of week from
	 * @return list of instances of {@link DayOfWeek}
	 */
	protected DayOfWeek[] daysOfWeek(String commaSeparatedList) {
		return Arrays.asList(commaSeparatedList.split(","))
				.stream().map(this::dayOfWeek)
				.collect(toList())
				.toArray(new DayOfWeek[0]);
	}

	/**
	 * Create a builder for a condition that is {@code true} on the specified days
	 * of the week or the specified pivot date.
	 *
	 * @param matcher regular expression matcher for retrieving the specification
	 *                for the condition
	 * @return condition builder
	 */
	public ConditionBuilder createConditionBuilder(Matcher matcher) {
		return new ConditionBuilder()
				.withDaysOfWeek(daysOfWeek(matcher.group(Patterns.PIVOT_DAYS_OF_WEEK_GROUP)))
				.withPivotDate(monthDay(
						matcher.group(Patterns.MONTH_GROUP), matcher.group(Patterns.DAY_GROUP)));
	}
}
