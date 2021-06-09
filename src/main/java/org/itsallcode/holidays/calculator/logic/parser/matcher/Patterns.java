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

import static org.itsallcode.holidays.calculator.logic.parser.Token.buildRegexp;

import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.parser.Token;

public class Patterns {

	// names of groups in regular expressions in order to easily extract matched
	// parts
	static final String CATEGORY_GROUP = "category";
	static final String MONTH_GROUP = "month";
	static final String DAY_GROUP = "day";
	static final String OFFSET_GROUP = "offset";
	static final String DIRECTION_GROUP = "direction";
	static final String DAY_OF_WEEK_GROUP = "dayOfWeek";
	static final String NAME_GROUP = "name";

	static final String MONTH_GROUP_2 = "pivotMonth";
	static final String DAY_GROUP_2 = "pivotDay";
	static final String PIVOT_DAYS_OF_WEEK_GROUP = "pivotDaysOfWeek";

	public static final String NAME_REGEXP = "[a-z]+";
	public static final String NAMES_REGEXP = "[a-z,]+";
	private static final String MONTH_REGEX = NAME_REGEXP + "|0?1|0?2|0?3|0?4|0?5|0?6|0?7|0?8|0?9|10|11|12";
	private static final String DAY_REGEX = "0?1|0?2|0?3|0?4|0?5|0?6|0?7|0?8|0?9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31";

	// tokens for regular expressions
	private static final Token CATEGORY = new Token(CATEGORY_GROUP, "\\S+");
	private static final Token MONTH = new Token(MONTH_GROUP, MONTH_REGEX);
	private static final Token DAY = new Token(DAY_GROUP, DAY_REGEX);
	public static final String LAST_DAY = "last-day";
	private static final Token DAY_OR_DEFAULT = new Token(DAY_GROUP, DAY.pattern + "|" + LAST_DAY);
	private static final Token OFFSET = new Token(OFFSET_GROUP, "[+-]?\\d\\d?");
	private static final Token POSITIVE_OFFSET = new Token(OFFSET_GROUP, "\\d\\d?");
	private static final Token DIRECTION = new Token(DIRECTION_GROUP, "before|after");
	private static final Token DAY_OF_WEEK = new Token(DAY_OF_WEEK_GROUP, NAME_REGEXP);
	private static final Token HOLIDAY_NAME = new Token(NAME_GROUP, ".*");

	private static final Token MONTH_2 = new Token(MONTH_GROUP_2, MONTH_REGEX);
	private static final Token DAY_2 = new Token(DAY_GROUP_2, DAY_REGEX);
	private static final Token PIVOT_DAYS_OF_WEEK = new Token(PIVOT_DAYS_OF_WEEK_GROUP, NAMES_REGEXP);

	// patterns
	public static final String SPACE_REGEXP = "\\s+";
	static final Pattern FIXED_HOLIDAY = buildRegexp(CATEGORY, "fixed", MONTH, DAY,
			HOLIDAY_NAME);

	static final Pattern CONDITIONAL_FIXED_HOLIDAY = buildRegexp(
			CATEGORY, "if", MONTH_2, DAY_2, "is", PIVOT_DAYS_OF_WEEK, "then", "fixed", MONTH, DAY,
			HOLIDAY_NAME);
	static final Pattern NEGATED_CONDITIONAL_FIXED_HOLIDAY = buildRegexp(
			CATEGORY, "if", MONTH_2, DAY_2, "is", "not", PIVOT_DAYS_OF_WEEK, "then", "fixed", MONTH, DAY,
			HOLIDAY_NAME);

	static final Pattern ALTERNATIVE_DATE_HOLIDAY = buildRegexp(
			CATEGORY, "either", MONTH, DAY, "or", "if",
			PIVOT_DAYS_OF_WEEK, "then", "fixed", MONTH_2, DAY_2, HOLIDAY_NAME);

	static final Pattern ALTERNATIVE_DATE_HOLIDAY_WITH_NEGATED_DAY_OF_WEEK = buildRegexp(
			CATEGORY, "either", MONTH, DAY, "or", "if", "not",
			PIVOT_DAYS_OF_WEEK, "then", "fixed", MONTH_2, DAY_2, HOLIDAY_NAME);

	static final Pattern FLOATING_HOLIDAY = buildRegexp( //
			CATEGORY, "float", POSITIVE_OFFSET, DAY_OF_WEEK, DIRECTION, MONTH, DAY_OR_DEFAULT, HOLIDAY_NAME);
	static final Pattern EASTER_BASED_HOLIDAY = buildRegexp(CATEGORY, "easter", OFFSET, HOLIDAY_NAME);
	static final Pattern ORTHODOX_EASTER_BASED_HOLIDAY = buildRegexp(CATEGORY, "orthodox-easter", OFFSET,
			HOLIDAY_NAME);

}
