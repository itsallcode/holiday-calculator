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
package org.itsallcode.holidays.calculator.logic.variants;

import java.time.LocalDate;

import org.itsallcode.holidays.calculator.logic.Easter;

/**
 * An instance of this class represents a holiday based on the Orthodox Easter
 * feast, see https://en.wikipedia.org/wiki/Eastern_Orthodoxy.
 */
public class OrthodoxEasterBasedHoliday extends PivotDateBasedHoliday {

	/**
	 * Create a new instance of an {@link OrthodoxEasterBasedHoliday}
	 *
	 * @param category     Category of the holiday, e.g. "birthday"
	 * @param name         Name of the holiday
	 * @param offsetInDays Offset in days between the Orthodox Easter feast and the
	 *                     holiday. A positive offset indicates the holiday being
	 *                     after the Orthodox Easter feast.
	 */
	public OrthodoxEasterBasedHoliday(String category, String name, int offsetInDays) {
		super("Orthodox Easter", category, name, offsetInDays);
	}

	@Override
	public LocalDate of(int year) {
		return Easter.orthodox(year).plusDays(offsetInDays);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}

}
