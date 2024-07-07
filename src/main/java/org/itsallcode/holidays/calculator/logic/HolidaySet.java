package org.itsallcode.holidays.calculator.logic;

import java.time.LocalDate;
import java.util.*;

import org.itsallcode.holidays.calculator.logic.variants.Holiday;

/**
 * Hosts a personal set of holidays typically defined in a configuration file.
 * Each holiday is meant to repeat every year and may have
 *
 * <ul>
 * <li>a fixed date identical for every year</li>
 * <li>a floating date defined by a specific date in each year and an offset
 * restricted to a particular day of the week, e.g. fourth Sunday before
 * Christmas</li>
 * <li>a date defined relatively to Easter Sunday with a positive or negative
 * offset of days</li>
 * </ul>
 */
public class HolidaySet {

	final List<Holiday> definitions = new ArrayList<>();

	// caches
	private final Set<Integer> years = new HashSet<>();
	private final HashMap<LocalDate, List<Holiday>> holidayInstances = new HashMap<>();

	/**
	 * Create a new set of holidays from list of holiday definitions.
	 *
	 * @param definitions list of holidays to include into the holiday set
	 */
	public HolidaySet(final Collection<Holiday> definitions) {
		this.definitions.addAll(definitions);
	}

	/**
	 * Return List of holidays occurring on the given date.
	 *
	 * @param date date
	 * @return List of holidays occurring on the given date. If there is no holiday
	 *         on given date, then list is empty.
	 */
	public List<Holiday> instances(final LocalDate date) {
		cacheHolidays(date.getYear());

		final List<Holiday> instances = holidayInstances.get(date);
		if (instances == null) {
			return Collections.<Holiday>emptyList();
		}
		return instances;
	}

	private void cacheHolidays(final int year) {
		if (years.contains(year)) {
			return;
		}

		for (final Holiday holiday : definitions) {
			final LocalDate date = holiday.of(year);
			final List<Holiday> entry = holidayInstances.computeIfAbsent(date, d -> new ArrayList<>());
			entry.add(holiday);
		}
		years.add(year);
	}

	List<Holiday> getDefinitions() {
		return definitions;
	}

}
