package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.builder.ConditionBuilder;

public class HolidayWithAlternative extends Holiday {

	private final Holiday defaultHoliday;
	private final Condition condition;
	private final Holiday alternative;

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
}
