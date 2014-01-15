package ch.bfh.bti7301.pinball.elements;

import static ch.bfh.bti7301.pinball.util.NumFormatUtil.asFloat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.bfh.bti7301.pinball.Physic;
import ch.bfh.bti7301.pinball.PinballSound;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This FieldElement subclass represents a bumper that applies an impulse to a
 * ball when it hits. The impulse magnitude is controlled by the "kick"
 * parameter in the configuration map.
 * 
 * @author Dominik Reubi
 * @version 1.0
 */
public class BumperElement extends FieldElement {

	Body pegBody;
	List pegBodySet;
	Texture texture;
	Pixmap pixmap;
	Sprite bumperSprite;

	float radius;
	float cx, cy;
	float kick;

	public void finishCreate(Map params, World world) {
		List pos = (List) params.get("position");
		this.radius = asFloat(params.get("radius"), 0f);
		this.cx = asFloat(pos.get(0), 0f);
		this.cy = asFloat(pos.get(1), 0f);
		this.kick = asFloat(params.get("kick"), 0f);

		pegBody = Physic.createCircle(world, cx, cy, radius, true);
		pegBody.setBullet(true);
		pegBody.setUserData(this);

		drawBumper(cx, cy, radius);

		pegBodySet = Collections.singletonList(pegBody);
	}

	Vector2 impulseForBall(Body ball) {
		if (this.kick <= 0.01f)
			return null;
		// compute unit vector from center of peg to ball, and scale by kick
		// value to get impulse
		Vector2 ballpos = ball.getWorldCenter();
		Vector2 thisPos = pegBody.getPosition();
		float ix = ballpos.x - thisPos.x;
		float iy = ballpos.y - thisPos.y;
		float mag = (float) Math.sqrt(ix * ix + iy * iy);
		float scale = this.kick / mag;
		return new Vector2(ix * scale, iy * scale);
	}

	public void handleCollision(Body ball) {
		Vector2 impulse = this.impulseForBall(ball);
		if (impulse != null) {
			ball.applyLinearImpulse(impulse, ball.getWorldCenter());
		}
		PinballSound.getInstance().playBumpers();
	}

	/**
	 * This Method draws the bumper by calling drawBumper() and its
	 * corresponding physic by calling Physic.createCircle with the parameters x
	 * and y for the position and the radius
	 */
	public Sprite drawBumper(Float posx, Float posy, Float radius) {

		pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
		texture = new Texture(pixmap);
		// draw a yellow circle
		pixmap.setColor(1, 1, 0, 1);
		pixmap.fillCircle(256 / 2, 256 / 2, 256 / 2);

		texture.draw(pixmap, 0, 0);
		texture.bind();

		bumperSprite = new Sprite(texture);
		bumperSprite.setPosition(posx, posy);
		bumperSprite.setSize(radius * 2, radius * 2);

		bumperSprite.setPosition(
				pegBody.getPosition().x - bumperSprite.getWidth() / 2,
				pegBody.getPosition().y - bumperSprite.getHeight() / 2);
		bumperSprite.setRotation(pegBody.getAngle()
				* MathUtils.radiansToDegrees);

		return bumperSprite;

	}

	/**
	 * getters
	 */
	public List<Body> getBodies() {
		return pegBodySet;
	}

	public Sprite getSprite() {
		return bumperSprite;
	}

	public float getX() {
		return cx;
	}

	public float getY() {
		return cy;
	}

	public float getRadius() {
		return radius;
	}

	public float getKick() {
		return kick;
	}

}
