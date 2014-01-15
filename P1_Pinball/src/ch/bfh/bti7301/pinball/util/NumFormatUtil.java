package ch.bfh.bti7301.pinball.util;

/**
 * NumFormatUtil, util class to get the right number formats
 * 
 * @author Sathesh Paramasamy
 * 
 */
public class NumFormatUtil {

	/**
	 * returns a object as float value if the object isn't a numeric value than
	 * it returns a default value
	 * 
	 * @param obj
	 * @param defvalue
	 * @return
	 */
	public static float asFloat(Object obj, float defvalue) {
		if (obj instanceof Number)
			return ((Number) obj).floatValue();
		return defvalue;
	}

	/**
	 * returns a object as float value
	 * 
	 * @param obj
	 * @return
	 */
	public static float asFloat(Object obj) {
		return asFloat(obj, 0f);
	}

	/**
	 * converts a float value to radians (also in float)
	 * 
	 * @param degrees
	 * @return
	 */
	public static float toRadians(float degrees) {
		return (float) (Math.PI / 180) * degrees;
	}

}
