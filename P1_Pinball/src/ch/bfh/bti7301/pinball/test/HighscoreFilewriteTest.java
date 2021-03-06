package ch.bfh.bti7301.pinball.test;

import java.io.IOException;

import org.junit.Test;

import ch.bfh.bti7301.pinball.screens.GameArea;
import ch.bfh.bti7301.pinball.screens.HighscoreScreen;
import ch.bfh.bti7301.pinball.screens.PinballGame;

/**
 * HighscoreTest class
 * 
 * @author Olivier Dueggelin
 * 
 */
public class HighscoreFilewriteTest {

	@Test
	public void readHighscoreTest() throws IOException {
		PinballGame game = new PinballGame();
		GameArea gameA = new GameArea(game, "Level 1.json", false);

		gameA.highscoreCheck();
		HighscoreScreen hss = new HighscoreScreen(game);
		hss.ReadFile();
	}

}
