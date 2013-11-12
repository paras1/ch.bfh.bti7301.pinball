package ch.bfh.bti7301.pinball.screens;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class HighscoreScreen implements Screen {
	
	PinballGame game;
	 

    // constructor to keep a reference to the main Game class
     public HighscoreScreen(PinballGame game){
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
	public void ReadFile(){
		
		String[][] dataArray = new String[5][2];
		String file = "/Users/bfh/git/ch.bfh.bti7301.pinball/P1_Pinball-android/assets/data/highscorelist.txt";
		
		try{
		InputStream ips = new FileInputStream(file); 
		InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String line;
        Character c;
        String cString = "";
        int i = 0;
        for (i = 0; i < 5; i++){
       
        	if (br.readLine() != null){
        		int j = 0;
        		line = br.readLine();
        		Scanner sc = new Scanner(line);
        		while (sc.next().charAt(j) != ',') {
        	        c = sc.next().charAt(j);
        	        cString = cString + c.toString();
        			j++;
        		}
        		dataArray[i][0] = cString;
        		c = null;
        		cString = "";
        		j++;
        		int k = j + 7;
        		while (j < k){
        	        c = (char) (c + sc.next().charAt(j));
        	        cString = cString + c.toString();
        	        j++;
        		}
        		dataArray[i][1] = cString;
        		System.out.println(cString);
        		c = null;
        		cString = "";
        	}
        	else 
        		dataArray[i][0] = "";
        		dataArray[i][1] = "";
        }
		br.close();
        }
		 catch (Exception e){
		        System.out.println(e.toString());
		    }
		//System.out.println(dataArray);
	}

}
