package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;

import org.itsallcode.holidays.calculator.logic.Easter;

/**
 * An instance of this class represents a holiday based on the Easter feast, see
 * https://en.wikipedia.org/wiki/Easter.
 */
public class EasterBasedHoliday extends PivotDateBasedHoliday {

	/**
	 * Create a new instance of an Easter-based holiday.
	 *
	 * @param category     category of the holiday, e.g. "Birthday"
	 * @param name         name of the holiday
	 * @param offsetInDays Offset in days between the Easter feast and the holiday.
	 *                     A positive offset indicates the holiday being after the
	 *                     Easter feast.
	 */
	public EasterBasedHoliday(final String category, final String name, final int offsetInDays) {
		super("Easter", category, name, offsetInDays);
	}

	@Override
	public LocalDate of(final int year) {
		return Easter.gauss(year).plusDays(offsetInDays);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return getClass() == obj.getClass();
	}
}
