package pacman;

import javax.sound.sampled.*;
import java.io.*;

/**
 * Preloads audio files needed for game and provides easy access to them.
 */
public class SoundPlayer {

	// Constants
	private static boolean mEnabled = true;

	/**
	 * Enum listing all sounds that the SoundPlayer can play.
	 */
	public enum Sound {
		EAT_PILL (0),
		START (1),
		EAT_GHOST (2),
		DEATH (3),
		GHOSTS_SCARED(4);

		private final int mValue;

		Sound(int value) {
			mValue = value;
		}

		public int value() { return mValue; }
	}

	// Members
	private static Clip[] mClips;

	/**
	 * Initialise the sound player.  Loads all samples into memory.
	 */
	public static void init() {
		mClips = new Clip[Sound.values().length];

		if (isEnabled()) mClips[0] = loadSample("/pacman/resources/sounds/Eat_Small_Pellet.wav");
		if (isEnabled()) mClips[1] = loadSample("/pacman/resources/sounds/Start.wav");
		if (isEnabled()) mClips[2] = loadSample("/pacman/resources/sounds/Eat_Ghost.wav");
		if (isEnabled()) mClips[3] = loadSample("/pacman/resources/sounds/Death.wav");
		if (isEnabled()) mClips[4] = loadSample("/pacman/resources/sounds/Ghosts_Scared.wav");
		
	}

	/**
	 * Check if audio is enabled.
	 * @return True if audio is enabled.
	 */
	public static boolean isEnabled() { return mEnabled == true; }

	/**
	 * Free all resources.
	 */
	public static void shutdown() {
		for (int i = 0; i < mClips.length; ++i) {
			if (mClips[i] != null) mClips[i].close();
		}
	}

	/**
	 * Play the specified sound.
	 */
	public static void playSound(Sound sound) {
		if (mEnabled) {
			mClips[sound.value()].stop();
			mClips[sound.value()].setFramePosition(0);
			mClips[sound.value()].start();
		}
	}

	/**
	 * Play the specified sound.
	 */
	public static void loopSound(Sound sound) {
		if (mEnabled) {
			mClips[sound.value()].stop();
			mClips[sound.value()].setFramePosition(0);
			mClips[sound.value()].loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	/**
	 * Stop the specified sound.
	 */
	public static void stopSound(Sound sound) {
		if (mEnabled) {
			mClips[sound.value()].stop();
		}
	}

	/**
	 * Check if the specified sound is playing.
	 * @param sound Sound to check.
	 * @return True if the sound is playing.
	 */
	public static boolean isSoundPlaying(Sound sound) {
		if (mEnabled) {
			return mClips[sound.value()].isActive();
		} else {
			return false;
		}
	}

	/**
	 * Load the specified sound.
	 * @param filename Filename of the sound to load.
	 * @return A clip containing the sound.
	 */
	private static Clip loadSample(String filename) {

		AudioInputStream input = null;
		Clip clip = null;

		try {
			input = AudioSystem.getAudioInputStream(PacMan.class.getResourceAsStream(filename));

			AudioFormat format = input.getFormat();

			DataLine.Info info = new DataLine.Info(Clip.class,
					format,
					(int)input.getFrameLength() * format.getFrameSize());

			clip = (Clip) AudioSystem.getLine(info);
			clip.open(input);
		} catch (IOException e) {
			util.Debug.print("Cannot open file: " + e);
			mEnabled = false;
			return null;
		} catch (LineUnavailableException e) {
			util.Debug.print("Cannot open line: " + e);
			mEnabled = false;
			return null;
		} catch (UnsupportedAudioFileException e) {
			util.Debug.print("Cannot open audio file: " + e);
			mEnabled = false;
			return null;
		} catch (Exception e) {
			util.Debug.print("Error: " + e);
			mEnabled = false;
			return null;
		}

		return clip;
	}
}
