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

public class OrthodoxEasterBasedHoliday extends PivotDateBasedHoliday {

	public OrthodoxEasterBasedHoliday(String category, String name, int offsetInDays) {
		super("Orthodox Easter", category, name, offsetInDays);
	}

	@Override
	public LocalDate of(int year) {
		return Easter.orthodox(year).plusDays(offsetInDays);
	}

}