package org.itsallcode.holidays.calculator.logic.variants;

import java.time.*;
import java.util.Objects;

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;

/**
 * Represents a holiday with a default date and an alternative date that applies
 * if the specified condition is {@code true} for a particular year.
 */
public class HolidayWithAlternative extends Holiday {

	private final Holiday defaultHoliday;
	private final Condition condition;
	private final Holiday alternative;

	/**
	 * Create a new instance of an alternative holiday.
	 *
	 * @param defaultHoliday   Default holiday if {@code condition} is {@code false}
	 *                         for a particular year.
	 * @param conditionBuilder Builder for the condition in a given year.
	 * @param alternateDate    Alternative date that applies if the specified
	 *                         {@code condition} is {@code true} for a particular
	 *                         year.
	 */
	public HolidayWithAlternative(Holiday defaultHoliday, ConditionBuilder conditionBuilder, MonthDay alternateDate) {
		super(defaultHoliday.getCategory(), defaultHoliday.getName());
		this.defaultHoliday = defaultHoliday;
		this.condition = conditionBuilder.withOptionalPivotDateFrom(defaultHoliday).build();
		this.alternative = new FixedDateHoliday(defaultHoliday, alternateDate);
	}

	@Override
	public LocalDate of(int year) {
		if (condition.applies(Year.of(year))) {
			return alternative.of(year);
		} else {
			return defaultHoliday.of(year);
		}
	}

	@Override
	public String toString() {
		return defaultHoliday.toString(""
				+ " or " + condition.toString()
				+ " then " + alternative.toString(defaultHoliday));
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + Objects.hash(alternative, condition, defaultHoliday);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HolidayWithAlternative other = (HolidayWithAlternative) obj;
		return Objects.equals(alternative, other.alternative) && Objects.equals(condition, other.condition)
				&& Objects.equals(defaultHoliday, other.defaultHoliday);
	}
}
