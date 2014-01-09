package ch.bfh.bti7301.pinball.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aurelienribon.bodyeditor.BodyEditorLoader;
import ch.bfh.bti7301.pinball.Assets;
import ch.bfh.bti7301.pinball.EditorBuffer;
import ch.bfh.bti7301.pinball.FieldLayout;
import ch.bfh.bti7301.pinball.GameState;
import ch.bfh.bti7301.pinball.Physic;
import ch.bfh.bti7301.pinball.elements.BumperElement;
import ch.bfh.bti7301.pinball.elements.FieldElement;
import ch.bfh.bti7301.pinball.elements.FlipperElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

/**
 * This Class draws the GameAreaEditor
 * 
 *
 * @author Sathesh Paramasamy
 * @version 1.0
 */
public class GameAreaEditor implements Screen {
	
    private World world;  
    private Box2DDebugRenderer debugRenderer;  
    private OrthographicCamera camera;  
    private static final float BOX_STEP=1/60f;  
    private static final int BOX_VELOCITY_ITERATIONS=8;  
    private static final int BOX_POSITION_ITERATIONS=3; 
    private static final float BACKGROUND_WIDTH = 506/10;
    private FieldLayout layout;
    
	private Body backgroundModel;
	private Texture backgroundTexture;
	private Sprite backgroundSprite;
	private Texture texture;
	private Sprite bumperSprite;
	
	private String scoreText;
	BitmapFont scoreFont;
	String playerName;
	private boolean isInputDone = false;
	

 
	//things needed to draw
	private Stage stage;
	private SpriteBatch batcher;
	private Pixmap pixmap;
	private Body ballBody;
	private Body bumperBody;
	private Body slingshotBody;
    
	private PinballGame game;
	
	private boolean isTouchable;
	private boolean isNewBumperVisible;
	String level;
	
	private TextButton tbNext;
	private TextButton tbBack;
	
	private Map mpActualBumper = new HashMap();
	private ArrayList alBumperColor1 = new ArrayList();
	private ArrayList<Float> alBumperPosition = new ArrayList<Float>();
	
	private Dialog dialog;



    // constructor to keep a reference to the main Game class
     public GameAreaEditor(PinballGame game, String level){
             this.game = game;
             this.level = level;
     }
     
     private void createBackgroundPhysic() {
 		//Create a loader for the file saved from the editor.
 		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/pinballbody.json"));

 		//Create a BodyDef, as usual.
 		BodyDef bd = new BodyDef();
 		bd.type = BodyType.StaticBody;

 		//Create a FixtureDef, as usual.
 		FixtureDef fd = new FixtureDef();
 		fd.density = 1;
 		fd.friction = 0.5f;
 		fd.restitution = 0.3f;

 		//Create a Body, as usual.
 		backgroundModel = world.createBody(bd);

 		//Create the body fixture automatically by using the loader.
 		loader.attachFixture(backgroundModel, "gamearea", fd, BACKGROUND_WIDTH);
 	}
     
     private void createBackgroundSprite() {
    	 backgroundTexture = new Texture(Gdx.files.internal("data/Pinball_Background.png"));
    	 backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

    	 backgroundSprite = new Sprite(backgroundTexture);
    	 backgroundSprite.setPosition(0, 0);
    	 backgroundSprite.setSize(506/10, 890/10 );
     }


 	
    /** Draws a new ball and its physic and set it to the Box2d World.
     * The position and velocity of the ball are controlled by the "launch" key in the field layout JSON.
     */
    public  void setBall() {
    	List<Number> position = layout.getLaunchPosition();
    	float radius = layout.getBallRadius();
    	
		ballBody = Physic.createCircle(world, position.get(0).floatValue(), position.get(1).floatValue(), radius, false);
		ballBody.setBullet(true);
//		VPSoundpool.playBall();
    }
    
    /** Draws the ball sprite
     */
    public Sprite getBallSprite(){
		Sprite ballSprite = new Sprite(new Texture("data/kugel1.png"));
		ballSprite.setSize(3f, 3f);
		ballSprite.setOrigin(ballSprite.getWidth()/2, ballSprite.getHeight()/2);
		ballSprite.setPosition(ballBody.getPosition().x - ballSprite.getWidth()/2, ballBody.getPosition().y - ballSprite.getHeight()/2);
		ballSprite.setRotation(ballBody.getAngle() * MathUtils.radiansToDegrees);
		
		return ballSprite;
    }
    

