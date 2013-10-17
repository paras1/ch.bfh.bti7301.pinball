package ch.bfh.bti7301.pinball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Menu implements Screen
{
	Stage stage;
	SpriteBatch batch;
	PinballGame game;
	TextButton startGameButton;
	TextButton optionsButton;
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
		optionsButton = new TextButton("Highscore", Assets.skin);
		exitButton = new TextButton("Editor", Assets.skin);

		
		startGameButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				game.setScreen(new GameArea(game));

				return true;
			}

		});

		table.setFillParent(true);
//		table.debug(); 
		table.add(startGameButton).width(150).height(50);
		table.row();
		table.add(optionsButton).width(150).height(50).padTop(10);
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
}


