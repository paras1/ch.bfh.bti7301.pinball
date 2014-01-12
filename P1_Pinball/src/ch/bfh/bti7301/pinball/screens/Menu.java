package ch.bfh.bti7301.pinball.screens;

import ch.bfh.bti7301.pinball.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
 * The Menu class is the entry point of the game and draws a table with three
 * buttons to start the game, view highscore or edit gamearea.
 * 
 * 
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class Menu implements Screen {
	Stage stage;
	SpriteBatch batch;
	PinballGame game;
	TextButton startGameButton;
	TextButton highscoreButton;
	TextButton editorButton;

	// Hintergrundbild
	Texture backgroundImage;
	SpriteBatch batcher;
	SpriteBatch spriteBatchBack;
	Sprite sprite;
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();

	public Menu(PinballGame game) {
		this.game = game;
	}

	@Override
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

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Table table = new Table(Assets.skin);

		// Hintergrundbild
		Texture spriteTexture = new Texture(Gdx.files.internal("data/back.png"));
		spriteTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		sprite = new Sprite(spriteTexture, 0, 0, spriteTexture.getWidth(),
				spriteTexture.getHeight());
		sprite.setSize(width, height);

		startGameButton = new TextButton("New Game", Assets.skin);
		highscoreButton = new TextButton("Highscore", Assets.skin);
		editorButton = new TextButton("Editor", Assets.skin);

		startGameButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new LevelScreen(game));

				return true;
			}

		});
		highscoreButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new HighscoreScreen(game));

				return true;
			}

		});
		editorButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				EditorScreen editor = new EditorScreen(game);
				game.setScreen(editor);

				return true;
			}

		});

		table.setFillParent(true);
		table.add(startGameButton).width(350).height(150);
		table.row();
		table.add(highscoreButton).width(350).height(150).padTop(50);
		table.row();
		table.add(editorButton).width(350).height(150).padTop(50);

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
