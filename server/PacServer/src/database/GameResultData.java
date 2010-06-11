package database;

/**
 * Contains the final result of a game session.
 */
public class GameResultData {

	// Members
	private int mPlayerId;
	private int mScore;
	private boolean mDisconnected;

	/**
	 * Constructor.
	 * @param playerId The ID of the player.
	 * @param score The player's score.
	 * @param disconnected True if the player disconnected during the game.
	 */
	public GameResultData(int playerId, int score, boolean disconnected) {
		mPlayerId = playerId;
		mScore = score;
		mDisconnected = disconnected;
	}

	/**
	 * Get the ID of the player.
	 * @return The ID of the player.
	 */
	public int getPlayerId() { return mPlayerId; }

	/**
	 * Get the score.
	 * @return The score.
	 */
	public int getScore() { return mScore; }

	/**
	 * Get the disconnected value.
	 * @return True if the player disconnected.
	 */
	public boolean getDisconnected() { return mDisconnected; }
}
