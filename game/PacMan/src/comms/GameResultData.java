package comms;

/**
 * Contains the final result of a game session.
 */
public class GameResultData {

	// Members
	private int mClientId;
	private int mScore;

	/**
	 * Constructor.
	 * @param clientId The ID of the client.
	 * @param score The client's score.
	 */
	public GameResultData(int clientId, int score) {
		mClientId = clientId;
		mScore = score;
	}

	/**
	 * Get the client ID.
	 * @return The client ID.
	 */
	public int getClientId() { return mClientId; }

	/**
	 * Get the score.
	 * @return The score.
	 */
	public int getScore() { return mScore; }

	/**
	 * Get a string representation of the object, formatted:
	 * clientid,score
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(mClientId);
		builder.append(",");
		builder.append(mScore);

		return builder.toString();
	}
}
