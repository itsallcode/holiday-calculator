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
