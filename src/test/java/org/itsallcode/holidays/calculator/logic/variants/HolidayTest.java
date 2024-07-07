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

import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class HolidayTest {
	@Test
	void testEquals() {
		EqualsVerifier.simple().forClasses(List.of(
				ConditionalHoliday.class, //
				EasterBasedHoliday.class, //
				FixedDateHoliday.class, //
				FloatingHoliday.class, //
				Holiday.class, //
				HolidayWithAlternative.class, //
				OrthodoxEasterBasedHoliday.class, //
				PivotDateBasedHoliday.class)).verify();
	}
}
