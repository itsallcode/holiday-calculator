package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;
import java.time.Year;
import java.util.Objects;

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;

/**
 * This class represents a conditional holiday that only applies if the
 * specified condition is met or that might occur on an alternative date in case
 * the condition is met.
 */
public class ConditionalHoliday extends Holiday {

	private final Condition condition;
	private final Holiday other;

	/**
	 * Create a new instance of the conditional holiday.
	 *
	 * @param conditionBuilder builder for the condition
	 * @param holiday          the original holiday to create the condition holiday
	 *                         upon
	 */
	public ConditionalHoliday(final ConditionBuilder conditionBuilder, final Holiday holiday) {
		super(holiday.getCategory(), holiday.getName());
		this.condition = conditionBuilder.build();
		this.other = holiday;
	}

	@Override
	public LocalDate of(final int year) {
		if (!condition.applies(Year.of(year))) {
			return null;
		}
		return other.of(year);
	}

	@Override
	public String toString() {
		return other.toString(condition.toString(" only "));
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + Objects.hash(condition, other);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ConditionalHoliday otherHoliday = (ConditionalHoliday) obj;
		return Objects.equals(condition, otherHoliday.condition) && Objects.equals(this.other, otherHoliday.other);
	}
}
