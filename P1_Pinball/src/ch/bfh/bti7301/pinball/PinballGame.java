package ch.bfh.bti7301.pinball;

import com.badlogic.gdx.Game;

public class PinballGame extends Game
{
	Menu mainMenuScreen;
    GameArea gameScreen;


   @Override
    public void create() {
	   Assets.load();
            mainMenuScreen = new Menu(this);
            gameScreen = new GameArea(this);
            setScreen(mainMenuScreen);              
    }
}