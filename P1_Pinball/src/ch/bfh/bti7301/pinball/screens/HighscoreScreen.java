package ch.bfh.bti7301.pinball.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;

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
		
		try {
			FileHandle fh = Gdx.files.internal("data/highscorelist.txt");
			InputStream ips = fh.read();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(ips));
			String line;
			String[] values;
			//Map<K, V, V> m = new HashMap<Integer,<Integer,String>>(); 
			//int i = 0;
			while ((line = br.readLine()) != null) {
				
				values= line.split(",");
				System.out.print(values[0] + "," + values[1] + "\n");
				//m.put(i,values[0], values[1]);
				//i++;
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
