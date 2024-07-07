package org.itsallcode.holidays.calculator.logic.variants;

import java.util.Objects;

import org.itsallcode.holidays.calculator.logic.Formatter;

/**
 * Each instance of this class represents a holiday based on a pivot date and
 * deviating from the pivot date by a fixed number of days.
 */
public abstract class PivotDateBasedHoliday extends Holiday {

	/** Name of the pivot date */
	protected final String pivotDateName;
	/**
	 * Offset in days between the pivot date and the holiday. A positive offset
	 * indicated the holiday being after the pivot date.
	 */
	protected final int offsetInDays;

	/**
	 * Constructors for a new instance of {@link PivotDateBasedHoliday}.
	 *
	 * @param pivotDateName Name of the pivot date
	 * @param category      Category of the holiday, e.g. "birthday"
	 * @param name          Name of the holiday
	 * @param offsetInDays  Offset in days between the pivot date and the holiday. A
	 *                      positive offset indicates the holiday being after the
	 *                      pivot date.
	 */
	protected PivotDateBasedHoliday(String pivotDateName, String category, String name, int offsetInDays) {
		super(category, name);
		this.offsetInDays = offsetInDays;
		this.pivotDateName = pivotDateName;
	}

	@Override
	public String toString() {
		return String.format("%s(%s %s: %s %s)",
				this.getClass().getSimpleName(),
				getCategory(),
				getName(),
				Formatter.offset(offsetInDays),
				pivotDateName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(offsetInDays, pivotDateName);
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
		final PivotDateBasedHoliday other = (PivotDateBasedHoliday) obj;
		return offsetInDays == other.offsetInDays && Objects.equals(pivotDateName, other.pivotDateName);
	}
}
