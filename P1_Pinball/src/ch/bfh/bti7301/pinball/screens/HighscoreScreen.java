package ch.bfh.bti7301.pinball.screens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import ch.bfh.bti7301.pinball.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class HighscoreScreen implements Screen {

	PinballGame game;

	// constructor to keep a reference to the main Game class
	public HighscoreScreen(PinballGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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

	public void ReadFile() throws IOException {
		String file = "/Users/bfh/git/ch.bfh.bti7301.pinball/P1_Pinball-android/assets/data/highscorelist.txt";
		try {
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				System.out.print(values[0] + "," + values[1] + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
