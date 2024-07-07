package org.itsallcode.holidays.calculator.logic.parser;

import java.util.HashMap;

/**
 * This class enables to parse enum values from potentially abbreviated strings.
 *
 * @param <T> Enum class containing the values to be parsed from the string.
 */
public class AbbreviationParser<T extends Enum<T>> {

	/**
	 * Exception indicating an ambiguous abbreviation for the name of a month or day
	 * of the week, e.g. "T" for the day of the week that could either mean
	 * "Tuesday" or "Thursday".
	 */
	public static class AmbigueAbbreviationException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		/**
		 * Create a new instance of this exception.
		 *
		 * @param message message of the exception
		 */
		public AmbigueAbbreviationException(String message) {
			super(message);
		}
	}

	/**
	 * Exception indicating an illegal abbreviation for a the name of a month or day
	 * of the week. E.g. "D" for a day of the week.
	 */
	public static class InvalidAbbreviationException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		/**
		 * Create a new instance of this exception.
		 *
		 * @param message message of the exception
		 */
		public InvalidAbbreviationException(String message) {
			super(message);
		}
	}

	private final Class<T> clazz;
	private final HashMap<String, T> cache = new HashMap<>();

	/**
	 * Create a new instance for the specified enum class.
	 *
	 * @param clazz enum class to create the abbreviation parser for
	 */
	public AbbreviationParser(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Return an enum value for the specified prefix (e.g. abbreviation).
	 *
	 * @param prefix prefix to search a unique enum value for
	 * @return enum value for the specified prefix
	 */
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
