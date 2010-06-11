package database;

import java.util.*;

/**
 * Class representing a list of games.
 */
public class GameList extends ArrayList<GameData> {

	/**
	 * Get a GameData object by its game ID.
	 * @param id The ID of the game to retrieve.
	 * @return The game data, or null if the game does not exist.
	 */
	public GameData getGameDataByID(int id) {
		for (GameData data : this) {
			if (data.getId() == id) {
				return data;
			}
		}

		return null;
	}

	/**
	 * Get a string representation of the object formatted for messages, as:
	 * data;data;data;etc.
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (GameData data : this) {

			// Use semi-colon as delimiter between games
			if (builder.length() > 0) builder.append(";");

			builder.append(data.toString());
		}

		return builder.toString();
	}
}
