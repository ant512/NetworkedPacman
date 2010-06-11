package pacserver;

import java.io.IOException;
import java.net.*;
import database.*;

/**
 * Class for handling connections to clients in a separate thread.  This thread
 * contains all clients not currently engaged in a game.
 */
public class LobbyThread extends PacThread {

	// Members
	private int mNextClientId;
	private int mNextGameSessionId;
	private GameThreadList mRunningGameList;

	private static LobbyThread mLobbyThread;

	/**
	 * Initialise the lobby thread.
	 */
	public static void init() {
		mLobbyThread = new LobbyThread();
		mLobbyThread.start();
	}

	/**
	 * Get a pointer to the lobby thread singleton.
	 * @return A pointer to the lobby thread singleton.
	 */
	public static LobbyThread getLobbyThread() {
		return mLobbyThread;
	}

	/**
	 * Constructor.  Singleton pattern.
	 */
	private LobbyThread() {
		super("ServerThread");
		mRunningGameList = new GameThreadList();
		mNextClientId = 1;
		mNextGameSessionId = 1;

		Debug.print("Lobby: Thread created");
	}

	/**
	 * Add a new client to the thread.
	 * @param socket The socket to communicate to the client with.
	 */
	public void addClient(Socket socket) {

		try {
			socket.setTcpNoDelay(true);
			Client client = new Client(mNextClientId, socket, getPostBox());
			getClientList().add(client);

			client.handshake();

			// Remember that we have a new client
			mNextClientId++;
			
		} catch (IOException e) {
			System.err.println("Error adding client.");
		}
	}

	/**
	 * Creates a new game thread and moves the client into it.
	 * @param gameData Data describing the game to be created.
	 * @param client Client that is creating the game.
	 */
	public GameThread createGame(GameData gameData, Client client) {

		Debug.print("Lobby: Creating new game thread");

		// Create a new game thread
		GameThread thread = new GameThread(gameData, mNextGameSessionId);
		mRunningGameList.add(thread);

		mNextGameSessionId++;

		// Move client to new thread
		removeClient(client);
		thread.addClient(client);

		// Start the game running
		thread.start();

		return thread;
	}

	/**
	 * Add a client to a game session.
	 * @param gameData The data of the game that the client wants to join.
	 * @param client The client that wants to join.
	 */
	public GameThread joinGame(GameData gameData, Client client) {

		// Attempt to find an existing session of this game that is waiting for
		// players
		for (int i = 0; i < mRunningGameList.size(); ++i) {

			GameThread thread = mRunningGameList.get(i);

			if (thread.getGameId() == gameData.getId()) {
				if (thread.isWaitingForPlayers()) {

					// Move client to new thread
					removeClient(client);
					thread.addClient(client);

					return thread;
				}
			}
		}

		// No existing thread, so create new
		return createGame(gameData, client);
	}

	/**
	 * Remove a game thread from the list of running game threads.
	 * @param thread The thread to remove.
	 */
	public synchronized void removeThread(GameThread thread) {

		Debug.print("Removing game thread from list");

		mRunningGameList.remove(thread);
	}
}

