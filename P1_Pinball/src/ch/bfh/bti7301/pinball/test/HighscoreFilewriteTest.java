package ch.bfh.bti7301.pinball.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ch.bfh.bti7301.pinball.screens.GameArea;
import ch.bfh.bti7301.pinball.screens.HighscoreScreen;
import ch.bfh.bti7301.pinball.screens.PinballGame;

public class HighscoreFilewriteTest {

	@Test
	public void readHighscoreTest() throws IOException {
		PinballGame game = new PinballGame();
		GameArea gameA = new GameArea(game,"Level 1.json");
		
		gameA.highscoreCheck();
		HighscoreScreen hss = new HighscoreScreen(game); 
		hss.ReadFile();
	}

}