	@Override
	public void dispose() {
		//dispose any object you created to free up the memory
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
         
		//start the batcher, so we would want to do all of our draw calls between batcher.begin and .end
		batcher.setProjectionMatrix(camera.combined);
        batcher.begin();
        
        
        scoreText = "Score: "+GameState.getInstance().getScore();

        //Draw the background
        backgroundSprite.draw(batcher);
      //setting linear filtering
        scoreFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        scoreFont.setScale(0.2f);

        scoreFont.draw(batcher, scoreText, 23,85);

        //Draw the bumpers
        for(int i = 0; i<elements.size(); i++){
	        if(elements.get(i) instanceof BumperElement){
	        	BumperElement bump = (BumperElement)elements.get(i);
	        	bump.getSprite().draw(batcher);
	        }
        }
        
    	//Draw the flippers
        for(int i = 0; i<flippers.size(); i++){
	        if(flippers.get(i) instanceof FlipperElement){
	        	FlipperElement flipper = (FlipperElement)flippers.get(i);
	        	flipper.drawFlipper().draw(batcher);
//	        	flipper.getSprite().draw(batcher);
	        }
        }

        //Draw the ball
		//getBallSprite().draw(batcher);   

		
		
		//find objects that collide in our pinballworld during rendering process
//		Physic.doCollisionDetection(world);


		//if the screen is touched, the ballbody will be accelerated(launched)
    	//List<Float> velocity = layout.getLaunchVelocity(); 
    	if (isTouchable){
	    	if(Gdx.input.isTouched() && Gdx.input.getY() < 750){
	    		mpActualBumper.put("class", "BumperElement");
	    		mpActualBumper.put("kick", EditorBuffer.getInstance().getActualBumperKick());
	    		mpActualBumper.put("score", EditorBuffer.getInstance().getActualBumperPoints());
	    		mpActualBumper.put("radius", EditorBuffer.getInstance().getActualBumperRadius());
	    		
	    		if (alBumperColor1.size()>0){
		    		alBumperColor1.set(0,0);
		    		alBumperColor1.set(1,0);
		    		alBumperColor1.set(2,224);
	    		} else {
	    			alBumperColor1.add(0);
		    		alBumperColor1.add(0);
		    		alBumperColor1.add(224);
	    		}
	    		mpActualBumper.put("color", alBumperColor1);
	    		
	    		if (alBumperPosition.size()>0){
		    		alBumperPosition.set(0,(float)Gdx.input.getX()/10);
		    		alBumperPosition.set(1, (float)Gdx.input.getY()/10);
	    		} else {
		    		alBumperPosition.add((float)Gdx.input.getX()/10);
		    		alBumperPosition.add((float)Gdx.input.getY()/10);
	    		}
	    		mpActualBumper.put("position", alBumperPosition);
	    		System.out.println(mpActualBumper.toString());
	    		//layout.addFieldElements(mpActualBumper, "", BumperElement.class, world);
	    		System.out.println(EditorBuffer.getInstance().getActualBumperNum()+": "+layout.getFieldElements());
	    		//elements = layout.getFieldElements();
	    		
	    		//batcher.begin();
	    		
	    		//getSprite().draw(batcher);
//	            for(int i = 0; i<elements.size(); i++){
//	            	System.out.println(elements.get(i).toString());
//	    	        if(elements.get(i) instanceof BumperElement){
//	    	        	BumperElement bump = (BumperElement)elements.get(i);
//	    	        	bump.getSprite().draw(batcher);
//	    	        }
//	            }
	            System.out.println("GDX X:"+Gdx.input.getX());
	            System.out.println("GDX Y:"+Gdx.input.getY());
	            isNewBumperVisible = true;
	            
	            //batcher.end();
	            
	        
	    		
	    	}
	    	else{
	    		
	    	}
	    	if (isNewBumperVisible) {
	    		drawBumper(alBumperPosition.get(0), alBumperPosition.get(1), EditorBuffer.getInstance().getActualBumperRadius()).draw(batcher);
	    		//bumperOK();
	    		//isTouchable = false;
	    	}
		}
    	
        
    	batcher.end();
    	
    	stage.act(delta);
    	stage.draw();
    	
//    	if(isTouchable){
//			if(Gdx.input.isTouched()){
//				//check if the right bottom place of the screen is touched(ball start position)
//				if(Gdx.input.getX()>Gdx.graphics.getWidth()-25 && Gdx.input.getY()>Gdx.graphics.getHeight()-45){
//					ballBody.setLinearVelocity(new Vector2(velocity.get(0), velocity.get(1)));
//					isTouchable = false;
//				}
//			}
//    	}
//    	//check if ball is in "deadzone" position of ball < 0 in y screen axis
//        if(ballBody.getPosition().y < 0){
//        	GameState.getInstance().doNextBall();
//        	if(GameState.getInstance().isGameInProgress()){
//            	world.destroyBody(ballBody);
//        		setBall();
//        	}
//        	else{
//        		if(isInputDone==false){
//        		gameOver();
//        		}
//        	}
//        	isTouchable = true;
//        }
//        debugRenderer.render(world, camera.combined);  
   
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		world = new World(new Vector2(0.0f, -20f), true);
		isTouchable = true;
		
		scoreText = "score: 0";
	    scoreFont = new BitmapFont(Gdx.files.internal("data/score.fnt"),false);
	    scoreFont.setColor(Color.RED);
	    
        createBackgroundPhysic();
        Physic.createCollisionListener(world);
        
        batcher = new SpriteBatch();
        
	    camera = new OrthographicCamera();
        camera.viewportHeight = 900/10;  
        camera.viewportWidth = 506/10;  
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);  
        camera.update();  
        
