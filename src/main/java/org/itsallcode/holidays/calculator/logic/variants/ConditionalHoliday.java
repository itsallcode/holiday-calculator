package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;
import java.time.Year;

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;

public class ConditionalHoliday extends Holiday {

	private final Condition condition;
	private final Holiday other;

	public ConditionalHoliday(ConditionBuilder conditionBuilder, Holiday holiday) {
		super(holiday.getCategory(), holiday.getName());
		this.condition = conditionBuilder.build();
		this.other = holiday;
	}

	@Override
	public LocalDate of(int year) {
		if (!condition.applies(Year.of(year))) {
			return null;
		}
		return other.of(year);
	}

	@Override
	public String toString() {
		return other.toString(condition.toString(" only "));
	}

}
