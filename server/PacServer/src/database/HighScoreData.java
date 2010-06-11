package database;

import java.util.*;

/** database.HighScoreData
 * Stores the HighScores for a specific game.
 * Created: 11-Mar-2009
 * @author wob
 */
public class HighScoreData extends ArrayList<HighScore> {

	// Members
	private int gameId;

	/**
	 * Constructor for a HighScoreData object.
	 * @param gameId An integer representing the ID of the game.
	 */
	public HighScoreData(int gameId) {
		this.gameId = gameId;
	}

	/**
	 * Returns the gameId of the game these highscores belong to.
	 * @return An int representing the ID of a game.
	 */
	public int getGameId() {
		return gameId;
	}

	/**
	 * Get a string representation of the object, formatted as:
	 * data,data,data,etc.
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		String scoreString = new String("");
		for (int i = 0; i < size(); i++) {
			scoreString += get(i).toString() + ",";
		}
		return scoreString;
	}
}
