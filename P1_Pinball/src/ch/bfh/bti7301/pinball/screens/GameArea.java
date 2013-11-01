package ch.bfh.bti7301.pinball.screens;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	Texture texture;
	Pixmap pixmap;
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
    	 backgroundSprite.setSize(506/10, 900/10 );
     }


 	
    /** Draws a new ball and its physic and set it to the Box2d World.
     * The position and velocity of the ball are controlled by the "launch" key in the field layout JSON.
     */
    public  void setBall() {
    	List<Number> position = layout.getLaunchPosition();
    	float radius = layout.getBallRadius();
    	
		ballBody = Physic.createCircle(world, position.get(0).floatValue(), position.get(1).floatValue(), radius, false);
		ballBody.setBullet(true);
		ballBody.setUserData(drawBall());
//		VPSoundpool.playBall();
    }
    
    /** Draws the ball sprite
     */
    public Sprite drawBall(){
		Sprite ballSprite = new Sprite(new Texture("data/kugel1.png"));
		ballSprite.setSize(3, 3);
		ballSprite.setOrigin(ballSprite.getWidth()/2, ballSprite.getHeight()/2);
		
		return ballSprite;
    }
    
    public void setSlingshot(SlingshotElement slingshot){
    	
    	Map slingshotMap = slingshot.getParameters();
    	List<Number> position = (List<Number>) slingshotMap.get("position");
    	List<Number> length = (List<Number>) slingshotMap.get("length");
    	Vector2 vLength =  new Vector2(length.get(0).floatValue(), length.get(1).floatValue());
    	int kick = (Integer)slingshotMap.get("kick");
    	
    	slingshotBody = Physic.createLine(world, position.get(0).floatValue(), position.get(1).floatValue(), vLength);
    	slingshotBody.setUserData(slingshot);
    }
    
    /*
     * This Method draws the bumper by calling drawBumper() and its corresponding physic by calling Phisic.createCircle
     * with the parameters x and y for the position and the radius
     */
    public  void setBumpers(BumperElement bumper, Float posx, Float posy, Float radius) {
    	
    	HashMap userData = new HashMap();
    	userData.put(bumper, drawBumper(posx, posy, radius));
    	

		bumperBody = Physic.createCircle(world, posx, posy, radius, true);
		bumperBody.setBullet(true);
		
		bumperBody.setUserData(userData);
//		VPSoundpool.playBall();
    }
    
    public Sprite drawBumper(Float posx, Float posy, Float radius){
 
 		//draw a yellow circle
 		pixmap.setColor(1, 1, 0,1);
 		pixmap.fillCircle(256/2,256/2,256/2);
  
 		texture.draw(pixmap, 0, 0);
 		texture.bind();
 		
 		Sprite bumperSprite = new Sprite(texture);
 		bumperSprite.setPosition(posx, posy);
 		bumperSprite.setSize(radius*2, radius*2);
 		
		return bumperSprite;
    	
    }
    
    private void createCollisionListener() {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
//                Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
//                Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });
    }

			

    

	@Override
	public void dispose() {
		//dispose any object you created to free up the memory
		texture.dispose();
		pixmap.dispose();
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
        
        backgroundSprite.draw(batcher);
        
        
        HashMap map = new HashMap();
        //get Bodies of Box2d World and find bodies with userdata(sprite)
		Iterator<Body> itBody = world.getBodies();
		while(itBody.hasNext()){
			Body body = itBody.next();
			if (body.getUserData() instanceof Sprite){
				Sprite sprite = (Sprite) body.getUserData();
				sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
				sprite.draw(batcher);
			}
			if (body.getUserData() instanceof HashMap){
				HashMap bumperMap = (HashMap)body.getUserData();
				Iterator iter = bumperMap.entrySet().iterator();
				 
				while (iter.hasNext()) {
					Map.Entry mEntry = (Map.Entry) iter.next();
					Sprite sprite = (Sprite) mEntry.getValue();
					sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
					sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
					sprite.draw(batcher);
					map.put(mEntry.getKey(), mEntry.getValue());
				}
			}
			
		}
		batcher.end();
		
		
		int numContacts = world.getContactCount();
        if (numContacts > 0) {
//            Gdx.app.log("contact", "start of contact list");
            for (Contact contact : world.getContactList()) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
//                Gdx.app.log("contact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
				 
					if(fixtureB.getBody().getUserData() instanceof HashMap){
						HashMap bumperMap = (HashMap)fixtureB.getBody().getUserData();
						Iterator iter = bumperMap.entrySet().iterator(); 
						
						
						while (iter.hasNext()) {
							Map.Entry mEntry = (Map.Entry) iter.next();
							BumperElement bElement = (BumperElement)mEntry.getKey();
//							bElement.handleCollision(fixtureA.getBody());
							Gdx.app.log("Score: ", bElement.getScore()+"");
							
							Vector2 ballpos = fixtureA.getBody().getWorldCenter();
							Vector2 thisPos = fixtureB.getBody().getPosition();
							float ix = ballpos.x - thisPos.x;
							float iy = ballpos.y - thisPos.y;
							float mag = (float)Math.sqrt(ix*ix + iy*iy);
							
							int kick = (Integer) bElement.getParameters().get("kick");
							float scale = kick / mag;
							fixtureA.getBody().applyLinearImpulse(new Vector2(ix*scale, iy*scale), fixtureA.getBody().getWorldCenter());
						}
					}
//					if(fixtureB.getBody().getUserData() instanceof SlingshotElement){
//						SlingshotElement slingshot = (SlingshotElement)fixtureB.getBody().getUserData();
//						int kick  = (Integer)slingshot.getParameters().get("kick");
//						
//						Vector2 ballpos = fixtureA.getBody().getWorldCenter();
//						Vector2 thisPos = fixtureB.getBody().getPosition();
//						float ix = ballpos.x - thisPos.x;
//						float iy = ballpos.y - thisPos.y;
//						float mag = (float)Math.sqrt(ix*ix + iy*iy);
//						
//						float scale = kick / mag;
//						fixtureA.getBody().applyLinearImpulse(new Vector2(ix*scale, iy*scale), fixtureA.getBody().getWorldCenter());
//					}
//					if(fixtureA.getBody().getUserData() instanceof HashMap){
//						HashMap bumperMap = (HashMap)fixtureA.getBody().getUserData();
//						Iterator iter = bumperMap.entrySet().iterator(); 
//						while (iter.hasNext()) {
//							Map.Entry mEntry = (Map.Entry) iter.next();
//							BumperElement bElement = (BumperElement)mEntry.getKey();
////							bElement.handleCollision(fixtureB.getBody());
//							Gdx.app.log("Score: ", bElement.getScore()+"");
//							
//							
//							Vector2 ballpos = fixtureB.getBody().getWorldCenter();
//							Vector2 thisPos = fixtureA.getBody().getPosition();
//							float ix = ballpos.x - thisPos.x;
//							float iy = ballpos.y - thisPos.y;
//							float mag = (float)Math.sqrt(ix*ix + iy*iy);
//							int kick = (Integer) bElement.getParameters().get("kick");
//							float scale = kick / mag;
//							fixtureB.getBody().applyLinearImpulse(new Vector2(ix*scale, iy*scale), fixtureB.getBody().getWorldCenter());
//						}
//					}
//					if(fixtureA.getBody().getUserData() instanceof SlingshotElement){
//						SlingshotElement slingshot = (SlingshotElement)fixtureA.getBody().getUserData();
//						int kick  = (Integer)slingshot.getParameters().get("kick");
//						
//						Vector2 ballpos = fixtureB.getBody().getWorldCenter();
//						Vector2 thisPos = fixtureA.getBody().getPosition();
//						float ix = ballpos.x - thisPos.x;
//						float iy = ballpos.y - thisPos.y;
//						float mag = (float)Math.sqrt(ix*ix + iy*iy);
//						
//						float scale = kick / mag;
//						fixtureB.getBody().applyLinearImpulse(new Vector2(ix*scale, iy*scale), fixtureB.getBody().getWorldCenter());
//					}
            }
//            Gdx.app.log("contact", "end of contact list");
        }

		//if the screen is touched, the ballbody will be accelerated(launched)
    	List<Float> velocity = layout.getLaunchVelocity();
		if(Gdx.input.isTouched()){
			ballBody.setLinearVelocity(new Vector2(velocity.get(0), velocity.get(1)));
		}
        
