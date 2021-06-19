/**
 * holiday-calculator
 * Copyright (C) 2021 itsallcode <github@kuhnke.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

	private MonthDay pivot;
	private final Set<DayOfWeek> daysOfWeek = new HashSet<>();

	public DayOfWeekCondition(DayOfWeek... daysOfWeek) {
		this(null, daysOfWeek);
	}

	public DayOfWeekCondition(MonthDay pivot, DayOfWeek... daysOfWeek) {
		this.pivot = pivot;
		this.daysOfWeek.addAll(Arrays.asList(daysOfWeek));
	}

	@Override
	public boolean applies(Year year) {
		return daysOfWeek.contains(year.atMonthDay(pivot).getDayOfWeek());
	}

	@Override
	public Condition withPivotDate(MonthDay pivot) {
		if (this.pivot == null) {
			this.pivot = pivot;
		}
		return this;
	}

	@Override
	public String toString(String prefix, boolean negated) {
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((daysOfWeek == null) ? 0 : daysOfWeek.hashCode());
		result = prime * result + ((pivot == null) ? 0 : pivot.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
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
