package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import ch.bfh.bti7301.pinball.FieldLayout;

public class FieldElementTest {

	@Test
	public void test() {
//		World world = new World(new Vector2(0.0f, -20f), true);
		FieldLayout layout = FieldLayout.layoutForLevel("level2.json", null);
		assertTrue(1.0f ==layout.getBallRadius());
	}

}
