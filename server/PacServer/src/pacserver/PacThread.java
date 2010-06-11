package pacserver;

/**
 * Basic class for a thread that can handle a list of clients and a postbox.
 */
public class PacThread extends Thread {

	// Members
	private ClientList mClientList;
	private ClientList mDeadClientList;
	private PostBox mPostBox;
	
	/**
	 * Constructor.
	 */
	public PacThread() {
		super();
		mPostBox = new PostBox();
		mClientList = new ClientList();
		mDeadClientList = new ClientList();
	}
	
	/**
	 * Constructor.
	 * @param name Name of the thread.
	 */
	public PacThread(String name) {
		super(name);
		mPostBox = new PostBox();
		mClientList = new ClientList();
		mDeadClientList = new ClientList();
	}
	
	/**
	 * Get the client list.
	 * @return The client list.
	 */
	protected ClientList getClientList() { return mClientList; }
	
	/**
	 * Get the list of dead clients.
	 * @return The list of dead clients.
	 */
	protected ClientList getDeadClientList() { return mDeadClientList; }
	
	/**
	 * Get the post box.
	 * @return The post box.
	 */
	protected PostBox getPostBox() { return mPostBox; }
	
	/**
	 * Add an existing client to the thread.
	 * @param client The client to add to the thread.
	 */
	public void addClient(Client client) {
		client.setPostBox(mPostBox);
		client.setResultList(null);
		mClientList.add(client);
	}

	/**
	 * Remove a client from the client list.
	 * @param client The client to remove.
	 */
	public void removeClient(Client client) {
		mClientList.remove(client);
	}
	
	/**
	 * Get the number of clients connected to this thread.
	 * @return Number of clients connected to this thread.
	 */
	public int getClientCount() { return mClientList.size(); }
	
	/**
	 * Receives messages from the client.
	 */
	@Override
	public void run() {
		while (true) {

			// Runs client code for all clients.
			for (int i = 0; i < mClientList.size(); ++i) {
				mClientList.get(i).run();
			}

			// Process any pending messages
			MessageHandler.processMessages(mPostBox, mClientList);
			
			removeDeadClients();
		}
	}
	
	/**
	 * Remove dead clients from the client list.
	 */
	protected void removeDeadClients() {
		
		// Remove all dead clients
		int i = 0;
		while (i < getClientCount()) {
			
			Client client = getClientList().get(i);
			
			if (client.isDead()) {
				
				// Client dead, so move to the dead client list
				getClientList().remove(client);
				--i;
			}
			
			++i;
		}
	}
}
