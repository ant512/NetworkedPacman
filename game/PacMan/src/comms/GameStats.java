package comms;

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
	 * Get the total time played as a pre-formatted string.
	 * @return The total time played as a pre-formatted string.
	 */
	public String getTotalDurationString() {
		
		long duration = mTotalDuration;
		int hours = (int)(duration / 3600000);
		duration -= hours * 3600000;

		int minutes = (int)(duration / 60000);
		duration -= minutes * 60000;

		int seconds = (int)(duration / 1000);
		
		return hours + "h " + minutes + "m " + seconds + "s";
	}
	
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
