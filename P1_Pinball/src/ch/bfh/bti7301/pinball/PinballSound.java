package ch.bfh.bti7301.pinball;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class PinballSound {

	private static HashMap<Integer, Sound> mSoundPoolMap;
	private static boolean soundEnabled = true;
	private static PinballSound instance = null;

	static int ID_LAUNCH = 100;
	static int ID_FLIPPER = 101;
	static int ID_START = 102;
	static int ID_BUMPERS = 103;
	static int ID_SLINGSHOT = 104;
	static int ID_GAMEOVER = 105;

	protected PinballSound() {
		loadSounds();
	}

	/**
	 * Singlton getinstance
	 * @return
	 */
	public static PinballSound getInstance() {
		if (instance == null) {
			instance = new PinballSound();
		}
		return instance;
	}

	/**
	 * Loading sounds from assets to gdx.audio
	 */
	private void loadSounds() {
		mSoundPoolMap = new HashMap<Integer, Sound>();

		mSoundPoolMap.put(ID_START, Gdx.audio.newSound(Gdx.files
				.internal("data/audio/startup1.ogg")));
		mSoundPoolMap.put(ID_LAUNCH, Gdx.audio.newSound(Gdx.files
				.internal("data/audio/mario_jump.ogg")));
		mSoundPoolMap.put(ID_FLIPPER, Gdx.audio.newSound(Gdx.files
				.internal("data/audio/FlipperUp.ogg")));
		mSoundPoolMap
				.put(ID_BUMPERS, Gdx.audio.newSound(Gdx.files
						.internal("data/audio/Bumper.ogg")));
		mSoundPoolMap.put(ID_SLINGSHOT,
				Gdx.audio.newSound(Gdx.files.internal("data/audio/Sling.ogg")));
		mSoundPoolMap.put(ID_GAMEOVER, Gdx.audio.newSound(Gdx.files
				.internal("data/audio/GameOver.ogg")));
	}

	/**
	 * Sound will be played everytime
	 * @param enabled
	 */
	public void setSoundEnabled(boolean enabled) {
		soundEnabled = enabled;
	}

	/**
	 * Play sound listener, actionHandler
	 * @param soundKey
	 */
	void playSound(int soundKey) {
		if (soundEnabled && mSoundPoolMap != null) {
			Sound sound = mSoundPoolMap.get(soundKey);
			if (sound != null) {
				sound.play();
			}
		}
	}

	public void playScore() {

	}

	public void playBall() {
		playSound(ID_LAUNCH);
	}

	public void playFlipper() {
		playSound(ID_FLIPPER);
	}

	public void playStart() {
		playSound(ID_START);
	}

	public void playGameover() {
		playSound(ID_GAMEOVER);
	}

	public void playBumpers() {
		playSound(ID_BUMPERS);
	}

	public void playSlingshot() {
		playSound(ID_SLINGSHOT);
	}

	public void cleanup() {
		mSoundPoolMap.clear();
	}
}
