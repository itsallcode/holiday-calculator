package org.itsallcode.holidays.calculator.logic.conditions.builder;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.List;

import org.itsallcode.holidays.calculator.logic.conditions.*;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

/**
 * Builder for conditions.
 *
 * Each instance of {@link Holiday} may have a condition component. The holiday
 * instance then can create a concrete condition using the builder and providing
 * a specific year. When evaluating the condition for the specific year the
 * holiday may then choose an alternative, e.g. if the holiday would fall onto a
 * Sunday in this year.
 */
public class ConditionBuilder {

	private boolean negated = false;
	private DayOfWeek[] daysOfWeek;
	private MonthDay pivotDate;

	/**
	 * Create a new instance.
	 */
	public ConditionBuilder() {
		// intentionally empty
	}

	/**
	 * Build a condition from the current builder.
	 *
	 * @return the built condition
	 */
	public Condition build() {
		if (pivotDate == null) {
			throw new IllegalStateException("Cannot build DayOfWeekCondition with unspecified pivot date.");
		}
		final DayOfWeekCondition condition = new DayOfWeekCondition(pivotDate, daysOfWeek);
		if (negated) {
			return new NegatedCondition(condition);
		} else {
			return condition;
		}
	}

	/**
	 * Add a specification to the builder regarding the specified days of the week.
	 *
	 * The condition could for example then be {@code true} only on these days of
	 * the week.
	 *
	 * @param daysOfWeek days of the week.
	 * @return self for fluent programming
	 */
	public ConditionBuilder withDaysOfWeek(final List<DayOfWeek> daysOfWeek) {
		this.daysOfWeek = daysOfWeek.toArray(DayOfWeek[]::new);
		return this;
	}

	/**
	 * Add a specification to the builder regarding a pivot date. Additional days of
	 * the week could then be evaluated for the pivot date rather than the holiday
	 * itself.
	 *
	 * @param monthDay month and day
	 * @return self for fluent programming
	 */
	public ConditionBuilder withPivotDate(final MonthDay monthDay) {
		this.pivotDate = monthDay;
		return this;
	}

	/**
	 * Add a specification to the builder regarding an optional pivot date. The
	 * builder uses the optional pivot date only if a (regular) pivot date has not
	 * already been specified.
	 *
	 * @param holiday holiday to use as optional pivot date
	 * @return self for fluent programming
	 */
	public ConditionBuilder withOptionalPivotDateFrom(final Holiday holiday) {
		if (pivotDate == null) {
			this.pivotDate = holiday.getMonthDay();
		}
		return this;
	}

	/**
	 * Add a specification to the builder to negate the condition that is specified
	 * otherwise.
	 *
	 * @return self for fluent programming
	 */
	public ConditionBuilder negated() {
		this.negated = !this.negated;
		return this;
	}
}
