package org.itsallcode.holidays.calculator.logic.parser;

import org.itsallcode.holidays.calculator.logic.parser.matcher.HolidayMatcher;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

/**
 * Class for parsing a single holiday from a string specification.
 */
public class HolidayParser {

	private final HolidayMatcher[] matchers = HolidayMatcher.matchers();

	/**
	 * Create a new instance
	 */
	public HolidayParser() {
		// intentionally empty
	}

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
