package database;

import java.util.*;

/**
 * Class representing a single game history.
 */
public class GameHistoryData {

	// Members
	private int mId;
	private int mGameId;
	private int mWinnerId;
	private java.sql.Date mStartDate;
	private long mDuration;

	/**
	 * Constructor.
	 * @param id The ID of the game history object.
	 * @param gameId The ID of the game.
	 * @param winnerId The ID of the winning player.
	 * @param startDate The start date of the game session.
	 * @param duration The duration of the game.
	 */
	public GameHistoryData(int id, int gameId, int winnerId, java.sql.Date startDate, long duration) {
		mId = id;
		mGameId = gameId;
		mWinnerId = winnerId;
		mStartDate = startDate;
		mDuration = duration;
	}

	/**
	 * Get the game history ID.
	 * @return The game history ID.
	 */
	public int getId() { return mId; }

	/**
	 * Get the game ID.
	 * @return The game ID.
	 */
	public int getGameId() { return mGameId; }

	/**
	 * Get the winner ID.
	 * @return The winner ID.
	 */
	public int getWinnerId() { return mWinnerId; }

	/**
	 * Get the start date.
	 * @return The start date.
	 */
	public Date getStartDate() { return mStartDate; }

	/**
	 * Get the duration of the game.
	 * @return The duration of the game.
	 */
	public long getDuration() { return mDuration; }

	/**
	 * Get a string representation of the object.
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return String.valueOf(mId);
	}
}
