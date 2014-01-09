package ch.bfh.bti7301.pinball;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import sun.tools.tree.ThisExpression;

public class EditorBuffer {

	private Map mpNewBoardBuffer = new HashMap();
	private ArrayList<Map> alElementsBuffer = new ArrayList();
	private Map mpLaunchBuffer = new HashMap();
	private int numBumpers = 0;
	private int actualBumperNum = 0;
	private float actualBumperRadius = 5f;
	private int actualBumperKick = 0;
	private int	actualBumperPoints = 0;
	private String levelname = "";
	private int numBalls = 0;
	private int launcherSpeed = 0;


	private static EditorBuffer instance = null;
	
	public static EditorBuffer getInstance(){
		 if(instance == null) {
	         instance = new EditorBuffer();
	     }
	     return instance;
	}
	
	public Map getMpNewBoardBuffer() {
		return mpNewBoardBuffer;
	}

	public void setMpNewBoardBuffer(Map mpNewBoardBuffer) {
		this.mpNewBoardBuffer = mpNewBoardBuffer;
		setAlElementsBuffer((ArrayList)mpNewBoardBuffer.get("elements"));
		System.out.println(getAlElementsBuffer());
		setMpLaunchBuffer((HashMap)mpNewBoardBuffer.get("launch"));
		System.out.println(getMpLaunchBuffer());
	}
	
	public void putMpNewBoardBufferElement(Object key, Object value) {
		this.mpNewBoardBuffer.put(key, value);
	}
	
	public ArrayList getAlElementsBuffer() {
		return alElementsBuffer;
	}

	public void setAlElementsBuffer(ArrayList alElementsBuffer) {
		this.alElementsBuffer = alElementsBuffer;
	}
	
	public void addAlElementsBufferElement(Map map) {
		
		this.alElementsBuffer.add(map);
	}

	public Map getMpLaunchBuffer() {
		return mpLaunchBuffer;
	}

	public void setMpLaunchBuffer(Map mpLaunchBuffer) {
		this.mpLaunchBuffer = mpLaunchBuffer;
	}
	
	public void putMpLaunchBufferElement(Object key, Object value) {
		this.mpLaunchBuffer.put(key, value);
	}

	public String getLevelname() {
		return levelname;
	}

	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}

	public int getNumBalls() {
		return numBalls;
	}

	public void setNumBalls(int numBalls) {
		this.numBalls = numBalls;
	}

	public int getLauncherSpeed() {
		return launcherSpeed;
	}

	public void setLauncherSpeed(int launcherSpeed) {
		this.launcherSpeed = launcherSpeed;
	}

	public int getNumBumpers() {
		return numBumpers;
	}

	public void setNumBumpers(int numBumpers) {
		this.numBumpers = numBumpers;
	}

	public int getActualBumperNum() {
		return actualBumperNum;
	}

	public void setActualBumperNum(int actualBumperNum) {
		this.actualBumperNum = actualBumperNum;
	}

	public float getActualBumperRadius() {
		return actualBumperRadius;
	}

	public void setActualBumperRadius(float actualBumperRadius) {
		this.actualBumperRadius = actualBumperRadius;
	}
	
	
	public int getActualBumperKick() {
		return actualBumperKick;
	}

	public void setActualBumperKick(int actualBumperKick) {
		this.actualBumperKick = actualBumperKick;
	}

	public int getActualBumperPoints() {
		return actualBumperPoints;
	}

	public void setActualBumperPoints(int actualBumperPoints) {
		this.actualBumperPoints = actualBumperPoints;
	}

	public void JSONWriter() {
		
		JSONObject jsob = new JSONObject(getMpNewBoardBuffer());
		
		FileHandle fh = Gdx.files.local("local/tables/"+getLevelname()+".json");
		String filename = fh.file().getAbsolutePath();
		System.out.println(filename);
		FileWriter file;
		try {
			file = new FileWriter(filename);
			jsob.write(file);
			file.flush();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
