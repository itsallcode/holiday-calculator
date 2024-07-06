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
package org.itsallcode.holidays.calculator.logic;

import java.time.LocalDate;

/**
 * This class provides calculation methods for the data of the Easter feast and
 * the Orthodox Easter feast.
 */
public class Easter {

	// disable to create unwanted instances
	private Easter() {
	}

	/**
	 * Source:
	 * https://de.wikibooks.org/wiki/Algorithmensammlung:_Kalender:_Feiertage/
	 *
	 * <p>
	 * Note:
	 * https://www.geeksforgeeks.org/how-to-calculate-the-easter-date-for-a-given-year-using-gauss-algorithm/
	 * yields different values.
	 * </p>
	 *
	 * @param year Year to calculate Easter Sunday for.
	 * @return Easter Sunday in the specified year.
	 */
	public static LocalDate gauss(int year) {
		final int x = year;
		int k; // secular number / century
		int m; // secular moon correction
		int s; // secular sun correction
		int a; // moon parameter / golden number
		int d; // seed for first full moon in spring / (23 - Epact) mod 30
		int r; // calendar correction
		int eb; // Easter boundary
		int sz; // first Sunday in March
		int eo; // offset of Easter Sunday to Easter boundary
		int es; // Easter Sunday as day number of March (March 32nd = April 1st)
		int easterDay;
		int easterMonth;

		k = x / 100;
		m = 15 + (3 * k + 3) / 4 - (8 * k + 13) / 25;
		s = 2 - (3 * k + 3) / 4;
		a = x % 19;
		d = (19 * a + m) % 30;
		r = (d + a / 11) / 29;
		eb = 21 + d - r;
		sz = 7 - (x + x / 4 + s) % 7;
		eo = 7 - (eb - sz) % 7;
		es = eb + eo;

		easterMonth = 2 + (es + 30) / 31;
		easterDay = es - 31 * (easterMonth / 4);

		return LocalDate.of(year, easterMonth, easterDay);
	}

	/**
	 * Source: https://www.emacswiki.org/emacs/ukrainian-holidays.el
	 *
	 * @param year Year to calculate Easter Sunday for.
	 * @return Orthodox Easter Sunday in the specified year.
	 */
	public static LocalDate orthodox(int year) {
		final int x = ((year % 19) * 19 + 15) % 30;
		final int day = x + 10 - (((5 * year) / 4 + x) % 7);
		if (day < 31) {
			return LocalDate.of(year, 4, day);
		} else {
			return LocalDate.of(year, 5, day - 30);
		}
	}
}