package comms;

/**
 * Class representing a peer of this client connected to the server.
 */
public class PeerData {

	// Members
	private int mId;
	private String mUsername;

	/**
	 * Constructor.
	 * @param id ID of the client.
	 * @param username Username of the player connected using the client.
	 */
	public PeerData(int id, String username) {
		mId = id;
		mUsername = username;
	}

	/**
	 * Get the client ID.
	 * @return The client ID.
	 */
	public int getClientId() { return mId; }

	/**
	 * Get the username of the player using the client.
	 * @return The username of the player using the client.
	 */
	public String getUsername() { return mUsername; }

	/**
	 * Get the string representation of the client data object.
	 * @return The string representation of the client data object.
	 */
	@Override
	public String toString() {
		return mId + "," + mUsername;
	}
}
