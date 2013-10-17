package ch.bfh.bti7301.pinball;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameArea implements Screen {
	
    World world;  
    Box2DDebugRenderer debugRenderer;  
    OrthographicCamera camera;  
    static final float BOX_STEP=1/60f;  
    static final int BOX_VELOCITY_ITERATIONS=8;  
    static final int BOX_POSITION_ITERATIONS=3;  
    static final float WORLD_TO_BOX=0.01f;  
    static final float BOX_WORLD_TO=100f;

 
	//things needed to draw
	SpriteBatch batcher;
	Texture texture;
	Pixmap pixmap;
    
	PinballGame game;
	 

    // constructor to keep a reference to the main Game class
     public GameArea(PinballGame game){
             this.game = game;
     }
     
     
 	private void DrawCircle()
 	{
  
 		//draw a yellow circle
 		pixmap.setColor(1, 1, 0,1);
 		pixmap.fillCircle(128,128,10);
  
 		texture.draw(pixmap, 0, 0);
 		texture.bind();
 	}
    

	@Override
	public void dispose() {
		//dispose any object you created to free up the memory
				texture.dispose();
				pixmap.dispose();
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
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  
        world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS); 
         
		//start the batcher, so we would want to do all of our draw calls between batcher.begin and .end
		batcher.setProjectionMatrix(camera.combined);
        batcher.begin();
        
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
		}
		batcher.end();
        
        debugRenderer.render(world, camera.combined);  
   
	}

	@Override
	public void show() {
		world = new World(new Vector2(0, -200), true);
	    camera = new OrthographicCamera();  
        camera.viewportHeight = 320;  
        camera.viewportWidth = 480;  
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);  
        camera.update();  
        
        
        
        //Ground body  
        BodyDef groundBodyDef =new BodyDef();
        FixtureDef fixtureDef1 = new FixtureDef(); 
//      
        
        // GROUND
        // body definition
        groundBodyDef.type = BodyType.StaticBody;
        groundBodyDef.position.set(10, 50);

        // ground shape
        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[] {new Vector2(100, 10), new Vector2(300, 10)});

        // fixture definition
        fixtureDef1.shape = groundShape;
        fixtureDef1.friction = 3.0f;
        fixtureDef1.restitution = 3;
        fixtureDef1.density = 3.0f;

        world.createBody(groundBodyDef).createFixture(fixtureDef1);

        groundShape.dispose();
        
        //Triangle
        Vector2[] vertices = new Vector2[]{new Vector2(100,-100/2), new Vector2(100/2,100/2),new Vector2(-100/2,100/2)};
        
        PolygonShape triangleShape = new PolygonShape();
        triangleShape.set(vertices);
        groundBodyDef.position.set(camera.viewportWidth /2, camera.viewportHeight / 2); 

        // fixture definition
        fixtureDef1.shape = triangleShape;
        fixtureDef1.friction = 0.0f;
        fixtureDef1.restitution = 0.0f;
        fixtureDef1.density = 1.0f;

        world.createBody(groundBodyDef).createFixture(fixtureDef1);
        triangleShape.dispose();
        
        
        //Dynamic Body  
       
        BodyDef bodyDef = new BodyDef();  
        bodyDef.type = BodyType.DynamicBody;  
//        bodyDef.position.set(camera.viewportWidth /2, camera.viewportHeight / 2);  
        bodyDef.position.set(new Vector2(260, 65));
        Body body = world.createBody(bodyDef);  
        CircleShape dynamicCircle = new CircleShape();
        dynamicCircle.setRadius(10f);  
        FixtureDef fixtureDef = new FixtureDef();  
        fixtureDef.shape = dynamicCircle;  
        fixtureDef.density = 5;  
        fixtureDef.friction = .75f;  
        fixtureDef.restitution = .1f;
        body.createFixture(fixtureDef);  
        
        dynamicCircle.dispose();
  

    	//setup these 3 for rendering- sprite batch will render out textures, and pixmaps allow you to draw on them
		batcher = new SpriteBatch();
		pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
		texture = new Texture(pixmap);

 
		//function to draw a smiley face on the pixmap, which we can use to draw on the texture
		DrawCircle();

		Sprite ballSprite = new Sprite(new Texture(pixmap));
		ballSprite.setOrigin(ballSprite.getWidth()/2, ballSprite.getHeight()/2);
		body.setUserData(ballSprite);       
		body.setBullet(true);
		body.setLinearVelocity(new Vector2(0,200));
		

        debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}
