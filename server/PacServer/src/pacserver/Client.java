package pacserver;

import java.net.*;
import java.io.*;
import database.*;

/**
 * Client class handles all client interaction with an individual client.
 */
public class Client {
	
	// Message types - server messages decrease from -1
	
	/** Sent when initiating client-server contact */
	public static final int MESSAGE_HANDSHAKE = -1;
	
	/** Sent when a client disconnects */
	public static final int MESSAGE_LOGOUT = -2;
	
	/** Sent when a client attempts to log in */
	public static final int MESSAGE_LOGIN = -3;
	
	/** Sent when a client requests list of peers */
	public static final int MESSAGE_PEER_LIST = -4;
	
	/** Sent when a client requests its player data */
	public static final int MESSAGE_PLAYER_DATA = -5;
	
	/** Sent when a client wants to join a game */
	public static final int MESSAGE_JOIN_GAME = -6;
	
	/** Sent when a client wants the list of available game types */
	public static final int MESSAGE_GAME_LIST = -7;
	
	/** Sent when user registers */
	public static final int MESSAGE_REGISTER = -8;
	
	/** Sent when a client requests its player stats */
	public static final int MESSAGE_PLAYER_STATS = -9;
	
	/** Sent when a game ends */
	public static final int MESSAGE_GAME_END = -10;
	
	/** Send when client requests high scores for a game */
	public static final int MESSAGE_HIGH_SCORES = -11;
	
	/** Sent to check a client is still alive */
	public static final int MESSAGE_PING = -12;
	
	/** Sent to notify other clients of a peer failure */
	public static final int MESSAGE_CLIENT_FAILED = -13;
	
	/** Sent when a client requests game stats */
	public static final int MESSAGE_GAME_STATS = -14;
        
	// Addresses
	
	/** Address of the server */
	public static final int ADDRESS_SERVER = 0;
	
	/** Messages to this address get sent to all clients */
	public static final int ADDRESS_ALL_CLIENTS = -1;

	// Other constants
	
	/** Default username for non-logged in clients */
	public static final String DEFAULT_USERNAME = "Not Authenticated";
	
	/** Maximum time between messages before client is presumed dead. */
	public static final long DEAD_CLIENT_TIMEOUT = 8000000;
	
	/** Time between pings */
	public static final long PING_TIME = 600000;
	
	// Members
	private int mId;
	private String mUsername;
	private int mPlayerId;
	private Socket mSocket;
	private PrintWriter mToClient = null;
	private BufferedReader mFromClient = null;
	private PostBox mPostBox = null;
	private boolean mIsAuthenticated;
	private GameResultList mResultList = null;
	private long mLastMessageTime;
	private boolean mIsDead;
	private long mPingTime;

	/**
	 * Constructor.
	 * @param id ID pf the new client.
	 * @param socket Socket to communicate with client on.
	 * @param postBox The postbox that the client will listen to/post to.
	 */
	public Client(int id, Socket socket, PostBox postBox) throws IOException {
		mId = id;
		mUsername = DEFAULT_USERNAME;
		mSocket = socket;
		mToClient = new PrintWriter(mSocket.getOutputStream(), true);
		mFromClient = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mPostBox = postBox;
		mIsAuthenticated = false;
		mLastMessageTime = 0;
		mIsDead = false;
		mPingTime = 0;
	}
	
	/**
	 * Close the client connections.
	 */
	public void close() {
		mToClient.close();
		
		try {
			mFromClient.close();
		} catch (IOException e) {
			// Ignore
		}
		
		try {
			mSocket.close();
		} catch (IOException e) {
			// Ignore
		}
	}

	/**
	 * Get the client ID.
	 * @return The client ID.
	 */
	public int getId() { return mId; }

	/**
	 * Get the ID of the player connected to this client.
	 * @return The player ID.
	 */
	public int getPlayerId() { return mPlayerId; }

	/**
	 * The the client username.
	 * @return The client username.
	 */
	public String getUsername() { return mUsername; }

	/**
	 * Set the postbox that the client posts messages to.
	 * @param postBox The new postbox.
	 */
	public void setPostBox(PostBox postBox) { mPostBox = postBox; }

