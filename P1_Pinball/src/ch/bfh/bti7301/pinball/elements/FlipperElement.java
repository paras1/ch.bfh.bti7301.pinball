package ch.bfh.bti7301.pinball.elements;

import static ch.bfh.bti7301.pinball.util.NumFormatUtil.asFloat;
import static ch.bfh.bti7301.pinball.util.NumFormatUtil.toRadians;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.bfh.bti7301.pinball.Physic;
import ch.bfh.bti7301.pinball.screens.GameArea;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;


public class FlipperElement extends FieldElement {

	Body flipperBody;
	List<Body> flipperBodySet;
	public Body anchorBody;
	public RevoluteJoint joint;
	RevoluteJointDef jointDef;

	float flipperLength;
	float upspeed, downspeed;
	float minangle, maxangle;
	float cx, cy;

	public void finishCreate (Map params, World world) {
		List pos = (List)params.get("position");

		this.cx = asFloat(pos.get(0));
		this.cy = asFloat(pos.get(1));
		this.flipperLength = asFloat(params.get("length"));
		this.minangle = toRadians(asFloat(params.get("minangle")));
		this.maxangle = toRadians(asFloat(params.get("maxangle")));
		this.upspeed = asFloat(params.get("upspeed"));
		this.downspeed = asFloat(params.get("downspeed"));

		this.anchorBody = Physic.createCircle(world, this.cx, this.cy, 0.05f, true);
		// joint angle is 0 when flipper is horizontal
		// flipper needs to be slightly extended past anchorBody to rotate correctly
		float ext = (this.flipperLength > 0) ? -0.05f : +0.05f;
		// width larger than 0.12 slows rotation?
		this.flipperBody = Physic.createWall(world, cx + ext, cy - 0.12f, cx + flipperLength, cy + 0.12f, 0f, 0f);
		flipperBody.setType(BodyDef.BodyType.DynamicBody);
		flipperBody.setBullet(true);
		flipperBody.getFixtureList().get(0).setDensity(5.0f);

		jointDef = new RevoluteJointDef();
		jointDef.initialize(anchorBody, flipperBody, new Vector2(this.cx, this.cy));
		jointDef.enableLimit = true;
		jointDef.enableMotor = true;
		// counterclockwise rotations are positive, so flip angles for flippers extending left
		jointDef.lowerAngle = (this.flipperLength > 0) ? this.minangle : -this.maxangle;
		jointDef.upperAngle = (this.flipperLength > 0) ? this.maxangle : -this.minangle;
		jointDef.maxMotorTorque = 1000f;

		this.joint = (RevoluteJoint)world.createJoint(jointDef);

		flipperBodySet = (List<Body>) Collections.singleton(flipperBody);
		this.setEffectiveSpeed(-this.downspeed); // force flipper to bottom when field is first created
	}

	/** Returns true if the flipper rotates around its right end, which requires negating some values. */
	boolean isReversed () {
		return (flipperLength < 0);
	}

	/** Returns the motor speed of the Box2D joint, normalized to be positive when the flipper is moving up. */
	float getEffectivSpeed () {
		float speed = joint.getMotorSpeed();
		return (isReversed()) ? -speed : speed;
	}

	/** Sets the motor speed of the Box2D joint, positive values move the flipper up. */
	void setEffectiveSpeed (float speed) {
		if (isReversed()) speed = -speed;
		joint.setMotorSpeed(speed);
	}

	public List<Body> getBodies () {
		return flipperBodySet;
	}

	public boolean shouldCallTick () {
		return true;
	}

	//Zum Aktivieren der Flippers, muesste die Gamearea od. FieldElement (jenachdem wie es geloest wird) mitgegeben werden und mit dem ContactListener (ist das Interface welches die tick(Gamearea area) Methode enthaelt verbunden
	
	public void tick (GameArea area) {
		//super.tick(area);			Zu beachten, tick(GameArea area) Methode muss noch implementiert werden)

		// if angle is at maximum, reduce speed so that the ball won't fly off when it hits
		if (getEffectivSpeed() > 0.5f) {
			float topAngle = (isReversed()) ? jointDef.lowerAngle : jointDef.upperAngle;
			if (Math.abs(topAngle - joint.getJointAngle()) < 0.05) {
				setEffectiveSpeed(0.5f);
			}
		}
	}

	public boolean isFlipperActivated () {
		return getEffectivSpeed() > 0;
	}

	public void setFlipperActivated (boolean active) {
		// only adjust speed if state is changing, so we don't accelerate flipper that's been slowed down in tick()
		if (active != this.isFlipperActivated()) {
			float speed = (active) ? upspeed : -downspeed;
			setEffectiveSpeed(speed);
		}
	}

	public float getFlipperLength () {
		return flipperLength;
	}

	public RevoluteJoint getJoint () {
		return joint;
	}

	public Body getAnchorBody () {
		return anchorBody;
	}

// FLIPPER Platziermethode. -> Vielleicht kannst du was anfangen damit
	
//	public void draw () {
//		// draw single line segment from anchor point
//		Vector2 position = anchorBody.getPosition();
//		float angle = joint.getJointAngle();
//		// HACK: angle can briefly get out of range, always draw between min and max
//		if (angle < jointDef.lowerAngle) angle = jointDef.lowerAngle;
//		if (angle > jointDef.upperAngle) angle = jointDef.upperAngle;
//		float x1 = position.x;
//		float y1 = position.y;
//		float x2 = position.x + flipperLength * (float)Math.cos(angle);
//		float y2 = position.y + flipperLength * (float)Math.sin(angle);
//
//
//	}
}

