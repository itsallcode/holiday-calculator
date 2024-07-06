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

public abstract class HolidayMatcher {

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

	protected HolidayMatcher(Pattern pattern) {
		this(null, pattern);
	}

	protected HolidayMatcher(HolidayMatcher originalMatcher, Pattern pattern) {
		this.originalMatcher = originalMatcher;
		this.pattern = pattern;
	}

	protected Holiday createOriginalHoliday(Matcher matcher) {
		return originalMatcher.createHoliday(matcher);
	}

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

	protected MonthDay monthDay(String month, String day) {
		return MonthDay.of(monthNumber(month), Integer.parseInt(day));
	}

	protected DayOfWeek dayOfWeek(String prefix) {
		return dayOfWeekParser.getEnumFor(prefix);
	}

	protected DayOfWeek[] daysOfWeek(String commaSeparatedList) {
		return Arrays.asList(commaSeparatedList.split(","))
				.stream().map(this::dayOfWeek)
				.collect(toList())
				.toArray(new DayOfWeek[0]);
	}

	public ConditionBuilder createConditionBuilder(Matcher matcher) {
		return new ConditionBuilder()
				.withDaysOfWeek(daysOfWeek(matcher.group(Patterns.PIVOT_DAYS_OF_WEEK_GROUP)))
				.withPivotDate(monthDay(
						matcher.group(Patterns.MONTH_GROUP), matcher.group(Patterns.DAY_GROUP)));
	}
}