	/**
	 * Run this client.  Fetches all incoming messages from the connected client
	 * and inserts them into the postbox.  Runs until all messages have been
	 * retrieved.
	 */
	public void run() {

		// Read all incoming messages and store them in the postbox
		String msg = null;

		while ((msg = getMessage()) != null) {
			mPostBox.addMessage(msg);

			Debug.print("Message: received: " + msg);
		}
		
		checkAlive();
		ping();
	}
	
	/**
	 * Check if the client is still alive by pinging it.  If no response is
	 * received within the timeout period, keep pinging until the maximum number
	 * of attemps has been reached.  After that period, declare the client dead.
	 */
	private void checkAlive() {

		if (!mIsDead) {

			mLastMessageTime++;
		
			// Timed out?
			if (mLastMessageTime > DEAD_CLIENT_TIMEOUT) {

				// Client not responding; declare it dead
				mIsDead = true;
				close();

				Debug.print("Client is dead!");
			}
		}
	}

	/**
	 * Send a ping to the client if the timeout has occurred.
	 */
	private void ping() {
		if (!mIsDead) {

			// Increase ping timer
			mPingTime++;

			// Timed out?
			if (mPingTime >= PING_TIME) {

				// Send a ping
				sendMessage(ADDRESS_SERVER, MESSAGE_PING, "");
				mPingTime = 0;

				Debug.print("Ping! " + mId);
			}
		}
	}
	
	/**
	 * Send a message to the client.
	 * Message format: header:data
	 * Header format: from,to,messageID
	 * Data format: Method specific
	 * From/to: 0 for server, -1 for all clients, >0 for specific client
	 * @param from Message sender.
	 * @param messageType Type of the message.
	 * @param data The data to send.
	 * @return True if the message sent OK.
	 */
	private boolean sendMessage(int from, int messageType, String data) {
		
		StringBuilder msg = new StringBuilder();
		
		// Header
		msg.append(from);
		msg.append(",");
		msg.append(mId);
		msg.append(",");
		msg.append(messageType);
		msg.append(":");
		
		// Data
		msg.append(data);
		
		mToClient.println(msg);

		return true;
	}
	
	/**
	 * Send a pre-formatted message to the client.
	 * @param message The message to send.
	 * @return True if the message sent OK.
	 */
	public boolean sendMessage(String message) {		
		mToClient.println(message);

		return true;
	}
	
	/**
	 * Get a message from the client.
	 * @return The message.
	 */
	public synchronized String getMessage() {
		String msg = null;
		
		try {
			if (mFromClient.ready()) {
				msg = mFromClient.readLine();
				mLastMessageTime = 0;
			}
		} catch (IOException e) {
			System.out.println("Error reading from client");
		}

		return msg;
	}
	
	/**
	 * Send essential data to the client.
	 */
	public void handshake() {

		StringBuilder msg = new StringBuilder();
		msg.append(mId);

		Debug.print("Message: handshake: " + msg.toString());

		sendMessage(ADDRESS_SERVER, MESSAGE_HANDSHAKE, msg.toString());
	}

	/**
	 * Send player data to the client.
	 * Data transmitted as id,username,password,joineddate
	 * @param playerData The player data object to send.
	 */
	public boolean sendPlayerData(PlayerData playerData) {
	
		if (playerData != null) {
			sendMessage(ADDRESS_SERVER, MESSAGE_PLAYER_DATA, playerData.toString());
		} else {
			sendMessage(ADDRESS_SERVER, MESSAGE_PLAYER_DATA, "");
		}
		
		return true;
	}

	/**
	 * Send peer list to the client.
	 * @param clientList List of clients to send the session data to.  The list
	 * should include the current client as one of the members.
	 */
	public void sendPeerList(ClientList clientList) {

		Debug.print("Message: sendGameSessionData: " + clientList.toMessageData());

		sendMessage(ADDRESS_SERVER, MESSAGE_PEER_LIST, clientList.toMessageData());
	}
	
	/**
	 * Log the client out.
	 */
	public void logout() {

		// Client has quit - close connection
		close();
		mIsDead = true;
	}

	/**
	 * Get the client's authenticated state.
	 * @return The client's authenticated state.
	 */
	public boolean isAuthenticated() { return mIsAuthenticated; }

	/**
	 * Check if the client's game has ended.
	 * @return True if the game has ended.
	 */
	public boolean isGameOver() { return (mResultList != null); }

	/**
	 * Get the results of this game.  Returns null if the game is still in
	 * progress.
	 * @return The results list for this player.
	 */
	public GameResultList getResultList() { return mResultList; }

