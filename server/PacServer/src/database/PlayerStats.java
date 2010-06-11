package database;

/**
 * Represents a player's stats.
 */
public class PlayerStats {

	// Members
	private String mUsername;
	private String mFavouriteGame;
	private String mLastGamePlayed;
	private String mLastGamePlayedDate;
	private int mNumberOfGamesPlayed;
	private int mNumberOfDisconnects;
	private int mNumberOfGamesWon;
	private String mTotalTimePlayed;
	private String mRanking;

	/**
	 * Constructor.
	 * @param username The player's username.
	 * @param favouriteGame The player's favourite game.
	 * @param lastGamePlayed The last game that the player played.
	 * @param lastGamePlayedDate The date that the player last played a game.
	 * @param numberOfGamesPlayed The number of games the player has played.
	 * @param numberOfDisconnects The number of times the player has disconnected.
	 * @param numberOfGamesWon The number of times the player has won a game.
	 * @param totalTimePlayed The total time the player has used the system.
	 * @param ranking The player's ranking.
	 */
	public PlayerStats(String username, String favouriteGame, String lastGamePlayed,
			String lastGamePlayedDate, int numberOfGamesPlayed, int numberOfDisconnects,
			int numberOfGamesWon, String totalTimePlayed, String ranking) {

		mUsername = username;
		mFavouriteGame = favouriteGame;
		mLastGamePlayed = lastGamePlayed;
		mLastGamePlayedDate = lastGamePlayedDate;
		mNumberOfGamesPlayed = numberOfGamesPlayed;
		mNumberOfDisconnects = numberOfDisconnects;
		mNumberOfGamesWon = numberOfGamesWon;
		mTotalTimePlayed = totalTimePlayed;
		mRanking = ranking;
	}

	/**
	 * Get the username.
	 * @return The username.
	 */
	public String getUsername() {
		return mUsername;
	}

	/**
	 * Get the name of the favourite game.
	 * @return The favourite game.
	 */
	public String getFavouriteGame() {
		return mFavouriteGame;
	}

	/**
	 * Get the name of the last game played.
	 * @return The name of the last game played.
	 */
	public String getLastGamePlayed() {
		return mLastGamePlayed;
	}

	/**
	 * Get the date of the last game played.
	 * @return The date of the last game played.
	 */
	public String getLastGamePlayedDate() {
		return mLastGamePlayedDate;
	}

	/**
	 * Get the number of games played.
	 * @return The number of games played.
	 */
	public int getNumberOfGamesPlayed() {
		return mNumberOfGamesPlayed;
	}

	/**
	 * Get the number of times the player has disconnected.
	 * @return The number of times the player has disconnected.
	 */
	public int getNumberOfDisconnects() {
		return mNumberOfDisconnects;
	}
	
	/**
	 * Get the number of times the player has won.
	 * @return The number of times the player has won.
	 */
	public int getNumberOfGamesWon() {
		return mNumberOfGamesWon;
	}

	/**
	 * Get the total time played.
	 * @return The total time played.
	 */
	public String getTotalTimePlayed() {
		return mTotalTimePlayed;
	}

	/**
	 * Get the player's ranking.
	 * @return The player's ranking.
	 */
	public String getRanking() {
		return mRanking;
	}

	/**
	 * Return a string representation of the object.
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		String statsString = "";
		statsString += mUsername + ",";
		statsString += mFavouriteGame + ",";
		statsString += mLastGamePlayed + ",";
		statsString += mLastGamePlayedDate + ",";
		statsString += mNumberOfGamesPlayed + ",";
		statsString += mNumberOfDisconnects + ",";
		statsString += mNumberOfGamesWon + ",";
		statsString += mTotalTimePlayed + ",";
		statsString += mRanking;

		return statsString;
	}
}
