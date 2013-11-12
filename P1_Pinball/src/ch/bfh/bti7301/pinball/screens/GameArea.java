package ch.bfh.bti7301.pinball.screens;

import java.util.HashMap;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import aurelienribon.bodyeditor.BodyEditorLoader;
import ch.bfh.bti7301.pinball.FieldLayout;
import ch.bfh.bti7301.pinball.Physic;
import ch.bfh.bti7301.pinball.elements.BumperElement;
import ch.bfh.bti7301.pinball.elements.SlingshotElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.net.httpserver.Filter.Chain;

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
    private static final float BOX_STEP=1/60f;  
    private static final int BOX_VELOCITY_ITERATIONS=8;  
    private static final int BOX_POSITION_ITERATIONS=3; 
    private static final float BACKGROUND_WIDTH = 506/10;
    private FieldLayout layout;
    
	private Body backgroundModel;
	private Texture backgroundTexture;
	private Sprite backgroundSprite;

 
	//things needed to draw
	private SpriteBatch batcher;

	private Body ballBody;
	private Body bumperBody;
	private Body slingshotBody;
    
	private PinballGame game;



    // constructor to keep a reference to the main Game class
     public GameArea(PinballGame game){
             this.game = game;
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
		ballSprite.setSize(3, 3);
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
        
         
		//start the batcher, so we would want to do all of our draw calls between batcher.begin and .end
		batcher.setProjectionMatrix(camera.combined);
        batcher.begin();
        
        //Draw the background
        backgroundSprite.draw(batcher);
        
        List elements = layout.getFieldElements();
        
        //Draw the bumpers
        for(int i = 0; i<elements.size(); i++){
	        if(elements.get(i) instanceof BumperElement){
	        	BumperElement bump = (BumperElement)elements.get(i);
	        	bump.getSprite().draw(batcher);
	        }
        }

        //Draw the ball
		getBallSprite().draw(batcher);   

		batcher.end();
		
		//find objects that collide in our pinballworld during rendering process
		Physic.doCollisionDetection(world);


		//if the screen is touched, the ballbody will be accelerated(launched)
    	List<Float> velocity = layout.getLaunchVelocity();
		if(Gdx.input.isTouched()){
			ballBody.setLinearVelocity(new Vector2(velocity.get(0), velocity.get(1)));
		}
        
        debugRenderer.render(world, camera.combined);  
   
	}

	@Override
	public void show() {
		world = new World(new Vector2(0.0f, -20f), true);
		

        createBackgroundPhysic();
        Physic.createCollisionListener(world);
        
        batcher = new SpriteBatch();
        
	    camera = new OrthographicCamera();
        camera.viewportHeight = 900/10;  
        camera.viewportWidth = 506/10;  
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);  
        camera.update();  
        
        layout = FieldLayout.layoutForLevel("Level1",world);
        

        createBackgroundSprite();
        setBall();
        
        debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
