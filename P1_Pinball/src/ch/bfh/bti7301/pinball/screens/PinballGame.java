package ch.bfh.bti7301.pinball.screens;

import ch.bfh.bti7301.pinball.Assets;
import ch.bfh.bti7301.pinball.Menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class PinballGame extends Game
{
	Menu mainMenuScreen;
    GameArea gameScreen;


   @Override
    public void create() {
	   Assets.load();
	   Texture.setEnforcePotImages(false);
       mainMenuScreen = new Menu(this);
       gameScreen = new GameArea(this);
       setScreen(mainMenuScreen);              
    }
}