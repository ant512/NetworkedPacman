package pacserver;

import java.util.*;

/**
 * List of clients.  Wrapper around the ArrayList class offering synchronized
 * access.
 */
public class ClientList {

	// Members
	private ArrayList<Client> mClientList;

	/**
	 * Constructor.
	 */
	public ClientList() {
		mClientList = new ArrayList<Client>();
	}

	/**
	 * Get the client at the specified index.
	 * @param i The index of the client to retrieve.
	 * @return The client at the specified index.
	 */
	public synchronized Client get(int i) {
		if (i < mClientList.size()) return mClientList.get(i);
		return null;
	}

	/**
	 * Add the supplied client to the list.
	 * @param client Client to add to the list.
	 */
	public synchronized void add(Client client) {
		mClientList.add(client);
	}

	/**
	 * Remove the specified client from the list.
	 * @param client The client to remove.
	 */
	public synchronized void remove(Client client) {
		mClientList.remove(client);
	}

	/**
	 * Retrieve a client by its ID.
	 * @param id The id of the client.
	 * @return The client.
	 */
	public synchronized Client getClientByID(int id) {
		for (Client client : mClientList) {
			if (client.getId() == id) return client;
		}

		return null;
	}

	/**
	 * Get the number of clients in the list.
	 * @return The number of clients in the list.
	 */
	public synchronized int size() {
		return mClientList.size();
	}
	
	/**
	 * Get the client list formatted as a message data string.
	 * @return The client list formatted as a message data string.
	 */
	public synchronized String toMessageData() {
		
		StringBuilder msg = new StringBuilder();

		// Add all clients and their IDs to the session data
		for (int i = 0; i < mClientList.size(); ++i) {

			if (msg.length() > 0) {
				msg.append(",");
			}

			msg.append(mClientList.get(i).getId());
			msg.append(",");
			msg.append(mClientList.get(i).getUsername());
		}
		
		return msg.toString();
	}
}
