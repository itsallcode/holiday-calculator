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

import java.time.Month;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.Holiday;

public abstract class HolidayMatcher {

	abstract Holiday createHoliday(Matcher matcher);

	private static final Pattern MONTH_NAME_PATTERN = Pattern.compile(
			HolidayParser.MONTH_NAME, Pattern.CASE_INSENSITIVE);

	private final AbbreviationParser<Month> monthNameParser = new AbbreviationParser<>(Month.class);
	private final Pattern pattern;

	protected HolidayMatcher(Pattern pattern) {
		this.pattern = pattern;
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

}
