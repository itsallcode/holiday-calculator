package org.itsallcode.holidays.calculator.logic.conditions;

import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ConditionTest {
	@Test
	void testEquals() {
		EqualsVerifier.simple().forClasses(List.of( //
				ConstantCondition.class, //
				DayOfWeekCondition.class, //
				NegatedCondition.class //
		)).verify();

	}
}
