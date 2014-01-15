package ch.bfh.bti7301.pinball.screens;

import ch.bfh.bti7301.pinball.Assets;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The LevelScreen class is the View where you can choose one of your created
 * GameAreas for playing.
 * 
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class LevelScreen implements Screen {
	Stage stage;
	SpriteBatch batch;
	PinballGame game;
	TextButton levelButton;
	TextButton levelButton2;
	final String level = "";
	// Hintergrundbild
	Texture backgroundImage;
	SpriteBatch batcher;
	SpriteBatch spriteBatchBack;
	Sprite sprite;
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();

	/**
	 * constructor
	 * @param game
	 */
	public LevelScreen(PinballGame game) {
		this.game = game;
	}

	/**
	 * renders the screen
	 */
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Hintergrundbild
		spriteBatchBack = new SpriteBatch();
		spriteBatchBack.begin();
		sprite.draw(spriteBatchBack);
		spriteBatchBack.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// resize the stage to the new window size
		stage.setViewport(width, height, false);
	}

	/**
	 * initiates the screen
	 */
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// Hintergrundbild
		Texture spriteTexture = new Texture(
				Gdx.files.internal("data/back2.png"));
		spriteTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		sprite = new Sprite(spriteTexture, 0, 0, spriteTexture.getWidth(),
				spriteTexture.getHeight());
		sprite.setSize(width, height);

		FileHandle dirHandle;
		Table table = new Table(Assets.skin);
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal("data/tables");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("./bin/data/tables");
		}

		table.setFillParent(true);
		for (FileHandle entry : dirHandle.list()) {
			final String level = entry.file().getName();
			String levelname = level.substring(0, level.lastIndexOf('.'));
			if (!levelname.equals("new_Board")) {
				levelButton = new TextButton(levelname, Assets.skin);

				levelButton.addListener(new InputListener() {
					public boolean touchDown(InputEvent event, float x,
							float y, int pointer, int button) {
						game.setScreen(new GameArea(game, level, false));

						return true;
					}

				});

				table.add(levelButton).width(350).height(150).padTop(50);
				table.row();
			}
		}

		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.local("local/tables");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("local/tables");
		}

		table.setFillParent(true);
		for (FileHandle entry : dirHandle.list()) {
			final String level = entry.file().getName();
			String levelname = level.substring(0, level.lastIndexOf('.'));
			if (!levelname.equals("new_Board")) {
				levelButton2 = new TextButton(levelname + " (OWN)", Assets.skin);

				levelButton2.addListener(new InputListener() {
					public boolean touchDown(InputEvent event, float x,
							float y, int pointer, int button) {
						// TODO Auto-generated method stub
						game.setScreen(new GameArea(game, level, true));

						return true;
					}

				});

				table.add(levelButton2).width(350).height(150).padTop(50);
				table.row();
			}
		}

		stage.addActor(table);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
