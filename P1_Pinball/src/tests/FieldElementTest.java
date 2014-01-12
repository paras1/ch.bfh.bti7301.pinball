package tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.bfh.bti7301.pinball.FieldLayout;

public class FieldElementTest {

	@Test
	public void test() {
		FieldLayout layout = FieldLayout.layoutForLevel("level2.json", null, false);
		assertTrue(1.0f ==layout.getBallRadius());
	}

}
