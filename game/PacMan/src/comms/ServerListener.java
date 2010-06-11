package comms;

import java.util.EventListener;

/**
 * Interface defining a ServerListener.
 */
public interface ServerListener extends EventListener {

	/**
	 * Client has connected to the server.
	 */
	public void serverConnected();

	/**
	 * Server has died.
	 */
	public void serverDied();

	/**
	 * Client has received a message from the server.
	 */
	public void serverReceivedMessage();

	/**
	 * Client has sent a message to the server.
	 */
	public void serverSentMessage();
}
