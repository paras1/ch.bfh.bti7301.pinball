package ch.bfh.bti7301.pinball;

import static ch.bfh.bti7301.pinball.util.NumFormatUtil.asFloat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.bfh.bti7301.pinball.elements.FieldElement;
import ch.bfh.bti7301.pinball.elements.FlipperElement;
import ch.bfh.bti7301.pinball.util.JSONUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The FieldLayout class get the saved GameAreas(json file) 
 * and provide getters and setters for the elements and their parameters like bumper size
 * 
 *
 * @author Dominik Reubi(reubd1@bfh.ch)
 * @version 1.0
 */
public class FieldLayout {
	
	static int _numLevels = -1;
	static Map<Object, Map> _layoutMap = new HashMap();
	Random randomno = new Random();
	float random = randomno.nextFloat();
    // check next float value  
	
	//random = random.nextFloat();
//	System.out.println("Next float value: ");
	
	public static Map readFieldLayout(String  name, boolean isGame) {
		try {
			String assetPath = "tables/"+ name;
			
			FileHandle fh;
			if(isGame){
				fh = Gdx.files.internal("data/"+assetPath);
			} else {
				fh = Gdx.files.local("local/tables/"+name);
				System.out.println("Getting Layout for: "+name);
			}
			InputStream fin = fh.read();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));

			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line=br.readLine())!=null) {
				buffer.append(line);
			}
			fin.close();
			Map layoutMap = JSONUtils.mapFromJSONString(buffer.toString());
			return layoutMap;
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static FieldLayout layoutForLevel(String name, World world, boolean isGame) {
		Map levelLayout = _layoutMap.get(name);
		if (levelLayout==null) {
			levelLayout = readFieldLayout(name, isGame);
			_layoutMap.put(name, levelLayout);
		}
		return new FieldLayout(levelLayout, world);
	}
	
	List<FieldElement> fieldElements = new ArrayList<FieldElement>();
	List<FlipperElement> flippers;
	float width;
	float height;
	List<Integer> ballColor;
	float targetTimeRatio;
	Map allParameters;
	
	static List<Integer> DEFAULT_BALL_COLOR = Arrays.asList(255, 0, 0);

	static List listForKey(Map map, String key) {
		if (map.containsKey(key)) return (List)map.get(key);
		return Collections.EMPTY_LIST;
	}
	
	public List addFieldElements(Map layoutMap, String key, Class defaultClass, World world) {
		List elements = new ArrayList();
		for(Object obj : listForKey(layoutMap, key)) {
			// allow strings in JSON for comments
			if (!(obj instanceof Map)) continue;
			Map params = (Map)obj;
			elements.add(FieldElement.createFromParameters(params, world, defaultClass));
		}
		fieldElements.addAll(elements);
		return elements;
	}
	
	public List<FieldElement> getFieldElements() {
		return fieldElements;
	}
	
	public FieldLayout(Map layoutMap, World world) {
		this.width = asFloat(layoutMap.get("width"), 20.0f);
		this.height = asFloat(layoutMap.get("height"), 30.0f);
		this.ballColor = (layoutMap.containsKey("ballcolor")) ? (List<Integer>)layoutMap.get("ballcolor") : DEFAULT_BALL_COLOR;
		this.allParameters = layoutMap;
		
		flippers = addFieldElements(layoutMap, "flippers", FlipperElement.class, world);

		
		addFieldElements(layoutMap, "elements", null, world);
	}
	
	public List<FlipperElement> getFlipperElements() {
		return flippers;
	}


	
	public float getBallRadius() {
		return asFloat(allParameters.get("ballradius"), 0.5f);
	}
	
	public List<Integer> getBallColor() {
		return ballColor;
	}
	
	public int getNumberOfBalls() {
		return (allParameters.containsKey("numballs")) ? ((Number)allParameters.get("numballs")).intValue() : 3;
	}
	
	public List<Number> getLaunchPosition() {
		Map launchMap = (Map)allParameters.get("launch");
		return (List<Number>)launchMap.get("position");
	}
	
	public List<Number> getLaunchDeadZone() {
		Map launchMap = (Map)allParameters.get("launch");
		return (List<Number>)launchMap.get("deadzone");
	}

	public List<Float> getLaunchVelocity() {
		Map launchMap = (Map)allParameters.get("launch");
		List<Number> velocity = (List<Number>)launchMap.get("velocity");
		float vx = velocity.get(0).floatValue();
		float vy = velocity.get(1).floatValue();
		vy = vy + random;
		
		return Arrays.asList(vx, vy);
	}
	
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	
}
