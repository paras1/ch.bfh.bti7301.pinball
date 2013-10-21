package ch.bfh.bti7301.pinball;

import ch.bfh.bti7301.pinball.screens.PinballGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "P1_Pinball";
		
		cfg.useGL20 = false;
		cfg.width = 506;
		cfg.height = 900;
		
		new LwjglApplication(new PinballGame(), cfg);
	}
}
