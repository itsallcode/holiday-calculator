package org.itsallcode.holidays.calculator.logic.parser.matcher;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.regex.Matcher;

import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Day;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Direction;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

class FloatingDateMatcher extends HolidayMatcher {
	FloatingDateMatcher() {
		super(Patterns.FLOATING_HOLIDAY);
	}

	@Override
	Holiday createHoliday(final Matcher matcher) {
		final DayOfWeek dayOfWeek = dayOfWeek(matcher.group(Patterns.DAY_OF_WEEK_GROUP));
		if (dayOfWeek == null) {
			return null;
		}

		final String category = matcher.group(Patterns.CATEGORY_GROUP);
		final String name = matcher.group(Patterns.NAME_GROUP);
		final int offset = Integer.parseInt(matcher.group(Patterns.OFFSET_GROUP));
		final Direction direction = Direction.parse(matcher.group(Patterns.DIRECTION_GROUP));
		final int month = monthNumber(matcher.group(Patterns.MONTH_GROUP));
		final String day = matcher.group(Patterns.DAY_GROUP);

		if (Patterns.LAST_DAY.equals(day)) {
			return new FloatingHoliday(category, name, offset, dayOfWeek, direction, month, Day.LAST);
		} else {
			return new FloatingHoliday(category, name, offset, dayOfWeek, direction,
					MonthDay.of(month, Integer.parseInt(day)));
		}
	}

	static class OffsetMatcher extends HolidayMatcher {
		OffsetMatcher() {
			super(new FloatingDateMatcher(), Patterns.FLOATING_HOLIDAY_WITH_OFFSET_IN_DAYS);
		}

		@Override
		Holiday createHoliday(final Matcher matcher) {
			final Direction direction = Direction.parse(matcher.group(Patterns.DIRECTION_GROUP_2));
			final int offset = Integer.parseInt(matcher.group(Patterns.OFFSET_GROUP_2));
			return createOriginalHoliday(matcher).withOffsetInDays(direction == Direction.BEFORE ? -offset : offset);
		}
	}
}
