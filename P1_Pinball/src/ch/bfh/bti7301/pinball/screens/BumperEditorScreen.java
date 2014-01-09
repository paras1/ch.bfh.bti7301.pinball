package ch.bfh.bti7301.pinball.screens;

import ch.bfh.bti7301.pinball.Assets;
import ch.bfh.bti7301.pinball.EditorBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

/**
 * The EditorScreen class is the View where you can choose one of your created GameAreas for playing.
 * 
 *
 * @author Sathesh Paramasamy (paras1@bfh.ch)
 * @version 1.0
 */
public class BumperEditorScreen implements Screen
{
	Stage stage;
	SpriteBatch batch;
	PinballGame game;
	int bumperNumber;
	TextButton btCreateBoard;
	Label lbTitle;
	Label lbBumperKick;
	Label lbBumperRadius;
	Label lbBumperPoints;
	Slider slBumperKick;
	Slider slBumperRadius;
	Slider slBumperPoints;
	
	final String level = "";

	public BumperEditorScreen(PinballGame game, int bumperNumber) {
		this.game = game;
		this.bumperNumber = bumperNumber;
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	public void resize(int width, int height) {
		//resize the stage to the new window size
				stage.setViewport(width, height, false);
	}

	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Table table = new Table(Assets.skin);
		
		table.setFillParent(true);

		lbTitle = new Label("P1 Pinball  -  Bumper "+bumperNumber+" properties", Assets.skin);
		
		table.add(lbTitle).width(420).height(50).colspan(2).padBottom(100);
		table.row();
		
		lbBumperKick = new Label("Kick:   0", Assets.skin);
		slBumperKick = new Slider(0, 200, 10, false, Assets.skin);
		slBumperKick.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				lbBumperKick.setText("Kick:   "+(int)slBumperKick.getValue());
			}
		}); 
		
		table.add(lbBumperKick).width(190).height(50).padTop(10);
		table.add(slBumperKick).width(230).height(50).padTop(10);
		table.row();
		
		lbBumperPoints = new Label("Points:   0", Assets.skin);
		slBumperPoints = new Slider(0, 200, 5, false, Assets.skin);
		slBumperPoints.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				lbBumperPoints.setText("Points:   "+(int)slBumperPoints.getValue());
			}
		});
		
		table.add(lbBumperPoints).width(190).height(50).padTop(10);
		table.add(slBumperPoints).width(230).height(50).padTop(10);
		table.row();
		
		lbBumperRadius = new Label("Radius:   1", Assets.skin);
		slBumperRadius = new Slider(1, 5, 1, false, Assets.skin);
		slBumperRadius.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				lbBumperRadius.setText("Radius:   "+(int)slBumperRadius.getValue());
			}
		});
		
		table.add(lbBumperRadius).width(190).height(50).padTop(10);
		table.add(slBumperRadius).width(230).height(50).padTop(10);
		table.row();

		btCreateBoard = new TextButton("Place bumper "+bumperNumber, Assets.skin);
		
		btCreateBoard.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				String level = EditorBuffer.getInstance().getLevelname()+".json";
				//String level = "new_board.json";
				EditorBuffer.getInstance().setActualBumperRadius(slBumperRadius.getValue());
				EditorBuffer.getInstance().setActualBumperKick((int)slBumperKick.getValue());
				EditorBuffer.getInstance().setActualBumperPoints((int)slBumperPoints.getValue());
				game.setScreen(new GameAreaEditor(game, level));
				return true;
			}
		});

		table.add(btCreateBoard).width(420).height(50).padTop(50).colspan(2);
		table.row();

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