//        debugRenderer.render(world, camera.combined);  
   
	}

	@Override
	public void show() {
		world = new World(new Vector2(0.0f, -20f), true);
		

        createBackgroundPhysic();
        createCollisionListener();
        
    	//setup these 3 for rendering- sprite batch will render out textures, and pixmaps allow you to draw on them
		batcher = new SpriteBatch();
		pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
		texture = new Texture(pixmap);
		
	    camera = new OrthographicCamera();
        camera.viewportHeight = 900/10;  
        camera.viewportWidth = 506/10;  
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);  
        camera.update();  
        
        layout = FieldLayout.layoutForLevel("Level1",world);
        

        createBackgroundSprite();
        setBall();
        
       
        List elements = layout.getFieldElements();
        BumperElement bumper = new BumperElement();
        SlingshotElement slingshot =  new SlingshotElement();
        
        for(int i = 0; i<elements.size(); i++){
	        if(elements.get(i) instanceof BumperElement){
	        	bumper = (BumperElement)elements.get(i);
	           	Map bumperMap = bumper.getParameters();
	        	Double radius = (Double)bumperMap.get("radius");
	        	List<Number> position = (List<Number>) bumperMap.get("position");
	        	setBumpers(bumper, position.get(0).floatValue(), position.get(1).floatValue(), radius.floatValue());
	        }
	        if(elements.get(i) instanceof SlingshotElement){
	        	slingshot = (SlingshotElement)elements.get(i);
	            setSlingshot(slingshot);
	        }
        }
        debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}
