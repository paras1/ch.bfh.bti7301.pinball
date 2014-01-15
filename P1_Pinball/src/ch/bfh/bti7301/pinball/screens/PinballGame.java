package ch.bfh.bti7301.pinball.screens;

import ch.bfh.bti7301.pinball.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

/**
 * PinballGame is the class which all the references are made to
 * 
 * @author Dominik Reubi
 * 
 */
public class PinballGame extends Game {
	Menu mainMenuScreen;
	GameArea gameScreen;

	/**
	 * creates a new game
	 */
	public void create() {
		Assets.load();
		String defaultLevel = "";
		Texture.setEnforcePotImages(false);
		mainMenuScreen = new Menu(this);
		gameScreen = new GameArea(this, defaultLevel, false);
		setScreen(mainMenuScreen);
	}
}