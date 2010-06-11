package comms;

/**
 * Thrown when waiting for a message and the wait timeout is exceeded.
 */
public class ServerWaitTimeoutException extends Exception {

	/**
	 * Constructor.
	 * @param msg Message associated with this exception.
	 */
	public ServerWaitTimeoutException(String msg) {
		super(msg);
	}
}
