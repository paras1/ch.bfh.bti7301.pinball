package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.bfh.bti7301.pinball.GameState;

public class GameStateTest {

	@Test
	public void testScore() {

		assertEquals(0, GameState.getInstance().getScore());
		GameState.getInstance().addScore(5);
		assertEquals(5, GameState.getInstance().getScore());
	}

	@Test
	public void testSingleton() {

		GameState gs1 = GameState.getInstance();
		GameState gs2 = GameState.getInstance();
		assertSame(gs1, gs2);
	}

	@Test
	public void testBalls() {

		assertEquals(3, GameState.getInstance().getTotalBalls());
		GameState.getInstance().doNextBall();
		assertEquals(1, GameState.getInstance().getBallNumber());
		GameState.getInstance().doNextBall();
		assertFalse(GameState.getInstance().isGameInProgress());
		assertEquals(2, GameState.getInstance().getBallNumber());
		GameState.getInstance().doNextBall();
		assertFalse(GameState.getInstance().isGameInProgress());
	}

}
