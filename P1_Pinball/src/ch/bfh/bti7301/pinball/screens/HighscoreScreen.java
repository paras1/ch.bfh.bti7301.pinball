package ch.bfh.bti7301.pinball.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.NavigableMap;
import java.awt.*;

import javax.swing.*;

import ch.bfh.bti7301.pinball.Assets;
import ch.bfh.bti7301.pinball.screens.GameArea;
import ch.bfh.bti7301.pinball.screens.HighscoreScreen;
import ch.bfh.bti7301.pinball.screens.PinballGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
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

public class HighscoreScreen implements Screen {
	Stage stage;
	PinballGame game;
	// Hintergrundbild
	Texture backgroundImage;
	SpriteBatch batcher;
	SpriteBatch spriteBatchBack;
	Sprite sprite;
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	private static final int NUMBER_OF_HIGHSCORES = 5;
	String[][] arrays = new String[NUMBER_OF_HIGHSCORES + 1][2];
	private FileHandle fhin = Gdx.files.internal("data/highscorelist.txt");
	TextButton MenuButton;
//	TextField title = "Highscore";

	// constructor to keep a reference to the main Game class
	public HighscoreScreen(PinballGame game) {
		this.game = game;
	}
	@org.junit.Test
	public void ReadFile() {
		try {
			InputStream ips = fhin.read();
			BufferedReader br = new BufferedReader(new InputStreamReader(ips));
			String line;
			String[] values;
			arrays[0][0] = "0";
			for (int i = 0; (i < NUMBER_OF_HIGHSCORES && (line = br.readLine()) != null); i++) {
				values = line.split(",");
				arrays[i][0] = values[0];
				arrays[i][1] = values[1];
				// System.out.println("i: "+i+"; "+arrays[i][0]+","+arrays[i][1]);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
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
		ReadFile();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// Hintergrundbild
		Texture spriteTexture = new Texture(
				Gdx.files.internal("data/back3.png"));
		spriteTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		sprite = new Sprite(spriteTexture, 0, 0, spriteTexture.getWidth(),
				spriteTexture.getHeight());
		sprite.setSize(width, height);

		Table table = new Table(Assets.skin);
		MenuButton = new TextButton("Menu", Assets.skin);
		MenuButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new Menu(game));
				return true;
			}

		});
		table.setFillParent(true);
		table.add("");
		table.add("Highscores");
		table.row();
		table.add("Rank");
		table.add("Name");
		table.add("Score");
		table.row();
		table.add("1");
		table.add(arrays[0][1]);
		table.add(arrays[0][0]);
		table.row();
		table.add("2");
		table.add(arrays[1][1]);
		table.add(arrays[1][0]);
		table.row();
		table.add("3");
		table.add(arrays[2][1]);
		table.add(arrays[2][0]);
		table.row();
		table.add("4");
		table.add(arrays[3][1]);
		table.add(arrays[3][0]);
		table.row();
		table.add("5");
		table.add(arrays[4][1]);
		table.add(arrays[4][0]);
		table.row();
		table.add("");
		table.add(MenuButton).width(150).height(50);
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
