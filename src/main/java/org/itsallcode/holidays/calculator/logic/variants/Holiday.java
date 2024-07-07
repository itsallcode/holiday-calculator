package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Objects;

/**
 * Abstract class representing a holiday.
 */
public abstract class Holiday {

	/**
	 * Create an instance of this holiday for the specified year.
	 *
	 * @param year year to instantiate the holiday for
	 * @return instance of this holiday for the specified year
	 */
	public abstract LocalDate of(int year);

	private final String category;
	private final String name;

	/**
	 * Offset in days between the pivot date and the holiday. A positive offset
	 * indicates the holiday being after the pivot date.
	 */
	protected int offsetInDays = 0;

	/**
	 * Create a new instance of this holiday.
	 *
	 * @param category Arbitrary category that may be evaluated by the application
	 *                 processing the holiday.
	 * @param name     Name of holiday.
	 */
	protected Holiday(String category, String name) {
		this.category = category;
		this.name = name;
	}

	/**
	 * Get month and day of the holiday. Must be overridden by subclasses if
	 * applicable.
	 *
	 * @return {@link MonthDay} instance of the current holiday
	 */
	public MonthDay getMonthDay() {
		return null;
	}

	/**
	 * Specify the offset in days of the holiday relative to the pivot date.
	 *
	 * @param offsetInDays Offset in days between the pivot date and the holiday. A
	 *                     positive offset indicates the holiday being after the
	 *                     pivot date.
	 * @return self for fluent programming
	 */
	public Holiday withOffsetInDays(int offsetInDays) {
		this.offsetInDays = offsetInDays;
		return this;
	}

	/**
	 * Get the category of this holiday.
	 *
	 * @return category of the holiday
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Get the name of this holiday.
	 *
	 * @return name of the holiday
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return toString("");
	}

	/**
	 * Return a string for the current holiday including the specified pivot date.
	 *
	 * @param pivot pivot holiday, for which the current holiday is an alternative
	 * @return string representation of current holiday as alternative to pivot
	 *         holiday
	 */
	protected String toString(Holiday pivot) {
		return "";
	}

	/**
	 * Return a string for the current holiday including the specified condition.
	 *
	 * @param condition string representation of the condition applying for the
	 *                  current holiday.
	 * @return string representation of the current holiday including the specified
	 *         condition
	 */
	protected String toString(String condition) {
		return "";
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, name, offsetInDays);
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
		final Holiday other = (Holiday) obj;
		return Objects.equals(category, other.category) && Objects.equals(name, other.name)
				&& offsetInDays == other.offsetInDays;
	}

}
