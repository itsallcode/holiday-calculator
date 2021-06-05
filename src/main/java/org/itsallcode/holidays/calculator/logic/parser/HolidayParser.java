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
package org.itsallcode.holidays.calculator.logic.parser;

import java.time.DayOfWeek;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.EasterBasedHoliday;
import org.itsallcode.holidays.calculator.logic.FixedDateHoliday;
import org.itsallcode.holidays.calculator.logic.FloatingHoliday;
import org.itsallcode.holidays.calculator.logic.FloatingHoliday.Direction;
import org.itsallcode.holidays.calculator.logic.Holiday;
import org.itsallcode.holidays.calculator.logic.OrthodoxEasterBasedHoliday;

public class HolidayParser {

	// names of groups in regular expressions in order to easily extract matched
	// parts
	private static final String CATEGORY_GROUP = "category";
	private static final String TYPE_GROUP = "type";
	private static final String MONTH_GROUP = "month";
	private static final String DAY_GROUP = "day";
	private static final String OFFSET_GROUP = "offset";
	private static final String DIRECTION_GROUP = "direction";
	private static final String DAY_OF_WEEK_GROUP = "dayOfWeek";
	private static final String NAME_GROUP = "name";

	public static final String NAME_REGEXP = "[a-z]+";

	// tokens for regular expressions
	private static final Token MONTH = new Token(MONTH_GROUP, NAME_REGEXP + "|0?1|0?2|0?3|0?4|0?5|0?6|0?7|0?8|0?9|10|11|12");
	private static final Token DAY = new Token(DAY_GROUP,
			"0?1|0?2|0?3|0?4|0?5|0?6|0?7|0?8|0?9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31");
	public static final String LAST_DAY = "last-day";
	private static final Token DAY_OR_DEFAULT = new Token(DAY_GROUP, DAY.pattern + "|" + LAST_DAY);
	private static final Token OFFSET = new Token(OFFSET_GROUP, "[+-]?\\d\\d?");
	private static final Token POSITIVE_OFFSET = new Token(OFFSET_GROUP, "\\d\\d?");
	private static final Token DIRECTION = new Token(DIRECTION_GROUP, "before|after");
	private static final Token DAY_OF_WEEK = new Token(DAY_OF_WEEK_GROUP, NAME_REGEXP);
	private static final Token HOLIDAY_NAME = new Token(NAME_GROUP, ".*");

	private static final Token CATEGORY = new Token(CATEGORY_GROUP, "\\S+");
	private static final Token FIXED = new Token(TYPE_GROUP, "fixed");
	private static final Token FLOAT = new Token(TYPE_GROUP, "float");
	private static final Token EASTER = new Token(TYPE_GROUP, "easter");
	private static final Token ORTHODOX_EASTER = new Token(TYPE_GROUP, "orthodox-easter");

	// patterns
	private static final String SPACE_REGEXP = "\\s+";
	private static final Pattern FIXED_HOLIDAY = buildRegexp(CATEGORY, FIXED, MONTH, DAY, HOLIDAY_NAME);
	private static final Pattern FLOATING_HOLIDAY = buildRegexp( //
			CATEGORY, FLOAT, POSITIVE_OFFSET, DAY_OF_WEEK, DIRECTION, MONTH, DAY_OR_DEFAULT, HOLIDAY_NAME);
	private static final Pattern EASTER_BASED_HOLIDAY = buildRegexp(CATEGORY, EASTER, OFFSET, HOLIDAY_NAME);
	private static final Pattern ORTHODOX_EASTER_BASED_HOLIDAY = buildRegexp(CATEGORY, ORTHODOX_EASTER, OFFSET,
			HOLIDAY_NAME);

	static Pattern buildRegexp(final Token... tokens) {
		final StringBuilder sb = new StringBuilder();
		for (final Token token : tokens) {
			if (sb.length() > 0) {
				sb.append(SPACE_REGEXP);
			}
			sb.append(String.format("(?<%s>%s)", token.groupName, token.pattern));
		}

		return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
	}

	public Holiday parse(String line) {
		final String trimmed = line.trim();
		final HolidayMatcher[] matchers = new HolidayMatcher[] {
				new FixedDateMatcher(),
				new FloatingDateMatcher(),
				new EasterBasedMatcher(),
				new OrthodoxEasterBasedMatcher()
		};

		for (final HolidayMatcher m : matchers) {
			final Holiday holiday = m.createHoliday(trimmed);
			if (holiday != null) {
				return holiday;
			}
		}

		return null;
	}

	private static class FixedDateMatcher extends HolidayMatcher {
		public FixedDateMatcher() {
			super(FIXED_HOLIDAY);
		}

		@Override
		Holiday createHoliday(Matcher matcher) {
			return new FixedDateHoliday(
					matcher.group(CATEGORY_GROUP),
					matcher.group(NAME_GROUP),
					monthNumber(matcher.group(MONTH_GROUP)),
					Integer.parseInt(matcher.group(DAY_GROUP)));
		}
	}

	private static class FloatingDateMatcher extends HolidayMatcher {
		private final AbbreviationParser<DayOfWeek> dayOfWeekParser = new AbbreviationParser<>(DayOfWeek.class);

		public FloatingDateMatcher() {
			super(FLOATING_HOLIDAY);
		}

		@Override
		Holiday createHoliday(Matcher matcher) {
			final DayOfWeek dayOfWeek = dayOfWeekParser.getEnumFor(matcher.group(DAY_OF_WEEK_GROUP));
			if (dayOfWeek == null) {
				return null;
			}

			return new FloatingHoliday(
					matcher.group(CATEGORY_GROUP),
					matcher.group(NAME_GROUP),
					Integer.parseInt(matcher.group(OFFSET_GROUP)),
					dayOfWeek,
					Direction.parse(matcher.group(DIRECTION_GROUP)),
					monthNumber(matcher.group(MONTH_GROUP)),
					Integer.parseInt(matcher.group(DAY_GROUP)));
		}
	}

	private static class EasterBasedMatcher extends HolidayMatcher {
		public EasterBasedMatcher() {
			super(EASTER_BASED_HOLIDAY);
		}

		@Override
		Holiday createHoliday(Matcher matcher) {
			return new EasterBasedHoliday(
					matcher.group(CATEGORY_GROUP),
					matcher.group(NAME_GROUP),
					Integer.parseInt(matcher.group(OFFSET_GROUP)));
		}
	}

	private static class OrthodoxEasterBasedMatcher extends HolidayMatcher {
		public OrthodoxEasterBasedMatcher() {
			super(ORTHODOX_EASTER_BASED_HOLIDAY);
		}

		@Override
		Holiday createHoliday(Matcher matcher) {
			return new OrthodoxEasterBasedHoliday(
					matcher.group(CATEGORY_GROUP),
					matcher.group(NAME_GROUP),
					Integer.parseInt(matcher.group(OFFSET_GROUP)));
		}
	}

	private static class Token {
		public final String groupName;
		public final String pattern;

		public Token(String groupName, String pattern) {
			this.groupName = groupName;
			this.pattern = pattern;
		}
	}
}
