package database;

/**
 * Class representing information describing a game stored in the database.
 */
public class GameData {
	
	// Members
	private int mId;
	private String mName;
	private int mPlayerCount;
	
	/**
	 * Constructor.
	 * @param id Game ID.
	 * @param name Game name.
	 * @param playerCount Number of players required for the game to start.
	 */
	public GameData(int id, String name, int playerCount) {
		mId = id;
		mName = name;
		mPlayerCount = playerCount;
	}
	
	/**
	 * Get the game ID.
	 * @return The game ID.
	 */
	public int getId() { return mId; }
	
	/**
	 * Get the game name.
	 * @return The game name.
	 */
	public String getName() { return mName; }
	
	/**
	 * Get the number of players required for the game to start.
	 * @return The number of players required for the game to start.
	 */
	public int getPlayers() { return mPlayerCount; }

	/**
	 * Get a string representation of the object, formatted for messaging.
	 * id,name,playercount
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(mId);
		builder.append(",");
		builder.append(mName);
		builder.append(",");
		builder.append(mPlayerCount);

		return builder.toString();
	}
}
