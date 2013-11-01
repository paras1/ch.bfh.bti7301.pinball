package ch.bfh.bti7301.pinball.elements;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/** Abstract superclass of all elements in the pinball field, such as bumpers and flippers.
 *  
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public abstract class FieldElement {

	Map parameters;
	World box2dWorld;
	String elementID;
	int[] color; // 3-element r,g,b values between 0 and 255
	long score = 0;
	
	
	/** Creates and returns a FieldElement object from the given map of parameters. The default class to instantiate is an argument to this method,
	 * and can be overridden by the "class" property of the parameter map. Calls the no-argument constructor of the default or custom class, and
	 * then calls initialize() passing the parameter map and World.
	 */
	public static FieldElement createFromParameters(Map params, World world, Class defaultClass) {
		try {
			if (params.containsKey("class")) {
				// if package not specified, use this package
				String className = (String)params.get("class");
				if (className.indexOf('.')==-1) {
					className = "ch.bfh.bti7301.pinball.elements." + className;
				}
				defaultClass = Class.forName(className);
			}
			
			FieldElement self = (FieldElement)defaultClass.newInstance();
			self.initialize(params, world);
			return self;
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/** Extracts common values from the definition parameter map, and calls finishCreate to allow subclasses to further initialize themselves.
	 * Subclasses should override finishCreate, and should not override this method.
	 */
	public void initialize(Map params, World world) {
		this.parameters = params;
		this.box2dWorld = world;
		this.elementID = (String)params.get("id");
		
		List<Integer> colorList = (List<Integer>)params.get("color");
		if (colorList!=null) {
			this.color = new int[] {colorList.get(0), colorList.get(1), colorList.get(2)};
		}
		
		if (params.containsKey("score")) {
			this.score = ((Number)params.get("score")).longValue();
		}
		this.finishCreate(params, world);
		
	}
	


	/** Must be overridden by subclasses to return a collection of all Box2D bodies which make up this element.
	 */
	public abstract List<Body> getBodies();


	/** Must be overridden by subclasses, which should perform any setup required after creation.
	 */
	public abstract void finishCreate(Map params, World world);
	
	/** Returns this element's ID as specified in the JSON definition, or null if the ID is not specified.
	 */
	public String getElementID() {
		return elementID;
	}
	
	/** Returns the parameter map from which this element was created.
	 */
	public Map getParameters() {
		return parameters;
	}

	/** Returns the "score" value for this element. The score is automatically added when the element is hit by a ball, and elements
	 * may apply scores under other conditions, e.g. RolloverGroupElement adds the score when a ball comes within range of a rollover.
	 */
	public long getScore() {
		return score;
	}
	
}
