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

import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.parser.matcher.Patterns;

/**
 * This class enables generating a case-insensitive regular expression from a
 * list of elements.
 */
public class Token {

	/**
	 * Generate a case-insensitive regular expression from a list of elements.
	 *
	 * Each element can either be a string representing a simple regular expression,
	 * or a {@link Token}.
	 *
	 * All elements in the final regular expression are separated by optional
	 * spaces, see {@link Patterns#SPACE_REGEXP}.
	 *
	 * Each token is represented as a named-group in the returned pattern. This way
	 * you can retrieve the match data of a particular token by specifying the
	 * group-name of the token.
	 *
	 * @param elements elements to build a regular expression from
	 * @return case-insensitive pattern compiled from the regular expression
	 */
	public static Pattern buildRegexp(final Object... elements) {
		final StringBuilder sb = new StringBuilder();
		for (final Object element : elements) {
			if (sb.length() > 0) {
				sb.append(Patterns.SPACE_REGEXP);
			}
			if (element instanceof Token) {
				sb.append(((Token) element).getRegex());
			} else {
				sb.append(element);
			}
		}

		return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
	}

	/** Name of the token's regex group */
	public final String groupName;
	/** Regex pattern to match the token */
	public final String pattern;

	/**
	 * Each token is represented as a named-group in the returned regular expression.
	 * This way you can get the token's match data by specifying its group-name.
	 *
	 * @param groupName name of the token's regex group
	 * @param pattern   regex pattern to match the token
	 */
	public Token(String groupName, String pattern) {
		this.groupName = groupName;
		this.pattern = pattern;
	}

	/**
	 * Get the regular expression for the current token
	 *
	 * @return regular expression for parsing this token
	 */
	public String getRegex() {
		return String.format("(?<%s>%s)", groupName, pattern);
	}
}