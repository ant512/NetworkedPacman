package comms;

/**
 * Represents all of the data necessary to establish a connection with the
 * server.
 */
public class HandshakeData {

	// Members
	private int mClientId;

	/**
	 * Constructor.
	 * @param clientId ID of this client.
	 */
	public HandshakeData(int clientId) {
		mClientId = clientId;
	}

	/**
	 * Get the client ID.
	 * @return The client ID.
	 */
	public int getClientId() { return mClientId; }
}
