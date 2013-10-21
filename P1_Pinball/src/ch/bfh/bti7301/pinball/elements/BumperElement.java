package ch.bfh.bti7301.pinball.elements;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.bfh.bti7301.pinball.Physic;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This FieldElement subclass represents a bumper that applies an impulse to a ball when it hits. The impulse magnitude is controlled
 * by the "kick" parameter in the configuration map. 
 *
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class BumperElement extends FieldElement {

	Body pegBody;
	List pegBodySet;
	
	float radius;
	float cx, cy;
	float kick;
	
	public void finishCreate(Map params, World world) {
		List pos = (List)params.get("position");
		this.radius = asFloat(params.get("radius"), 0f);
		this.cx = asFloat(pos.get(0), 0f);
		this.cy = asFloat(pos.get(1), 0f);
		this.kick = asFloat(params.get("kick"), 0f);
		
		pegBody = Physic.createCircle(world, cx, cy, radius, true);
		pegBodySet = Collections.singletonList(pegBody);
	}
	
	@Override
	public List<Body> getBodies() {
		return pegBodySet;
	}



	public static float asFloat(Object obj, float defvalue) {
		if (obj instanceof Number) return ((Number)obj).floatValue();
		return defvalue;
	}
}