	/**
	 * Set the results of this game.
	 * @param resultList The list of results for this game.
	 */
	public void setResultList(GameResultList resultList) { mResultList = resultList; }

	/**
	 * Attempt to authenticate the client.  Sends a PlayerData object back to
	 * the client if authentication succeeds, or null if it fails.
	 * @param username The username to authenticate.
	 * @param password The password to authenticate.
	 */
	public void authenticate(String username, String password) {

		PlayerData playerData = Database.getPlayer(username);

		if (playerData == null) {

			// No player with that name exists
			sendLoginData(null);
		} else {

			// Player exists; does password match?
			if (password.equals(playerData.getPassword())) {

				// Player valid
				mIsAuthenticated = true;
				mUsername = playerData.getUsername();
				mPlayerId = playerData.getId();
				sendLoginData(playerData);
			} else {

				// Password invalid
				sendLoginData(null);
			}
		}
	}

	/**
	 * Send player data to the client for login.
	 * Data transmitted as id,username,password,joineddate
	 * @param playerData The player data object to send.
	 */
	public boolean sendLoginData(PlayerData playerData) {

		if (playerData != null) {
			sendMessage(ADDRESS_SERVER, MESSAGE_LOGIN, playerData.toString());
		} else {
			sendMessage(ADDRESS_SERVER, MESSAGE_LOGIN, "");
		}

		return true;
	}


	/**
	 * Send a game session ID to the client.
	 * @param sessionId The session ID to send.
	 */
	public void sendGameSessionId(int sessionId) {
		sendMessage(ADDRESS_SERVER, MESSAGE_JOIN_GAME, String.valueOf(sessionId));
	}

	/**
	 * Send the list of available game types to the client.
	 */
	public void sendGameList() {
		GameList gameList = Database.getGameList();

		sendMessage(ADDRESS_SERVER, MESSAGE_GAME_LIST, gameList.toString());
	}

	/**
	 * Register a new user the client.  Sends a PlayerData object back to
	 * the client if registration succeeds, or null if it fails.
	 * @param username The username to authenticate.
	 * @param password The password to authenticate.
	 */
	public void register(String username, String password) {

		PlayerData playerData = Database.register(username, password);

		if (playerData != null) {
			
			// Player valid
			mIsAuthenticated = true;
			mUsername = playerData.getUsername();
			mPlayerId = playerData.getId();
			
			sendMessage(ADDRESS_SERVER, MESSAGE_REGISTER, playerData.toString());
		} else {
			sendMessage(ADDRESS_SERVER, MESSAGE_REGISTER, "");
		}
	}

	/**
	 * Send player stats to the client.
	 * Data transmitted as username,favouritegame,lastgameplayed,
	 * lastgameplayeddate,numberofgamesplayed,numberofdisconnects,
	 * totaltimeplayed,ranking
	 */
	public void sendPlayerStats() {

		PlayerStats playerStats = Database.getPlayerStats(mUsername);

		if (playerStats != null) {
			sendMessage(ADDRESS_SERVER, MESSAGE_PLAYER_STATS, playerStats.toString());
		} else {
			sendMessage(ADDRESS_SERVER, MESSAGE_PLAYER_STATS, "");
		}
	}
        
	/**
	 * Send the list of available game types to the client.
	 * @param gameId ID of the game to send high scores for.
	 */
	public void sendHighScores(int gameId) {
		HighScoreData highScores = Database.getHighScores(gameId);

		sendMessage(ADDRESS_SERVER, MESSAGE_HIGH_SCORES, highScores.toString());
	}
	
	/**
	 * Send the game stats to the client.
	 * @param gameId ID of the game.
	 */
	public void sendGameStats(int gameId) {
		GameStats stats = Database.getGameStats(gameId);

		sendMessage(ADDRESS_SERVER, MESSAGE_GAME_STATS, stats.toString());
	}
	
	/**
	 * Check if the client is no longer responding to pings and should be
	 * treated as dead.
	 * @return True if the client is dead.
	 */
	public boolean isDead() { return mIsDead; }
	
	/**
	 * Notify this client that one of its peers has failed.
	 * @param clientId The ID of the client that failed.
	 */
	public void sendClientFailure(int clientId) {
		sendMessage(ADDRESS_SERVER, MESSAGE_CLIENT_FAILED, String.valueOf(clientId));
	}
}
