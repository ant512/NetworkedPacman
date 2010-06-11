package comms;

/**
 * Interface definition for a message handler.
 */
public interface MessageHandlerInterface {

	/**
	 * Handle the specified message.
	 * @param msg The message to handle.
	 */
	public void handleMessage(Message msg);

	/**
	 * Handle a client failure.
	 * @param clientId The ID of the failed client.
	 */
	public void handleClientFailure(int clientId);
}
