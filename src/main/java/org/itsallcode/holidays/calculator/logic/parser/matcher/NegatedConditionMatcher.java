package org.itsallcode.holidays.calculator.logic.parser.matcher;

import java.time.MonthDay;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.variants.ConditionalHoliday;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

class NegatedConditionMatcher extends HolidayMatcher {

	NegatedConditionMatcher(final HolidayMatcher originalMatcher, final Pattern pattern) {
		super(originalMatcher, pattern);
	}

	@Override
	Holiday createHoliday(final Matcher matcher) {
		final MonthDay pivotDate = monthDay(matcher, Patterns.MONTH_GROUP_2, Patterns.DAY_GROUP_2);
		return new ConditionalHoliday( //
				createConditionBuilder(matcher).withPivotDate(pivotDate).negated(),
				createOriginalHoliday(matcher));
	}
}
