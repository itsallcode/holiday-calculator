package org.itsallcode.holidays.calculator.logic.parser.matcher;

import java.time.MonthDay;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;
import org.itsallcode.holidays.calculator.logic.variants.*;

class FixedDateMatcher extends HolidayMatcher {
	FixedDateMatcher() {
		super(Patterns.FIXED_HOLIDAY);
	}

	@Override
	FixedDateHoliday createHoliday(final Matcher matcher) {
		return new FixedDateHoliday(
				matcher.group(Patterns.CATEGORY_GROUP),
				matcher.group(Patterns.NAME_GROUP),
				monthDay(matcher, Patterns.MONTH_GROUP, Patterns.DAY_GROUP));
	}

	static class Conditional extends HolidayMatcher {
		Conditional() {
			super(new FixedDateMatcher(), Patterns.CONDITIONAL_FIXED_HOLIDAY);
		}

		@Override
		Holiday createHoliday(final Matcher matcher) {
			final ConditionBuilder conditionBuilder = createConditionBuilder(matcher) //
					.withPivotDate(monthDay(matcher, Patterns.MONTH_GROUP_2, Patterns.DAY_GROUP_2));
			return new ConditionalHoliday( //
					conditionBuilder,
					createOriginalHoliday(matcher));
		}
	}

	static class Alternative extends HolidayMatcher {
		private final boolean negated;

		Alternative(final Pattern pattern) {
			super(new FixedDateMatcher(), pattern);
			this.negated = (pattern == Patterns.ALTERNATIVE_DATE_HOLIDAY_NEGATED_DAY_OF_WEEK);
		}

		@Override
		Holiday createHoliday(final Matcher matcher) {
			final MonthDay originalDate = monthDay(matcher, Patterns.MONTH_GROUP, Patterns.DAY_GROUP);
			final ConditionBuilder conditionBuilder = createConditionBuilder(matcher).withPivotDate(originalDate);
			final MonthDay alternateDate = monthDay(matcher, Patterns.MONTH_GROUP_2, Patterns.DAY_GROUP_2);
			return new HolidayWithAlternative( //
					createOriginalHoliday(matcher),
					(negated ? conditionBuilder.negated() : conditionBuilder),
					alternateDate);
		}
	}
}
