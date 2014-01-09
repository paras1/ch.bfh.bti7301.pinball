package ch.bfh.bti7301.pinball.screens;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import aurelienribon.bodyeditor.BodyEditorLoader;
import ch.bfh.bti7301.pinball.FieldLayout;
import ch.bfh.bti7301.pinball.GameState;
import ch.bfh.bti7301.pinball.Physic;
import ch.bfh.bti7301.pinball.elements.BumperElement;
import ch.bfh.bti7301.pinball.elements.FlipperElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This Class draws the GameArea and its elements with their physic
 * 
 * 
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class GameArea implements Screen {

	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private static final float BOX_STEP = 1 / 60f;
	private static final int BOX_VELOCITY_ITERATIONS = 8;
	private static final int BOX_POSITION_ITERATIONS = 3;
	private static final float BACKGROUND_WIDTH = 506 / 10;
	private FieldLayout layout;

	private Body backgroundModel;
	private Texture backgroundTexture;
	private Sprite backgroundSprite;

	private String scoreText;
	BitmapFont scoreFont;
	String playername = "player1";
	private boolean isInputDone = false;

	// things needed to draw
	private SpriteBatch batcher;

	private Body ballBody;
	private Body bumperBody;
	private Body slingshotBody;

	private PinballGame game;

	private boolean isTouchable;
	String level;
	Long highscore;
	private static final int NUMBER_OF_HIGHSCORES = 5;
	private FileHandle fhin = Gdx.files.internal("data/highscorelist.txt");
	private FileHandle fhout = Gdx.files.local("data/highscorelist.txt");
	private FileHandle level1 = Gdx.files.internal("data/pinballbody.json");
	private FileHandle level2 = Gdx.files.internal("data/pinballbody2.json");
	private FileHandle deci;


	// constructor to keep a reference to the main Game class
	public GameArea(PinballGame game, String level) {
		this.game = game;
		this.level = level;
	}

	private void createBackgroundPhysic() {
		// Create a loader for the file saved from the editor.
		if (level.equals("Level 1.json")) {
			deci = Gdx.files.internal("data/pinballbody.json");
			}
		else {
			deci = Gdx.files.internal("data/pinballbody2.json");

		}
		
		BodyEditorLoader loader = new BodyEditorLoader(deci);
		// Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		// Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;

		// Create a Body, as usual.
		backgroundModel = world.createBody(bd);

		// Create the body fixture automatically by using the loader.
		loader.attachFixture(backgroundModel, "gamearea", fd, BACKGROUND_WIDTH);
	}

	private void createBackgroundSprite() {
		if (level.equals("Level 1.json")) {
			backgroundTexture = new Texture(
					Gdx.files.internal("data/Pinball_Background.png"));
		}
		else {
			backgroundTexture = new Texture(
					Gdx.files.internal("data/Pinball_Background2.png"));
		}
				
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		backgroundSprite = new Sprite(backgroundTexture);
		backgroundSprite.setPosition(0, 0);
		backgroundSprite.setSize(506 / 10, 890 / 10);
	}

	/**
	 * Draws a new ball and its physic and set it to the Box2d World. The
	 * position and velocity of the ball are controlled by the "launch" key in
	 * the field layout JSON.
	 */
	public void setBall() {
		List<Number> position = layout.getLaunchPosition();
		float radius = layout.getBallRadius();

		ballBody = Physic.createCircle(world, position.get(0).floatValue(),
				position.get(1).floatValue(), radius, false);
		ballBody.setBullet(true);
		// VPSoundpool.playBall();
	}

	/**
	 * Draws the ball sprite
	 */
	public Sprite getBallSprite() {
		Sprite ballSprite = new Sprite(new Texture("data/kugel2.png"));
		ballSprite.setSize(3f, 3f);
		ballSprite.setOrigin(ballSprite.getWidth() / 2,
				ballSprite.getHeight() / 2);
		ballSprite.setPosition(ballBody.getPosition().x - ballSprite.getWidth()
				/ 2, ballBody.getPosition().y - ballSprite.getHeight() / 2);
		ballSprite
				.setRotation(ballBody.getAngle() * MathUtils.radiansToDegrees);

		return ballSprite;
	}

	@Override
	public void dispose() {
		// dispose any object you created to free up the memory
		backgroundTexture.dispose();
		batcher.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render(float delta) {
		//
		GL10 gl = Gdx.gl10;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);

		List flippers = layout.getFlipperElements();
		List elements = layout.getFieldElements();

		// start the batcher, so we would want to do all of our draw calls
		// between batcher.begin and .end
		batcher.setProjectionMatrix(camera.combined);
		batcher.begin();

		scoreText = "Score: " + GameState.getInstance().getScore();

		// Draw the background
		backgroundSprite.draw(batcher);
		// setting linear filtering
		scoreFont.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		scoreFont.setScale(0.2f);
		scoreFont.draw(batcher, scoreText, 23, 85);

		// Draw the bumpers
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i) instanceof BumperElement) {
				BumperElement bump = (BumperElement) elements.get(i);
				bump.getSprite().draw(batcher);
			}
		}

		// Draw the flippers
		for (int i = 0; i < flippers.size(); i++) {
			if (flippers.get(i) instanceof FlipperElement) {
				FlipperElement flipper = (FlipperElement) flippers.get(i);
				flipper.drawFlipper().draw(batcher);
				// flipper.getSprite().draw(batcher);
			}
		}

		// Draw the ball
		getBallSprite().draw(batcher);

		batcher.end();

		// find objects that collide in our pinballworld during rendering
		// process
		// Physic.doCollisionDetection(world);

		// if the screen is touched, the ballbody will be accelerated(launched)
		List<Float> velocity = layout.getLaunchVelocity();

		if (Gdx.input.isTouched()) {
			activateFlippers(flippers, true);
		} else {
			activateFlippers(flippers, false);
		}

		if (isTouchable) {
			if (Gdx.input.isTouched()) {
				// check if the right bottom place of the screen is touched(ball
				// start position)
				if (Gdx.input.getX() > Gdx.graphics.getWidth() - 25
						&& Gdx.input.getY() > Gdx.graphics.getHeight() - 45) {
					ballBody.setLinearVelocity(new Vector2(velocity.get(0),
							velocity.get(1)));
					isTouchable = false;
				}
			}
		}
		// check if ball is in "deadzone" position of ball < 0 in y screen axis
		if (ballBody.getPosition().y < 0) {
			GameState.getInstance().doNextBall();
			if (GameState.getInstance().isGameInProgress()) {
				world.destroyBody(ballBody);
				setBall();
			} else {
				if (isInputDone == false) {
					gameOver();
				}
			}
			isTouchable = true;
		}
		// debugRenderer.render(world, camera.combined);

	}

	public void activateFlippers(List flippers, boolean activated) {
		for (int i = 0; i < flippers.size(); i++) {
			if (flippers.get(i) instanceof FlipperElement) {
				FlipperElement flipper = (FlipperElement) flippers.get(i);
				flipper.setFlipperActivated(activated);
			}
		}
	}

	@Override
	public void show() {
		world = new World(new Vector2(0.0f, -20f), true);
		isTouchable = true;

		scoreText = "score: 0";
		scoreFont = new BitmapFont(Gdx.files.internal("data/score.fnt"), false);
		scoreFont.setColor(Color.RED);
		layout = FieldLayout.layoutForLevel(level, world);

		createBackgroundPhysic();
		Physic.createCollisionListener(world);

		batcher = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.viewportHeight = 900 / 10;
		camera.viewportWidth = 506 / 10;
		camera.position.set(camera.viewportWidth * .5f,
				camera.viewportHeight * .5f, 0f);
		camera.update();


		GameState.getInstance().setTotalBalls(layout.getNumberOfBalls());
		createBackgroundSprite();
		GameState.getInstance().startNewGame();
		setBall();
		Gdx.input.getTextInput(new TextInputListener() {
			@Override
			public void input(String text) {
				playername = text;
			}

			@Override
			public void canceled() {
			}
		}, "enter your name", "");
		// debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	private void gameOver() {
		highscore = GameState.getInstance().getScore();
		if (highscore != 0) highscoreCheck();
		isInputDone = true;
		Gdx.app.log("gamestate: ", "GAMEOVER!!!");
		Gdx.app.log("endscore: ", highscore + "");
		game.setScreen(new Menu(game));
	}

	@org.junit.Test
	public void highscoreCheck() {
		String[][] arrays = new String[NUMBER_OF_HIGHSCORES+1][2];
		
		try {
			InputStream ips = fhin.read();
			BufferedReader br = new BufferedReader(new InputStreamReader(ips));
			String line;
			String[] values;
			arrays[0][0] = "0";
			for (int i = 1; (i <= NUMBER_OF_HIGHSCORES && (line = br.readLine()) != null); i++) {
				values = line.split(",");
				arrays[i][0] = values[0];
				arrays[i][1] = values[1];
//				System.out.println("i: "+i+"; "+arrays[i][0]+","+arrays[i][1]);
			}
			for (int k = 0; k < NUMBER_OF_HIGHSCORES; k++) {
			long score = Long.parseLong(arrays[k+1][0]);
			if (score > highscore.longValue()){
				arrays[k][0] = arrays[k+1][0];
				arrays[k][1] = arrays[k+1][1];
			}
			else {
				arrays[k][0] = highscore.toString();
				arrays[k][1] = playername;
				break;
			}
//			System.out.println("k: "+k+"; "+arrays[k][0]+","+arrays[k][1]);

			}
			br.close();

			System.out.println("New Highscore by " + playername + ": " + highscore +"!");
			fhout.writeString(arrays[0][0]+ "," + arrays[0][1] + "\n" + arrays[1][0]+ "," + arrays[1][1] + "\n"+ arrays[2][0]+ "," + arrays[2][1] + "\n"+ arrays[3][0]+ "," + arrays[3][1] + "\n"+ arrays[4][0]+ "," + arrays[4][1] + "\n", false);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
