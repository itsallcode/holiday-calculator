package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;

import org.itsallcode.holidays.calculator.logic.Easter;

/**
 * An instance of this class represents a holiday based on the Orthodox Easter
 * feast, see https://en.wikipedia.org/wiki/Eastern_Orthodoxy.
 */
public class OrthodoxEasterBasedHoliday extends PivotDateBasedHoliday {

	/**
	 * Create a new instance of an {@link OrthodoxEasterBasedHoliday}
	 *
	 * @param category     Category of the holiday, e.g. "birthday"
	 * @param name         Name of the holiday
	 * @param offsetInDays Offset in days between the Orthodox Easter feast and the
	 *                     holiday. A positive offset indicates the holiday being
	 *                     after the Orthodox Easter feast.
	 */
	public OrthodoxEasterBasedHoliday(final String category, final String name, final int offsetInDays) {
		super("Orthodox Easter", category, name, offsetInDays);
	}

	@Override
	public LocalDate of(final int year) {
		return Easter.orthodox(year).plusDays(offsetInDays);
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
