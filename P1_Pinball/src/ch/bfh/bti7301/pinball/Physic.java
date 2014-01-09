package ch.bfh.bti7301.pinball;

import ch.bfh.bti7301.pinball.elements.BumperElement;
import ch.bfh.bti7301.pinball.elements.SlingshotElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This Class contains generic methods to draw the required elements(e.g. CircleShape for bumpers)
 * 
 *
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class Physic {
	
	private static GameState gameState = GameState.getInstance();
	
	/** Creates a circle object with the given position and radius.
	 */
	public static Body createCircle(World world, float x, float y, float radius, boolean isStatic) {
	
		CircleShape sd = new CircleShape();
		sd.setRadius(radius);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = sd;
		fdef.density = 0.5f;
		fdef.friction = 0.1f;
		fdef.restitution = 0.0f;

		/*fdef.density = 0.7f;
		fdef.friction = 0.3f;
		fdef.restitution = 0.6f;*/
		
		BodyDef bd = new BodyDef();
		bd.allowSleep = true;
		bd.position.set(x, y);
		Body body = world.createBody(bd);
		body.createFixture(fdef);
		if (isStatic) {
			body.setType(BodyDef.BodyType.StaticBody);
		}
		else {
			body.setType(BodyDef.BodyType.DynamicBody);
		}
		return body;
		
		
	}
	
	/** Creates a line object with the given position.
	 */
	public static Body createLine(World world, float x, float y, Vector2 direction) {
		
		
		ChainShape chain = new ChainShape();
		chain.createChain(new Vector2[]{direction, new Vector2(0,0)});
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = chain;
		
		fdef.density = 1.0f;
		fdef.friction = 0.3f;
		fdef.restitution = 0.6f;
		
		BodyDef bd = new BodyDef();
		bd.allowSleep = true;
		bd.position.set(x, y);
		Body body = world.createBody(bd);
		body.createFixture(fdef);
		
		return body;
	
	}
	
	public static Body createWall (World world, float xmin, float ymin, float xmax, float ymax, float angle, float restitution) {
		float cx = (xmin + xmax) / 2;
		float cy = (ymin + ymax) / 2;
		float hx = (xmax - xmin) / 2;
		float hy = (ymax - ymin) / 2;
		if (hx < 0) hx = -hx;
		if (hy < 0) hy = -hy;
		PolygonShape wallshape = new PolygonShape();
		wallshape.setAsBox(hx, hy, new Vector2(0f, 0f), angle);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = wallshape;
		fdef.density = 1.0f;
		if (restitution > 0) fdef.restitution = restitution;

		BodyDef bd = new BodyDef();
		bd.position.set(cx, cy);
		Body wall = world.createBody(bd);
		wall.createFixture(fdef);
		wall.setType(BodyDef.BodyType.StaticBody);
		return wall;
	}
	
    public static void createCollisionListener(World world) {
        world.setContactListener(new ContactListener() {
	
	            @Override
	            public void beginContact(Contact contact) {
		                Fixture fixtureA = contact.getFixtureA();
		                Fixture fixtureB = contact.getFixtureB();
		                
		                if(fixtureB.getBody().getUserData() instanceof BumperElement){
							
							BumperElement bElement = (BumperElement)fixtureB.getBody().getUserData();
							Gdx.app.log("Score: ", bElement.getScore()+"");
							gameState.addScore(bElement.getScore());
						
							bElement.handleCollision(fixtureA.getBody());
						
						}
						if(fixtureA.getBody().getUserData() instanceof BumperElement){
							
							BumperElement bElement = (BumperElement)fixtureA.getBody().getUserData();
							Gdx.app.log("Score: ", bElement.getScore()+"");
							gameState.addScore(bElement.getScore());							

							bElement.handleCollision(fixtureB.getBody());
			
						}
						if(fixtureB.getBody().getUserData() instanceof SlingshotElement){
							Gdx.app.log("Slingshot touched: ", "");
							SlingshotElement slingshotElement = (SlingshotElement)fixtureB.getBody().getUserData();
							
							slingshotElement.handleCollision(fixtureA.getBody());
						
						}
						if(fixtureA.getBody().getUserData() instanceof SlingshotElement){
							Gdx.app.log("Slingshot touched: ", "");
							SlingshotElement slingshotElement = (SlingshotElement)fixtureA.getBody().getUserData();
							
							slingshotElement.handleCollision(fixtureB.getBody());
			
						}
	            
			
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });
    }

}
