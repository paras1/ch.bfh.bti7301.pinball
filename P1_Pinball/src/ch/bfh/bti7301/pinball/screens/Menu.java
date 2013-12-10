package ch.bfh.bti7301.pinball.screens;

import java.io.IOException;

import ch.bfh.bti7301.pinball.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
	TextButton editorButton;
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
		// TODO Auto-generated method stub
		//Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		spriteBatchBack = new SpriteBatch();

		
	    spriteBatchBack.begin();
	    sprite.draw(spriteBatchBack);
	    spriteBatchBack.end();
		
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
		
		Texture spriteTexture = new Texture(Gdx.files.internal("data/back.png"));

	    spriteTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	    sprite = new Sprite(spriteTexture, 0, 0, spriteTexture.getWidth(), spriteTexture.getHeight());
	    sprite.setSize(width, height);

		startGameButton = new TextButton("New Game", Assets.skin);
		highscoreButton = new TextButton("Highscore", Assets.skin);
		editorButton = new TextButton("Editor", Assets.skin);

		
		startGameButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
//				game.setScreen(new GameArea(game));
				game.setScreen(new LevelScreen(game));

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
		editorButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				EditorScreen editor = new EditorScreen(game);
				game.setScreen(editor);
				
				return true;
			}

		});


		table.setFillParent(true);
//		table.debug(); 
		table.add(startGameButton).width(150).height(50);
		table.row();
		table.add(highscoreButton).width(150).height(50).padTop(10);
		table.row();
		table.add(editorButton).width(150).height(50).padTop(10);

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
}


