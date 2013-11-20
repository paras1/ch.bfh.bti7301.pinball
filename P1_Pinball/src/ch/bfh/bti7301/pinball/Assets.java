package ch.bfh.bti7301.pinball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/*
 * This class loads some assets like the skin for the menu buttons
* @author Dominik Reubi(reubd1@bfh.ch)
* @version 1.0
*/
public class Assets{
	public static Skin skin;


	public static void load () {

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
	}

	}
