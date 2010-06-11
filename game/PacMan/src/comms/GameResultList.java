package comms;

import java.util.*;

/**
 * List of GameResultData objects.
 */
public class GameResultList extends ArrayList<GameResultData> {

	/**
	 * Get a string representation of the list, formatted for messaging:
	 * gameresultdata;gameresultdata;gameresultdata;etc.
	 * @return The string representation of the list.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (GameResultData data : this) {

			if (builder.length() > 0) builder.append(";");

			builder.append(data.toString());
		}

		return builder.toString();
	}
}
