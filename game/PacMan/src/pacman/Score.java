package pacman;

/**
 * Class to keep track of a player's score.
 */
public class Score {

	// Constants
	
	/** Score given each time PacMan collects a pill */
	final public static int SCORE_PILL = 10;
	
	/** Score given each time PacMan collects a power pill */
	final public static int SCORE_POWER_PILL = 50;
	
	/** Score given each time PacMan eats a ghost */
	final public static int SCORE_EAT_GHOST = 100;
	
	/** Score given each time a ghost eats PacMan */
	final public static int SCORE_EAT_PACMAN = 1500;

	// Members
	private int mScore;

	/**
	 * Constructor.
	 */
	public Score() {
		mScore = 0;
	}

	/**
	 * Get the score.
	 * @return The score.
	 */
	public int getScore() { return mScore; }

	/**
	 * Increase the score by the specified amount.
	 * @param score Amount to increase the score by.
	 */
	public void add(int score) {
		mScore += score;
	}

	/**
	 * Reset the score to 0.
	 */
	public void reset() { mScore = 0; }
}
