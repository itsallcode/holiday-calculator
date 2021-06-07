package org.itsallcode.holidays.calculator.logic.conditions;

import static java.util.stream.Collectors.joining;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.itsallcode.holidays.calculator.logic.Formatter;

/**
 * Example
 *
 * <pre>
 * if DEC 26 is SAT,SUN
 * </pre>
 */
public class DayOfWeekCondition extends Condition {

	private final MonthDay pivot;
	private final Set<DayOfWeek> daysOfWeek = new HashSet<>();

	public DayOfWeekCondition(MonthDay pivot, DayOfWeek... daysOfWeek) {
		super(false);
		this.pivot = pivot;
		this.daysOfWeek.addAll(Arrays.asList(daysOfWeek));
	}

	@Override
	public boolean applies(Year year) {
		final boolean result = daysOfWeek.contains(year.atMonthDay(pivot).getDayOfWeek());
		return (negate ? !result : result);
	}

	@Override
	protected Condition copy() {
		final DayOfWeekCondition result = new DayOfWeekCondition(pivot, daysOfWeek.toArray(new DayOfWeek[0]));
		result.negate = negate;
		return result;
	}

	@Override
	public String toString() {
		final String days = Arrays.asList(daysOfWeek.toArray(new DayOfWeek[0]))
				.stream()
				.map(Formatter::format)
				.collect(joining(","));
		return String.format(" only if %s is%s %s", Formatter.format(pivot), (negate ? " not" : ""), days);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((daysOfWeek == null) ? 0 : daysOfWeek.hashCode());
		result = prime * result + ((pivot == null) ? 0 : pivot.hashCode());
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
		final DayOfWeekCondition other = (DayOfWeekCondition) obj;
		if (daysOfWeek == null) {
			if (other.daysOfWeek != null) {
				return false;
			}
		} else if (!daysOfWeek.equals(other.daysOfWeek)) {
			return false;
		}
		if (pivot == null) {
			if (other.pivot != null) {
				return false;
			}
		}
		return (pivot.equals(other.pivot));
	}
}
