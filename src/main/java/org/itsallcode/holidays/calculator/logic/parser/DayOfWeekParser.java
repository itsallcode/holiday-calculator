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
package org.itsallcode.holidays.calculator.logic.parser;

import java.time.DayOfWeek;
import java.util.HashMap;

public class DayOfWeekParser {
	public static class AmbigueDayOfWeekAbbreviationException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AmbigueDayOfWeekAbbreviationException(String message) {
			super(message);
		}
	}

	HashMap<String, DayOfWeek> cache = new HashMap<>();

	public DayOfWeek getDayOfWeek(final String prefix) {
		if (cache.containsKey(prefix)) {
			return cache.get(prefix);
		}

		String upper = "";
		if (prefix != null) {
			upper = prefix.toUpperCase();
		}

		DayOfWeek result = null;
		for (final DayOfWeek day : DayOfWeek.values()) {
			if (day.toString().toUpperCase().startsWith(upper)) {
				if (result != null) {
					throw new AmbigueDayOfWeekAbbreviationException(prefix);
				}
				result = day;
			}
		}

		if (result != null) {
			cache.put(prefix, result);
		}

		return result;
	}

}
