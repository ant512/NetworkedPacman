package database;

/**
 * Contains information about a particular game.
 */
public class GameStats {
	
	// Members
	private int mGameId;
	private long mTotalDuration;
	private int mTimesPlayed;
	
	/**
	 * Constructor.
	 * @param gameId The game ID.
	 * @param totalDuration The total time the game has been played.
	 * @param timesPlayed The number of times the game has been played.
	 */
	public GameStats(int gameId, long totalDuration, int timesPlayed) {
		mGameId = gameId;
		mTotalDuration = totalDuration;
		mTimesPlayed = timesPlayed;
	}
	
	/**
	 * Get the game ID.
	 * @return The game ID.
	 */
	public int getGameId() { return mGameId; }
	
	/**
	 * Get the total amount of time that the game has been played.
	 * @return The total amount of time that the game has been played.
	 */
	public long getTotalDuration() { return mTotalDuration; }
	
	/**
	 * Get the number of times that the game has been played.
	 * @return The number of times that the game has been played.
	 */
	public int getTimesPlayed() { return mTimesPlayed; }
	
	/**
	 * Return a message-ready version of the object, in the format
	 * gameid,totalduration,timesplayed
	 * @return A message-ready representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(mGameId);
		builder.append(",");
		builder.append(mTotalDuration);
		builder.append(",");
		builder.append(mTimesPlayed);
		
		return builder.toString();
	}
}
