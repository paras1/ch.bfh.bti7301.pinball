package ch.bfh.bti7301.pinball;

import java.util.HashMap;
import android.media.SoundPool;

import com.sun.tools.javac.util.Context;

public class SoundPlayer {

	public static final int S1 = R.raw.s1;
	public static final int S2 = R.raw.s2;
	public static final int S3 = R.raw.s3;
	private static SoundPool soundPool;
	private static HashMap soundPoolMap;

	/** Populate the SoundPool */
	public static void initSounds(Context context) {
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap(3);

		soundPoolMap.put(S1, soundPool.load(context, R.raw.s1, 1));
		soundPoolMap.put(S2, soundPool.load(context, R.raw.s2, 2));
		soundPoolMap.put(S3, soundPool.load(context, R.raw.s3, 3));
	}

	/** Play a given sound in the soundPool */
	public static void playSound(Context context, int soundID) {
	if(soundPool == null || soundPoolMap == null){
	   initSounds(context);
	}
	    float volume = ....// whatever in the range = 0.0 to 1.0

	    // play sound with same right and left volume, with a priority of 1, 
	    // zero repeats (i.e play once), and a playback rate of 1f
	    soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, 0, 1f);
	 }
}
