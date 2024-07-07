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
package org.itsallcode.holidays.calculator.logic;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Day;

/**
 * Formatter for dates in a colloquial way.
 */
public class Formatter {

	private static final DateTimeFormatter DAY_OF_WEEK_FORMATTER_SHORT = DateTimeFormatter.ofPattern("EEE", Locale.US);
	private static final DateTimeFormatter DAY_OF_WEEK_FORMATTER_LONG = DateTimeFormatter.ofPattern("EEEE", Locale.US);
	private static final DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d", Locale.US);
	private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM", Locale.US);

	/**
	 * Format the given day of week in a condensed colloquial way.
	 *
	 * @param dayOfWeek day of the week
	 * @return string representing the specified date in a colloquial way.
	 */
	public static String format(DayOfWeek dayOfWeek) {
		return DAY_OF_WEEK_FORMATTER_SHORT.format(dayOfWeek);
	}

	/**
	 * Format the given day of week in a verbose colloquial way.
	 *
	 * @param dayOfWeek day of the week
	 * @return string representing the specified date in a colloquial way.
	 */
	public static String formatLong(DayOfWeek dayOfWeek) {
		return DAY_OF_WEEK_FORMATTER_LONG.format(dayOfWeek);
	}

	/**
	 * Format the given month and day in a colloquial way.
	 *
	 * @param monthDay date to be formatted
	 * @return string representing the specified date in a colloquial way.
	 *
	 */
	public static String format(MonthDay monthDay) {
		return MONTH_DAY_FORMATTER.format(monthDay).toUpperCase();
	}

	/**
	 * Format the given month and (optional) day in a colloquial way using a special
	 * interpretation for the first or last day of the month. The day component of
	 * parameter {@code monthDay} may be optional in some cases.
	 *
	 * @param monthDay          date to be formatted
	 * @param dayInterpretation if {@link Day#LAST} then replace specific number of
	 *                          the day of the month by "last-day". for
	 *                          {@link Day#FIRST} the day may be omitted in argument
	 *                          {@code monthDay}
	 * @return string representing the specified date in a colloquial way.
	 */
	public static String format(MonthDay monthDay, Day dayInterpretation) {
		switch (dayInterpretation) {
		case LAST:
			return MONTH_FORMATTER.format(monthDay.getMonth()).toUpperCase() + " last-day";
		case FIRST:
			return MONTH_DAY_FORMATTER.format(monthDay.withDayOfMonth(1));
		default:
			return MONTH_DAY_FORMATTER.format(monthDay).toUpperCase();
		}
	}

	/**
	 * Return string specifying the offset in days in human readable form. If
	 * {@code offset == 0} return an empty string.
	 *
	 * @param offset offset in days
	 * @return string specifying the offset in human readable form. Negative values
	 *         are rendered using the term "before", positive values will be
	 *         indicated by the string "after".
	 */
	public static String offset(int offset) {
		return String.format("%d day%s %s",
				Math.abs(offset),
				(Math.abs(offset) == 1 ? "" : "s"),
				(offset < 0 ? "before" : "after"));
	}

	/**
	 * Return string specifying the offset in days in human readable form. If
	 * {@code offset == 0} return an empty string.
	 *
	 * @param offset offset in days
	 * @return string specifying the offset in human readable form
	 */
	public static String offsetWithoutZero(int offset) {
		return (offset == 0 ? "" : offset(offset) + " ");
	}

	/**
	 * Return a string representing the ordinal variant of the given integer
	 * {@code offset}. E.g. 1 becomes "1st", 2 becomes "2nd", etc.
	 *
	 * @param offset integer value to get the ordinal form for
	 * @return ordinal form for the provided {@code offset}.
	 */
	public static String ordinal(int offset) {
		final String[] suffix = { "", "st", "nd", "rd" };
		return String.format("%d%s", offset,
				(offset < 4 ? suffix[offset] : "th"));
	}

	private Formatter() {
		// prevent instantiation
	}
}
