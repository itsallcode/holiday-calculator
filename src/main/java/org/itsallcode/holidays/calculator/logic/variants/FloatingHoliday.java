/**
 * holiday-calculator
 * Copyright (C) 2022 itsallcode <github@kuhnke.net>
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
package org.itsallcode.holidays.calculator.logic.variants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

import org.itsallcode.holidays.calculator.logic.Formatter;

/**
 * Class for floating holidays, e.g. first Monday in March.
 */
public class FloatingHoliday extends Holiday {

	/**
	 * Indicates the direction of an offset for the floating holiday from a specific
	 * pivot date.
	 */
	public enum Direction {
		/** The floating day is after the pivot date. */
		BEFORE,
		/** The floating day is before the pivot date. */
		AFTER;

		/**
		 * Parse the direction from the specified string.
		 *
		 * @param s string to parse the direction from
		 * @return direction parsed from the string
		 */
		public static Direction parse(String s) {
			return valueOf(s.toUpperCase());
		}
	}

	/**
	 * Enables to refer to specific days in a month using colloquial phrases like
	 * "first" or "last day of the month".
	 */
	public enum Day {
		/**
		 * Format the number of the day of month as specified, i.e. in a numeric way.
		 */
		AS_SPECIFIED,
		/** Use the first day of the month */
		FIRST,
		/** Replace the number of the day of the month by "last-day" */
		LAST;
	}

	private final int offset;
	private final DayOfWeek dayOfWeek;
	private final Direction direction;
	private final MonthDay monthDay;
	private final Day dayInterpretation;

	/**
	 * Holiday called &lt;name&gt; on the &lt;offset&gt; &lt;dayOfWeek&gt;
	 * after/before first or last day of &lt;month&gt;.
	 *
	 * @param category          category
	 * @param name              name
	 * @param offset            offset
	 * @param dayOfWeek         day of week
	 * @param direction         direction
	 * @param month             number of month
	 * @param dayInterpretation FIRST, LAST or AS_SPECIFIED
	 */
	public FloatingHoliday(String category, String name, int offset, DayOfWeek dayOfWeek, Direction direction,
			int month, Day dayInterpretation) {
		this(category, name, offset, dayOfWeek, direction, MonthDay.of(month, 1), dayInterpretation);
	}

	/**
	 * Holiday called &lt;name&gt; on the &lt;offset&gt; &lt;dayOfWeek&gt;
	 * after/before pivot date &lt;monthDay&gt;.
	 *
	 * @param category  category
	 * @param name      name
	 * @param offset    offset
	 * @param dayOfWeek day of week
	 * @param direction direction
	 * @param monthDay  pivot date
	 */
	public FloatingHoliday(String category, String name, int offset, DayOfWeek dayOfWeek, Direction direction,
			MonthDay monthDay) {
		this(category, name, offset, dayOfWeek, direction, monthDay, Day.AS_SPECIFIED);
	}

	private FloatingHoliday(String category, String name, int offset, DayOfWeek dayOfWeek, Direction direction,
			MonthDay monthDay, Day dayInterpretation) {
		super(category, name);

		if (offset < 0) {
			throw new IllegalArgumentException("Argument offset must be >= 0, but was " + offset);
		}
		this.offset = offset;
		this.dayOfWeek = dayOfWeek;
		this.direction = direction;
		this.dayInterpretation = dayInterpretation;
		this.monthDay = monthDay;
	}

	@Override
	public LocalDate of(int year) {
		final LocalDate pivotDay = pivotDay(year).with(TemporalAdjusters.previousOrSame(dayOfWeek));
		final int delta = (direction == Direction.AFTER ? offset - 1 : 1 - offset);
		return pivotDay.plusDays(7L * delta + offsetInDays);
	}

	private LocalDate pivotDay(int year) {
		if (direction == Direction.AFTER) {
			return monthDay.atYear(year).plusDays(6);
		}

		if (dayInterpretation == Day.LAST) {
			return monthDay.atYear(year).with(TemporalAdjusters.lastDayOfMonth());
		}

		return monthDay.atYear(year);
	}

	@Override
	public String toString() {
		return String.format("%s(%s %s: %s%s %s %s %s)",
				this.getClass().getSimpleName(),
				getCategory(),
				getName(),
				Formatter.offsetWithoutZero(offsetInDays),
				Formatter.ordinal(offset),
				Formatter.formatLong(dayOfWeek),
				direction.toString().toLowerCase(),
				Formatter.format(monthDay, dayInterpretation));
	}

	@Override
	public MonthDay getMonthDay() {
		return this.monthDay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dayInterpretation, dayOfWeek, direction, monthDay, offset);
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
		final FloatingHoliday other = (FloatingHoliday) obj;
		return dayInterpretation == other.dayInterpretation && dayOfWeek == other.dayOfWeek
				&& direction == other.direction && Objects.equals(monthDay, other.monthDay) && offset == other.offset;
	}

}
