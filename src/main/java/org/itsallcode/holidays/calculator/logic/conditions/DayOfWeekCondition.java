package org.itsallcode.holidays.calculator.logic.conditions;

import static java.util.stream.Collectors.joining;

import java.time.*;
import java.util.*;

import org.itsallcode.holidays.calculator.logic.Formatter;

/**
 * This class represents a condition based on the day of week.
 *
 * Example
 *
 * <pre>
 * if DEC 26 is SAT,SUN
 * </pre>
 */
public class DayOfWeekCondition extends Condition {

	private MonthDay pivot;
	private final Set<DayOfWeek> daysOfWeek = new HashSet<>();

	/**
	 * Create a new condition that is {@code true} on the specified days of the
	 * week.
	 *
	 * @param daysOfWeek days of the week on which the current condition shall be
	 *                   {@code true}.
	 */
	public DayOfWeekCondition(final DayOfWeek... daysOfWeek) {
		this(null, daysOfWeek);
	}

	/**
	 * Create a new condition that is {@code true} if the specified pivot date is on
	 * one of the specified days of the week.
	 *
	 * @param pivot      pivot date
	 * @param daysOfWeek days of the week
	 */
	public DayOfWeekCondition(final MonthDay pivot, final DayOfWeek... daysOfWeek) {
		this.pivot = pivot;
		this.daysOfWeek.addAll(Arrays.asList(daysOfWeek));
	}

	@Override
	public boolean applies(final Year year) {
		return daysOfWeek.contains(year.atMonthDay(pivot).getDayOfWeek());
	}

	@Override
	public Condition withPivotDate(final MonthDay pivot) {
		if (this.pivot == null) {
			this.pivot = pivot;
		}
		return this;
	}

	@Override
	public String toString(final String prefix, final boolean negated) {
		final String days = Arrays.asList(daysOfWeek.toArray(new DayOfWeek[0]))
				.stream()
				.sorted(DayOfWeek::compareTo)
				.map(Formatter::format)
				.collect(joining(","));
		return String.format("%sif %s is%s %s",
				prefix,
				Formatter.format(pivot),
				(negated ? " not" : ""),
				days);
	}

	@Override
	public int hashCode() {
		return Objects.hash(daysOfWeek, pivot);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DayOfWeekCondition other = (DayOfWeekCondition) obj;
		return Objects.equals(daysOfWeek, other.daysOfWeek) && Objects.equals(pivot, other.pivot);
	}
}
