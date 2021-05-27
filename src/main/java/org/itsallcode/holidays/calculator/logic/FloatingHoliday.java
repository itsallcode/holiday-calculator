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
package org.itsallcode.holidays.calculator.logic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.itsallcode.holidays.calculator.logic.parser.HolidayParser;

public class FloatingHoliday extends Holiday {
	public enum Direction {
		BEFORE, AFTER;

		static public Direction parse(String s) {
			return valueOf(s.toUpperCase());
		}
	}

	public static final int LAST_DAY_OF_THE_MONTH = -1;

	private final int offset;
	private final DayOfWeek dayOfWeek;
	private final Direction direction;
	private final int month;
	private final int day;

	/**
	 * Holiday called &lt;name&gt; on the &lt;offset&gt; &lt;dayOfWeek&gt;
	 * after/before &lt;month&gt; &lt;day&gt;.
	 *
	 * @param category  category
	 * @param name      name
	 * @param offset    offset
	 * @param dayOfWeek day of week
	 * @param direction direction
	 * @param month     Month
	 * @param day       Number of pivot day in given month or using constant
	 *                  LAST_LAY_OF_MONTH.
	 */
	public FloatingHoliday(String category, String name, int offset,
			DayOfWeek dayOfWeek, Direction direction, int month, int day) {
		super(category, name);
		if (offset < 0) {
			throw new IllegalArgumentException("Argument offset must be >= 0, but was " + offset);
		}
		this.offset = offset;
		this.dayOfWeek = dayOfWeek;
		if (day < 1 && day != LAST_DAY_OF_THE_MONTH) {
			throw new IllegalArgumentException(
					"Argument day must be > 0 or equal to LAST_DAY_OF_THE_MONTH, but was " + day);
		}
		this.direction = direction;
		this.day = day;
		this.month = month;
		if (this.day > 0) {
			ensureValidDate(month, this.day);
		}
	}

	@Override
	public LocalDate of(int year) {
		final LocalDate pivotDay = pivotDay(year).with(TemporalAdjusters.previousOrSame(dayOfWeek));
		final int delta = (direction == Direction.AFTER ? offset - 1 : 1 - offset);
		return pivotDay.plusDays(7 * delta);
	}

	private LocalDate pivotDay(int year) {
		if (direction == Direction.AFTER) {
			return LocalDate.of(year, month, day).plusDays(6);
		}

		if (day == LAST_DAY_OF_THE_MONTH) {
			return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
		}

		return LocalDate.of(year, month, day);
	}

	@Override
	public String toString() {
		return String.format("%s(%s %s: %d%s %s %s %02d-%s)",
				this.getClass().getSimpleName(), getCategory(), getName(),
				offset, ordinal(offset), capitalize(dayOfWeek),
				direction.toString().toLowerCase(), month, dayAsString(this.day));
	}

	private String ordinal(int offset) {
		switch (offset) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}

	private String capitalize(DayOfWeek dayOfWeek) {
		if (dayOfWeek == null) {
			return "";
		}

		final String str = dayOfWeek.toString();
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	private String dayAsString(int day) {
		if (day == LAST_DAY_OF_THE_MONTH) {
			return HolidayParser.LAST_DAY;
		}

		return String.format("%02d", day);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + day;
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + month;
		result = prime * result + offset;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (!super.equals(obj))
			return false;
		final FloatingHoliday other = (FloatingHoliday) obj;
		if (day != other.day)
			return false;
		if (dayOfWeek != other.dayOfWeek)
			return false;
		if (direction != other.direction)
			return false;
		if (month != other.month)
			return false;
		if (offset != other.offset)
			return false;
		return true;
	}
}
