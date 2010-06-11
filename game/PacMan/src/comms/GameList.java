package comms;

import java.util.*;

/**
 * Class containing a list of GameData objects.
 */
public class GameList extends ArrayList<GameData> {

	/**
	 * Get a GameData object by its ID.
	 * @param id The ID of the object to retrieve.
	 * @return The GameData object.
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
	 * Get a GameData object by its name.
	 * @param name The name of the object to retrieve.
	 * @return The GameData object.
	 */
	public GameData getGameDataByName(String name) {
		for (GameData data : this) {
			if (data.getName().equals(name)) {
				return data;
			}
		}

		return null;
	}

	/**
	 * Get a string representation of the list, formatted for message sending:
	 * gamedata;gamedata;gamedata;etc.
	 * @return A string representation of the list.
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
