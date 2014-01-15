package ch.bfh.bti7301.pinball.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.bfh.bti7301.pinball.FieldLayout;

/**
 * FieldElementTest class
 * 
 * @author Dominik Reubi
 * 
 */
public class FieldElementTest {

	@Test
	public void test() {
		FieldLayout layout = FieldLayout.layoutForLevel("Level 2.json", null,
				false);
		assertTrue(1.0f == layout.getBallRadius());
	}

}
