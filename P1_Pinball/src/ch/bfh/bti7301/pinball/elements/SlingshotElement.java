package ch.bfh.bti7301.pinball.elements;

import static ch.bfh.bti7301.pinball.util.NumFormatUtil.asFloat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.bfh.bti7301.pinball.Physic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This FieldElement subclass represents a slingshotline that applies an impulse to a ball when it hits. The impulse magnitude is controlled
 * by the "kick" parameter in the configuration map. 
 *
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class SlingshotElement extends FieldElement {

	Body slingshotBody;
	List slingshotBodySet;
	
	float px, py;
	float lx, ly;
	float kick;
	
	
	public void finishCreate(Map params, World world) {
		List pos = (List)params.get("position");
		List length = (List)params.get("length");
		
		this.px = asFloat(pos.get(0), 0f);
		this.py = asFloat(pos.get(1), 0f);
		this.lx = asFloat(length.get(0), 0f);
		this.ly = asFloat(length.get(1), 0f);
		this.kick = asFloat(params.get("kick"), 0f);
		
		Vector2 vLength =  new Vector2(lx, ly);
    	
		slingshotBody = Physic.createLine(world, px, py, vLength);
		slingshotBody.setUserData(this);
		slingshotBody.setBullet(true);

		slingshotBodySet = Collections.singletonList(slingshotBody);
	}
	
	@Override
	public List<Body> getBodies() {
		return slingshotBodySet;
	}
	
	Vector2 impulseForBall(Body ball) {
		if (this.kick <= 0.01f) return null;
		// compute unit vector from center of peg to ball, and scale by kick value to get impulse
		Vector2 ballpos = ball.getWorldCenter();
		Vector2 thisPos = slingshotBody.getPosition();
		float ix = ballpos.x - thisPos.x;
		float iy = ballpos.y - thisPos.y;
//		float mag = (float)Math.sqrt(ix*ix + iy*iy);
		float scale = this.kick; // mag;
		return new Vector2(ix*scale, iy*scale);
//		return null;
	}

	
	public void handleCollision(Body ball) {
		Vector2 impulse = this.impulseForBall(ball);
		if (impulse!=null) {
			ball.applyLinearImpulse(impulse, ball.getWorldCenter());
		}
	}
	
}
