package ch.bfh.bti7301.pinball.screens;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import ch.bfh.bti7301.pinball.Assets;
import ch.bfh.bti7301.pinball.EditorBuffer;
import ch.bfh.bti7301.pinball.FieldLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

/**
 * The EditorScreen class is the View where you can choose one of your created GameAreas for playing.
 * 
 *
 * @author Sathesh Paramasamy (paras1@bfh.ch)
 * @version 1.0
 */
public class EditorScreen implements Screen
{
	Stage stage;
	SpriteBatch batch;
	PinballGame game;
	TextButton btCreateBoard;
	Label lbTitle;
	Label lbLevelName;
	Label lbNumBalls;
	Label lbGravity;
	Label lbLauncherSpeed;
	Label lbNumBumpers;
	TextField tfLevelName;
	Slider slNumBalls;
	Slider slGravity;
	Slider slLauncherSpeed;
	Slider slNumBumpers;
	
	final String level = "";

	public EditorScreen(PinballGame game) {
		this.game = game;
	}

	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
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

		lbTitle = new Label("P1 Pinball  -  Gameboard creator", Assets.skin);
		
		table.add(lbTitle).width(420).height(50).colspan(2).padBottom(100);
		table.row();
		
		lbLevelName = new Label("Levelname:", Assets.skin);
		tfLevelName = new TextField("", Assets.skin);
		
		table.add(lbLevelName).width(190).height(50).padTop(10);
		table.add(tfLevelName).width(230).height(50).padTop(10);
		table.row();
		
		lbNumBalls = new Label("Number of balls:   1", Assets.skin);
		slNumBalls = new Slider(1, 6, 1, false, Assets.skin);
		slNumBalls.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				lbNumBalls.setText("Number of balls:   "+(int)slNumBalls.getValue());
			}
		}); 
		
		table.add(lbNumBalls).width(190).height(50).padTop(10);
		table.add(slNumBalls).width(230).height(50).padTop(10);
		table.row();
		
		lbNumBumpers = new Label("Number of bumpers:   1", Assets.skin);
		slNumBumpers = new Slider(1, 10, 1, false, Assets.skin);
		slNumBumpers.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				lbNumBumpers.setText("Number of bumpers:   "+(int)slNumBumpers.getValue());
			}
		});
		
		table.add(lbNumBumpers).width(190).height(50).padTop(10);
		table.add(slNumBumpers).width(230).height(50).padTop(10);
		table.row();
		
		lbLauncherSpeed = new Label("Launcher speed:   10", Assets.skin);
		slLauncherSpeed = new Slider(10, 100, 5, false, Assets.skin);
		slLauncherSpeed.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				lbLauncherSpeed.setText("Launcher speed:   "+(int)slLauncherSpeed.getValue());
			}
		});
		
		table.add(lbLauncherSpeed).width(190).height(50).padTop(10);
		table.add(slLauncherSpeed).width(230).height(50).padTop(10);
		table.row();

		btCreateBoard = new TextButton("Create new pinballboard", Assets.skin);
		
		btCreateBoard.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				String level = "new_board.json";
				EditorBuffer.getInstance().setLevelname(tfLevelName.getText());
				EditorBuffer.getInstance().setNumBumpers((int)slNumBumpers.getValue());
				EditorBuffer.getInstance().setActualBumperNum(1);
				EditorBuffer.getInstance().setNumBalls((int)slNumBalls.getValue());
				EditorBuffer.getInstance().setLauncherSpeed((int)slLauncherSpeed.getValue());
				EditorBuffer.getInstance().setMpNewBoardBuffer(FieldLayout.readFieldLayout(level, true));
				EditorBuffer.getInstance().putMpNewBoardBufferElement("numballs", (int)slNumBalls.getValue());
				ArrayList alLauncherSpeed = new ArrayList();
				alLauncherSpeed.add(0);
				alLauncherSpeed.add((int)slLauncherSpeed.getValue());
				EditorBuffer.getInstance().putMpLaunchBufferElement("velocity", alLauncherSpeed);
				System.out.println(new JSONObject(EditorBuffer.getInstance().getMpNewBoardBuffer()));
				System.out.println(new JSONObject(EditorBuffer.getInstance().getMpLaunchBuffer()));
				try {
					FileHandle newBoard = Gdx.files.local("local/tables/"+EditorBuffer.getInstance().getLevelname()+".json");
					System.out.println(newBoard.path());
					//newBoard.file().mkdirs();
					newBoard.file().createNewFile();
					FileHandle templateBoard = Gdx.files.internal("data/tables/new_board.json");
					newBoard.writeString(templateBoard.readString(), false);
					//templateBoard.copyTo(newBoard);
//					System.out.println(templateBoard.file().getAbsolutePath());
//			        InputStream in = new FileInputStream(templateBoard.file());
//
//			        //For Append the file.
//			        //OutputStream out = new FileOutputStream(f2,true);
//
//			        //For Overwrite the file.
//			        OutputStream out = new FileOutputStream(newBoard.file());
//
//			        byte[] buf = new byte[1024];
//			        int len;
//			        while ((len = in.read(buf)) > 0){
//			          out.write(buf, 0, len);
//			        }
//			        in.close();
//			        out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game.setScreen(new BumperEditorScreen(game, EditorBuffer.getInstance().getActualBumperNum()));
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


