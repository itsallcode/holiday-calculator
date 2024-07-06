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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.holidays.calculator.logic.variants.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser for a file containing holiday definitions.
 */
public class HolidaysFileParser {

	/**
	 * Represents an error that has occurred during parsing the holidays.
	 */
	public static class Error {
		/** number of the line causing the error. */
		public final int lineNumber;
		/** Content of the line causing the error. */
		public final String content;

		Error(int lineNumber, String content) {
			this.lineNumber = lineNumber;
			this.content = content;
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(HolidaysFileParser.class);
	private static final String COMMENT_CHAR = "#";

	private final HolidayParser holidayParser = new HolidayParser();
	private final List<Error> errors = new ArrayList<>();
	private final String identifier;

	/**
	 * Construct a new instance of a holiday file parser.
	 *
	 * @param inputSourceIdentifier Just a string in order to identify the stream in
	 *                              potential error messages. Could be name or path
	 *                              of the file represented by the stream.
	 */
	public HolidaysFileParser(String inputSourceIdentifier) {
		this.identifier = inputSourceIdentifier;
	}

	/**
	 * Parse holidays from the specified input stream.
	 *
	 * @param stream input stream to parse the holidays from.
	 * @return list of holidays
	 * @throws IOException in case of unexpected failures
	 */
	public List<Holiday> parse(InputStream stream) throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
		final List<Holiday> result = new ArrayList<>();

		int n = 0;
		String line;
		while ((line = reader.readLine()) != null) {
			n++;
			line = cutOffComment(line);
			if (line.isEmpty()) {
				continue;
			}

			final Holiday holiday = holidayParser.parse(line);
			if (holiday != null) {
				result.add(holiday);
			} else {
				LOG.error("File {}:{}: Couldn't parse '{}'.", identifier, n, line);
				errors.add(new Error(n, line));
			}
		}
		return result;
	}

	private String cutOffComment(String string) {
		return string.replaceFirst(COMMENT_CHAR + ".*$", "").trim();
	}

	/**
	 * Get the list of errors that occurred during parsing the holidays.
	 *
	 * @return list of errors
	 */
	public List<Error> getErrors() {
		return errors;
	}

}
