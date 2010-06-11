package database;

import java.util.*;

/**
 * Contains GameResultData objects.  Used in verifying the end result of a game.
 */
public class GameResultList extends ArrayList<GameResultData> {

	/**
	 * Get the score for the specified player.
	 * @param playerId The ID of the player whose score needs to be retrieved.
	 * @return The score of the specified player, or 0 if the player does not
	 * exist in the list.
	 */
	public int getScoreForPlayer(int playerId) {
		for (GameResultData data : this) {
			if (data.getPlayerId() == playerId) return data.getScore();
		}

		return -1;
	}

	/**
	 * Get the id of the player who won the game.
	 * @return The id of the player who won the game.
	 */
	public int getWinnerId() {
		int winnerId = -1;
		int winnerScore = -1;

		for (GameResultData data : this) {
			if (data.getScore() > winnerScore) {
				winnerScore = data.getScore();
				winnerId = data.getPlayerId();
			}
		}

		return winnerId;
	}
}
