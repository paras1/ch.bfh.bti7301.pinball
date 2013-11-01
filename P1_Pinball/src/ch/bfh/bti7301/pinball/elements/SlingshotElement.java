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

	Body pegBody;
	List pegBodySet;
	
	float cx, cy;
	float kick;
	
	public void finishCreate(Map params, World world) {

	}
	
	@Override
	public List<Body> getBodies() {
		return pegBodySet;
	}
	
}
