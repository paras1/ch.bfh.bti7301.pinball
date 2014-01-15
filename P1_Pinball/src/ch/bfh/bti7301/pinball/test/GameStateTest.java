package ch.bfh.bti7301.pinball.test;

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
		//before new game
		assertEquals(3, GameState.getInstance().getTotalBalls());
		assertEquals(0, GameState.getInstance().getBallNumber());
		assertFalse(GameState.getInstance().isGameInProgress());
 
		//start new game
		GameState.getInstance().startNewGame();
		assertEquals(1, GameState.getInstance().getBallNumber());
		assertTrue(GameState.getInstance().isGameInProgress());
 
		//second ball
		GameState.getInstance().doNextBall();
		assertEquals(2, GameState.getInstance().getBallNumber());
		assertTrue(GameState.getInstance().isGameInProgress());
 
		//third ball
		GameState.getInstance().doNextBall();
		assertEquals(3, GameState.getInstance().getBallNumber());
		assertTrue(GameState.getInstance().isGameInProgress());
 
		//game over
		GameState.getInstance().doNextBall();
		assertEquals(3, GameState.getInstance().getBallNumber());
		assertFalse(GameState.getInstance().isGameInProgress());
	}

}
