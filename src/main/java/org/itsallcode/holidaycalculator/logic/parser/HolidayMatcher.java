package org.itsallcode.holidaycalculator.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itsallcode.holidaycalculator.logic.Holiday;

public abstract class HolidayMatcher {
	abstract Holiday createHoliday(Matcher matcher);

	private final Pattern pattern;

	public HolidayMatcher(Pattern pattern) {
		this.pattern = pattern;
	}

	public Holiday createHoliday(String line) {
		final Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()) {
			return null;
		}
		return createHoliday(matcher);
	}

}
