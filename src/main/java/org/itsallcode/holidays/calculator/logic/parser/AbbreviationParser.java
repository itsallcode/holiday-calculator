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
package org.itsallcode.holidays.calculator.logic.parser;

import java.util.HashMap;

public class AbbreviationParser<T extends Enum<T>> {

	public static class AmbigueAbbreviationException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AmbigueAbbreviationException(String message) {
			super(message);
		}
	}

	public static class InvalidAbbreviationException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public InvalidAbbreviationException(String message) {
			super(message);
		}
	}

	private final Class<T> clazz;
	private final HashMap<String, T> cache = new HashMap<>();

	public AbbreviationParser(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T getEnumFor(final String prefix) {
		if (cache.containsKey(prefix)) {
			return cache.get(prefix);
		}

		String upper = "";
		if (prefix != null) {
			upper = prefix.toUpperCase();
		}

		T result = null;
		for (final T value : clazz.getEnumConstants()) {
			if (value.toString().toUpperCase().startsWith(upper)) {
				if (result != null) {
					throw new AmbigueAbbreviationException(prefix);
				}
				result = value;
			}
		}

		if (result == null) {
			throw new InvalidAbbreviationException("Could not find any " + clazz.getSimpleName()
					+ " for abbreviation \"" + prefix + "\"");
		}

		cache.put(prefix, result);
		return result;
	}

}
