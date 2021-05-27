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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

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
public class HolidayService {
	final List<Holiday> definitions = new ArrayList<>();
	// caches
	Set<Integer> years = new HashSet<>();
	Hashtable<LocalDate, List<Holiday>> holidayInstances = new Hashtable<>();

	public HolidayService(final List<Holiday> list) {
		definitions.addAll(list);
	}

	/**
	 * @param date date
	 * @return List of holidays occurring on the given date. If there is no holiday
	 *         on given date, then list is empty.
	 */
	public List<Holiday> getHolidays(LocalDate date) {
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
			List<Holiday> entry = holidayInstances.get(date);
			if (entry == null) {
				entry = new ArrayList<>();
				holidayInstances.put(date, entry);
			}
			entry.add(holiday);
		}
		years.add(year);
	}

	List<Holiday> getDefinitions() {
		return definitions;
	}

}