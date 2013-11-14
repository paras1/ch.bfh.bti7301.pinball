package ch.bfh.bti7301.pinball;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.bfh.bti7301.pinball.screens.GameArea;
import ch.bfh.bti7301.pinball.screens.HighscoreScreen;
import ch.bfh.bti7301.pinball.screens.PinballGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * The Menu class is the entry point of the game and draws a table with three buttons
 * to start the game, view highscore or edit gamearea.
 * 
 *
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class Menu implements Screen
{
	Stage stage;
	SpriteBatch batch;
	PinballGame game;
	TextButton startGameButton;
	TextButton highscoreButton;
	TextButton exitButton;

	public Menu(PinballGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		//resize the stage to the new window size
				stage.setViewport(width, height, false);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Table table = new Table(Assets.skin);

		startGameButton = new TextButton("New Game", Assets.skin);
		highscoreButton = new TextButton("Highscore", Assets.skin);
		exitButton = new TextButton("Editor", Assets.skin);

		
		startGameButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				game.setScreen(new GameArea(game));

				return true;
			}

		});
		highscoreButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				HighscoreScreen hsc = new HighscoreScreen(game);
				try {
					hsc.ReadFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game.setScreen(hsc);
				
				return true;
			}

		});

		table.setFillParent(true);
//		table.debug(); 
		table.add(startGameButton).width(150).height(50);
		table.row();
		table.add(highscoreButton).width(150).height(50).padTop(10);
		table.row();
		table.add(exitButton).width(150).height(50).padTop(10);

//		stage.addActor(backImage);
		stage.addActor(table);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	public void loadBackground(){
		try {
			Image image = new Image();
			Drawable  drawable  = Drawable.createFromPath(Assets.background);
			image.setImageDrawable(drawable);	
		} catch (IOException e) {
		}
	}
}


