package org.itsallcode.holidays.calculator.logic;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday.Day;

public class Formatter {

	private static final DateTimeFormatter DAY_OF_WEEK_FORMATTER_SHORT = DateTimeFormatter.ofPattern("EEE", Locale.US);
	private static final DateTimeFormatter DAY_OF_WEEK_FORMATTER_LONG = DateTimeFormatter.ofPattern("EEEE", Locale.US);
	private static final DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d", Locale.US);
	private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM", Locale.US);

	public static String format(DayOfWeek dayOfWeek) {
		return DAY_OF_WEEK_FORMATTER_SHORT.format(dayOfWeek);
	}

	public static String formatLong(DayOfWeek dayOfWeek) {
		return DAY_OF_WEEK_FORMATTER_LONG.format(dayOfWeek);
	}

	public static String format(MonthDay monthDay) {
		return MONTH_DAY_FORMATTER.format(monthDay).toUpperCase();
	}

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

	public static String offset(int offset) {
		return String.format("%d day%s %s",
				Math.abs(offset),
				(Math.abs(offset) == 1 ? "" : "s"),
				(offset < 0 ? "before" : "after"));
	}

	public static String ordinal(int offset) {
		final String[] suffix = { "", "st", "nd", "rd" };
		return String.format("%d%s", offset,
				(offset < 4 ? suffix[offset] : "th"));
	}

	private Formatter() {
	}
}
