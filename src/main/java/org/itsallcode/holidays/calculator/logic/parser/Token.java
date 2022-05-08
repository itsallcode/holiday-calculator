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

public class Token {

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

	public final String groupName;
	public final String pattern;

	public Token(String groupName, String pattern) {
		this.groupName = groupName;
		this.pattern = pattern;
	}

	public String getRegex() {
		return String.format("(?<%s>%s)", groupName, pattern);
	}
}