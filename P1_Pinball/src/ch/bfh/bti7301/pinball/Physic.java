package ch.bfh.bti7301.pinball;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
	
	/** Creates a circle object with the given position and radius.
	 */
	public static Body createCircle(World world, float x, float y, float radius, boolean isStatic) {
	
		CircleShape sd = new CircleShape();
		sd.setRadius(radius);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = sd;
		fdef.density = 1.0f;
		fdef.friction = 0.3f;
		fdef.restitution = 0.6f;
		
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
		
		
		PolygonShape line = new PolygonShape();
		ChainShape chain = new ChainShape();
		chain.createChain(new Vector2[]{direction, new Vector2(0,0)});
//		line.setAsBox(direction.x, direction.y);
		
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

}
