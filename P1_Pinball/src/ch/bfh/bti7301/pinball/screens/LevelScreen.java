package ch.bfh.bti7301.pinball.screens;

import ch.bfh.bti7301.pinball.Assets;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The LevelScreen class is the View where you can choose one of your created GameAreas for playing.
 * 
 *
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class LevelScreen implements Screen
{
	Stage stage;
	SpriteBatch batch;
	PinballGame game;
	TextButton levelButton;
	TextButton levelButton2;
	final String level = "";

	public LevelScreen(PinballGame game) {
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
		
		
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
			  dirHandle = Gdx.files.internal("data/tables");
			} else {
			  // ApplicationType.Desktop ..
			  dirHandle = Gdx.files.internal("./bin/data/tables");
			}

		table.setFillParent(true);	
		for (FileHandle entry: dirHandle.list()) {
		   final String level = entry.file().getName();
		   String levelname = level.substring(0, level.lastIndexOf('.'));
		   if (!levelname.equals("new_Board")) {
			   levelButton = new TextButton(levelname, Assets.skin);
				
				levelButton.addListener(new InputListener() {
					public boolean touchDown(InputEvent event, float x, float y,
							int pointer, int button) {
						// TODO Auto-generated method stub
						game.setScreen(new GameArea(game, level));
	
						return true;
					}
	
				});
	
				table.add(levelButton).width(150).height(50).padTop(10);
				table.row();
		   }
		}

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
}


