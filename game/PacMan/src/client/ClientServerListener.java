package client;

import comms.*;

/**
 * Listens for server events and shuts down the client if the server dies.
 */
public class ClientServerListener extends ServerAdaptor {

	/**
	 * Handle server died events.
	 */
	@Override
	public void serverDied() {
		System.exit(1);
	}
}
