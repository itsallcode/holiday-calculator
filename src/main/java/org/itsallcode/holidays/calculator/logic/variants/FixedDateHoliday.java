package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Objects;

import org.itsallcode.holidays.calculator.logic.Formatter;

/**
 * Class representing holidays that occur on a fixed date each year, e.g.
 * December the 24th.
 */
public class FixedDateHoliday extends Holiday {

	private final MonthDay monthDay;

	/**
	 * Create a new instance of a fixed date holiday.
	 *
	 * @param category category of the holiday
	 * @param name     name of the holiday
	 * @param monthDay month and day of the fixed-date holiday
	 */
	public FixedDateHoliday(String category, String name, MonthDay monthDay) {
		super(category, name);
		this.monthDay = monthDay;
	}

	/**
	 * Create a new instance of a fixed date holiday based on another holiday.
	 *
	 * @param other    other holiday to base the current onto
	 * @param monthDay month and day of the current holiday
	 */
	public FixedDateHoliday(Holiday other, MonthDay monthDay) {
		super(other.getCategory(), other.getName());
		this.monthDay = monthDay;
	}

	@Override
	public LocalDate of(int year) {
		return monthDay.atYear(year);
	}

	@Override
	public String toString(Holiday pivot) {
		if (pivot instanceof FixedDateHoliday
				&& pivot.getName().equals(getName())
				&& pivot.getCategory().equals(getCategory())) {
			return Formatter.format(monthDay);
		}
		return toString();
	}

	@Override
	public String toString(String condition) {
		return String.format("%s(%s %s: %s%s)", this.getClass().getSimpleName(),
				getCategory(),
				getName(),
				Formatter.format(monthDay),
				condition);
	}

	@Override
	public MonthDay getMonthDay() {
		return monthDay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(monthDay);
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
		final FixedDateHoliday other = (FixedDateHoliday) obj;
		return Objects.equals(monthDay, other.monthDay);
	}

}