        layout = FieldLayout.layoutForLevel(level,world,false);
        
        GameState.getInstance().setTotalBalls(layout.getNumberOfBalls());
        createBackgroundSprite();
        GameState.getInstance().startNewGame();
        setBall();
        
		if(EditorBuffer.getInstance().getActualBumperNum()<EditorBuffer.getInstance().getNumBumpers()){
			tbNext = new TextButton("NEXT", Assets.skin);
		} else {
			tbNext = new TextButton("SAVE", Assets.skin);
		}
        
        tbNext.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				//String level = EditorBuffer.getInstance().getLevelname()+".json";
				//String level = "new_board.json";
				EditorBuffer.getInstance().addAlElementsBufferElement(mpActualBumper);
				if(EditorBuffer.getInstance().getActualBumperNum()<EditorBuffer.getInstance().getNumBumpers()){
					EditorBuffer.getInstance().setActualBumperNum(EditorBuffer.getInstance().getActualBumperNum()+1);
					EditorBuffer.getInstance().JSONWriter();
					game.setScreen(new BumperEditorScreen(game, EditorBuffer.getInstance().getActualBumperNum()));
				} else {
					EditorBuffer.getInstance().JSONWriter();
					game.setScreen(new Menu(game));
				}
				
				return true;
			}
		});
        tbBack = new TextButton("BACK", Assets.skin);
        
//		tbNext.setScale(0.1f);
//		tbBack.setScale(0.1f);
        
        Table table = new Table();
        table.setFillParent(true);
//      table.setScale(0.1f);
        table.bottom();
        
        table.add(tbBack).align(Align.left).width(120).height(40).padRight(220);
        table.add(tbNext).align(Align.right).width(120).height(40);
        
        stage.addActor(table);

//        debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
//	private void bumperOK(){
//		Gdx.app.log("gamestate: ", "GAMEOVER!!!");
//		Gdx.app.log("endscore: ", GameState.getInstance().getScore()+"");
//		Gdx.input.getTextInput(new TextInputListener() {
//			@Override
//			public void input () {
//				playerName = text;
//				game.setScreen(new Menu(game));
//			}
//
//			@Override
//			public void canceled () {
//				playerName = "default";
//			}
//		}, "enter your name", "");
//		isInputDone = true;
//    
//	}
	
	private void gameOver(){
		Gdx.app.log("gamestate: ", "BumperOK?!!!");
		Gdx.app.log("endscore: ", GameState.getInstance().getScore()+"");
		Gdx.input.getTextInput(new TextInputListener() {
			@Override
			public void input (String text) {
				playerName = text;
				FileHandle file = Gdx.files.local("assets/data/highscorelist.txt");
				file.writeString("\n"+GameState.getInstance().getScore()+","+playerName, true);
				game.setScreen(new Menu(game));
			}

			@Override
			public void canceled () {
				playerName = "default";
			}
		}, "enter your name", "");
		isInputDone = true;
	}
	
    public Sprite drawBumper(Float posx, Float posy, Float radius){
    	 
		pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
		texture = new Texture(pixmap);
 		//draw a yellow circle
 		pixmap.setColor(1, 1, 0,1);
 		pixmap.fillCircle(256/2,256/2,256/2);
  
 		texture.draw(pixmap, 0, 0);
 		texture.bind();
 		
 		bumperSprite = new Sprite(texture);
 		bumperSprite.setSize(radius*2, radius*2);
 		bumperSprite.setPosition(posx-2, camera.viewportHeight-posy-2);
 		
		return bumperSprite;
    	
    }

}
