package ch.bfh.bti7301.pinball.screens;

import ch.bfh.bti7301.pinball.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class PinballGame extends Game
{
	Menu mainMenuScreen;
    GameArea gameScreen;


   @Override
    public void create() {
	   Assets.load();
	   String defaultLevel = "";
	   Texture.setEnforcePotImages(false);
       mainMenuScreen = new Menu(this);
       gameScreen = new GameArea(this, defaultLevel, false);
       setScreen(mainMenuScreen);              
    }
}