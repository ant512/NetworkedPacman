package pacman;

/**
 * Handles the power pill timer, and keeps track of the number of ghosts that
 * have been eaten whilst a pill is active.  Note that collecting a second pill
 * while the first is still active makes the timer reset to maximum (so pill
 * times are not cumulative) but ghost count is not (so ghost count *is*
 * cumulative).
 */
public class PowerPillTimer {

	// Constants
	final private static int TIME = 400;
	final private static int GHOST_FLASH_TIME = 100;

	// Members
	private static int mTimer = -1;
	private static int mGhostsEaten = 0;

	/**
	 * Start the timer.
	 */
	public static void start() {
		mTimer = TIME;

		//Make ghosts edible
		PacManGame.getGame().getPlayerList().makeNormalEdible();

		SoundPlayer.loopSound(SoundPlayer.Sound.GHOSTS_SCARED);
	}

	/**
	 * Stop the timer.
	 */
	public static void stop() {
		mTimer = -1;
		mGhostsEaten = 0;

		// Reset ghosts back to normal
		PacManGame.getGame().getPlayerList().makeFlashingNormal();

		SoundPlayer.stopSound(SoundPlayer.Sound.GHOSTS_SCARED);
	}

	/**
	 * Run the timer.
	 */
	public static void run() {
		if (mTimer > 0) {

			mTimer--;

			switch (mTimer) {
				case GHOST_FLASH_TIME:
				
					// Make edible ghosts flash
					PacManGame.getGame().getPlayerList().makeEdibleFlash();
					break;

				case 0:
					stop();
					break;
			}
		}
	}

	/**
	 * Get the timer.
	 * @return The timer.
	 */
	public static int getTimer() { return mTimer; }

	/**
	 * Get the number of ghosts eaten during this timer sequence.
	 * @return The number of ghosts eaten.
	 */
	public static int getGhostsEaten() { return mGhostsEaten; }

	/**
	 * Add a ghost to the tally of ghosts eaten.
	 */
	public static void addGhostEaten() { mGhostsEaten++; }
}
