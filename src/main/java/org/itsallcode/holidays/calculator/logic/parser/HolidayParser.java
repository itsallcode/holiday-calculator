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
package org.itsallcode.holidays.calculator.logic.parser;

import org.itsallcode.holidays.calculator.logic.parser.matcher.HolidayMatcher;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

/**
 * Class for parsing a single holiday from a string specification.
 */
public class HolidayParser {

	private final HolidayMatcher[] matchers = HolidayMatcher.matchers();

	/**
	 * Parse a single holiday from a string containing the holiday's specification.
	 *
	 * @param line containing the holday's specification.
	 * @return parse holiday or null if none of the available holiday matchers
	 *         matched the specification.
	 */
	public Holiday parse(String line) {
		final String trimmed = line.trim();
		for (final HolidayMatcher m : matchers) {
			final Holiday holiday = m.createHoliday(trimmed);
			if (holiday != null) {
				return holiday;
			}
		}

		return null;
	}
}
