package org.itsallcode.holidays.calculator.logic.conditions.builder;

import java.time.DayOfWeek;
import java.time.MonthDay;

import org.itsallcode.holidays.calculator.logic.conditions.Condition;
import org.itsallcode.holidays.calculator.logic.conditions.DayOfWeekCondition;
import org.itsallcode.holidays.calculator.logic.conditions.NegatedCondition;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

public class ConditionBuilder {

	public static class UnspecifiedPivotDateException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UnspecifiedPivotDateException(String message) {
			super(message);
		}
	}

	private boolean negated = false;
	private DayOfWeek[] daysOfWeek;
	private MonthDay pivotDate;

	public Condition build() {
		if (pivotDate == null) {
			throw new UnspecifiedPivotDateException("Cannot build DayOfWeekCondition with unspecified pivot date.");
		}
		final DayOfWeekCondition condition = new DayOfWeekCondition(pivotDate, daysOfWeek);
		if (negated) {
			return new NegatedCondition(condition);
		} else {
			return condition;
		}
	}

	public ConditionBuilder withDaysOfWeek(DayOfWeek... daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
		return this;
	}

	public ConditionBuilder withPivotDate(MonthDay monthDay) {
		this.pivotDate = monthDay;
		return this;
	}

	public ConditionBuilder withOptionalPivotDateFrom(Holiday holiday) {
		if (pivotDate == null) {
			this.pivotDate = holiday.getMonthDay();
		}
		return this;
	}

	public ConditionBuilder negated() {
		this.negated = !this.negated;
		return this;
	}

}
