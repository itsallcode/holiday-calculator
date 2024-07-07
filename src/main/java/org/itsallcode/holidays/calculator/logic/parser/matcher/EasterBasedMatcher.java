package org.itsallcode.holidays.calculator.logic.parser.matcher;

import java.util.regex.Matcher;

import org.itsallcode.holidays.calculator.logic.variants.EasterBasedHoliday;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

class EasterBasedMatcher extends HolidayMatcher {
	EasterBasedMatcher() {
		super(Patterns.EASTER_BASED_HOLIDAY);
	}

	@Override
	Holiday createHoliday(Matcher matcher) {
		return new EasterBasedHoliday(
				matcher.group(Patterns.CATEGORY_GROUP),
				matcher.group(Patterns.NAME_GROUP),
				Integer.parseInt(matcher.group(Patterns.OFFSET_GROUP)));
	}
}
