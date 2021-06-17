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
package org.itsallcode.holidays.calculator.logic.parser.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.itsallcode.holidays.calculator.logic.variants.ConditionalHoliday;
import org.itsallcode.holidays.calculator.logic.variants.Holiday;

class NegatedConditionMatcher extends HolidayMatcher {

	NegatedConditionMatcher(HolidayMatcher originalMatcher, Pattern pattern) {
		super(originalMatcher, pattern);
	}

	@Override
	Holiday createHoliday(Matcher matcher) {
		return new ConditionalHoliday( //
				createConditionBuilder(matcher).negated(),
				createOriginalHoliday(matcher));
	}
}
