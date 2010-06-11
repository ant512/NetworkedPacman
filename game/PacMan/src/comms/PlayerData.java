package comms;

/**
 * Represents all of the data relevant to the current player.
 */
public class PlayerData {

	// Members
	private int mId;
	private String mUsername;
	private String mPassword;
	private String mJoinedDate;

	/**
	 * Constructor.
	 * @param id The player's ID.
	 * @param username The player's username.
	 * @param password The player's password.
	 * @param joinedDate The date that the player registered.
	 */
	public PlayerData(int id, String username, String password, String joinedDate) {
		mId = id;
		mUsername = username;
		mPassword = password;
		mJoinedDate = joinedDate;
	}

	/**
	 * Get the player's username.
	 * @return The player's username.
	 */
	public String getUsername() { return mUsername; }

	/**
	 * Get the player's password.
	 * @return The player's password.
	 */
	public String getPassword() { return mPassword; }

	/**
	 * Get the player's ID.
	 * @return The player's ID.
	 */
	public int getId() { return mId; }

	/**
	 * Get the date that the player joined.
	 * @return The date that the player joined.
	 */
	public String getJoinedDate() { return mJoinedDate; }

	/**
	 * Get a string representation of the object, formatted:
	 * id,username,password,joineddate
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(mId);
		builder.append(",");
		builder.append(mUsername);
		builder.append(",");
		builder.append(mPassword);
		builder.append(",");
		builder.append(mJoinedDate);

		return builder.toString();
	}
}
