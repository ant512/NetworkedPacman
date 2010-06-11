package comms;

import java.io.*;
import java.net.*;

/**
 * Handles all socket code and communicates with server.  Can send all generic
 * messages.
 */
public class Server {

	/** Message intended for the server */
	public static final int ADDRESS_SERVER = 0;
	
	/** Message intended for all clients */
	public static final int ADDRESS_ALL_CLIENTS = -1;

	/** Maximum time between messages before server is presumed dead. */
	public static final long DEAD_SERVER_TIMEOUT = 150000;	
	
	/** Time between pings */
	public static final long PING_TIME = 4000;				
	
	// Members
	private static Socket mSocket = null;
	private static PrintWriter mToServer = null;
	private static BufferedReader mFromServer = null;

	private static MessageQueue mMessageQueue = new MessageQueue();

	private static int mClientId;
	private static int mSessionId;
	private static long mLastMessageTime;
	private static boolean mIsDead;
	private static long mPingTime;
	private static ServerListenerList mListenersList = new ServerListenerList();

	/**
	 * Enum of all events that the server can raise.
	 */
	private enum EventType {
		CONNECTED,
		DIED,
		RECEIVED_MESSAGE,
		SENT_MESSAGE;
	}

	/**
	 * Constructor.  Private to prevent construction.
	 */
	private Server()  { }

	/**
	 * Open a new connection to the specified host.
	 * @param host Host to connect to.
	 * @param port Port to connect on.
	 * @throws java.net.SocketException Thrown if the socket cannot be created.
	 */
	public static boolean connect(String host, int port) {
		try {
			mSocket = new Socket(host, port);
			mSocket.setTcpNoDelay(true);

			mToServer = new PrintWriter(mSocket.getOutputStream(), true);
			mFromServer = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			mLastMessageTime = System.currentTimeMillis();
			mIsDead = false;
			mPingTime = System.currentTimeMillis();

			handshake();

			raiseEvent(EventType.CONNECTED);
			
		} catch (IOException e) {
			
			// Cannot open socket
			util.Debug.print("Cannot open connection: " + e);
			return false;
		}

		return true;
	}

	/**
	 * Add a listener to the server.
	 * @param listener The listener to add.
	 * @return True if the listener was added.
	 */
	public static boolean addServerListener(ServerListener listener) {
		return mListenersList.add(listener);
	}

	/**
	 * Remove a listener from the server.
	 * @param listener The listener to remove.
	 * @return True if the listener was removed.
	 */
	public static boolean removeServerListener(ServerListener listener) {
		return mListenersList.remove(listener);
	}

	/**
	 * Raise an event of the specified type to all registered listeners.
	 * @param eventType The type of event to raise.
	 */
	private static void raiseEvent(EventType eventType) {
		for (int i = 0; i < mListenersList.size(); ++i) {
			switch (eventType) {
				case CONNECTED:
					mListenersList.get(i).serverConnected();
					break;
				case DIED:
					mListenersList.get(i).serverDied();
					break;
				case RECEIVED_MESSAGE:
					mListenersList.get(i).serverReceivedMessage();
					break;
				case SENT_MESSAGE:
					mListenersList.get(i).serverSentMessage();
					break;
			}
		}
	}

	/**
	 * Check if the server is still alive.  If no message is delivered within an
	 * acceptable time, the  server is presumed dead.
	 */
	private static void checkAlive() {

		if (!mIsDead) {

			// Timed out?
			if (System.currentTimeMillis() - mLastMessageTime > DEAD_SERVER_TIMEOUT) {

				// Server not responding; declare it dead
				mIsDead = true;

				util.Debug.print("Server is dead!");

				raiseEvent(EventType.DIED);
			}
		}
	}

	/**
	 * Send a ping to the server if the timeout has occurred.
	 */
	private static void ping() {
		if (!mIsDead) {

			// Timed out?
			if (System.currentTimeMillis() - mPingTime > PING_TIME) {

				// Send a ping
				sendMessage(new ServerMessage(mClientId, ServerMessage.ServerMessageType.PING, ""));
				mPingTime = System.currentTimeMillis();

				util.Debug.print("Ping!");
			}
		}
	}

	/**
	 * Send all incoming messages to the MessageHandler for processing.
	 */
	public static void processMessages(MessageHandlerInterface messageHandler) {

		Message msg;

		// Read all incoming messages into the queue
		queueMessages();

		// Process queued messages
		if (messageHandler != null) {
			while ((msg = mMessageQueue.poll()) != null) {
				messageHandler.handleMessage(msg);
			}
		}
	}

	/**
	 * Read all incoming messages from the socket and queue them.
	 */
	private static void queueMessages() {
		Message msg;

		while ((msg = getMessageFromSocket()) != null) {

			if (msg.getType() == ServerMessage.ServerMessageType.PING.type()) {
				util.Debug.print("Pong!");
			} else {

				// Queue message for later processing
				mMessageQueue.add(msg);
			}

			util.Debug.print("Message: received and queued: " + msg);
		}

		checkAlive();
		ping();
	}

	/**
	 * Join a game session.
	 * @param gameId The ID of the game to join.
	 * @return The ID of the session.
	 */
	public static int joinGameSession(int gameId) {

		// Request a new game
		sendMessage(new ServerMessage(mClientId,
				ServerMessage.ServerMessageType.JOIN_GAME,
				String.valueOf(gameId)));

		// Get the ID of the game session
		try {
			Message msg = waitForMessage(ServerMessage.ServerMessageType.JOIN_GAME.type(), 0);

			mSessionId = Integer.parseInt(msg.getData());
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown, as we wait forever
		}

		return mSessionId;
	}

	/**
	 * Send message indicating client has quit.
	 */
	public static void logout() {
		sendMessage(new ServerMessage(mClientId,
				ServerMessage.ServerMessageType.LOGOUT,
				""));
	}

	/**
	 * Login to server.  Should be run when a server connection is first
	 * established.
	 * @param username The player's username.
	 * @param password The player's password.
	 * @return The client data from the server.
	 */
	public static PlayerData login(String username, String password) {

		StringBuilder sendMsg = new StringBuilder();
		
		sendMsg.append(username);
		sendMsg.append(",");
		sendMsg.append(password);

		sendMessage(new ServerMessage(mClientId,
				ServerMessage.ServerMessageType.LOGIN,
				sendMsg.toString()));

		try {
			Message msg = waitForMessage(ServerMessage.ServerMessageType.LOGIN.type(), 0);

			// Parse the data from the message and extract player data
			if (msg.getData().length() > 0) {

				// Login OK, return data
				String data[] = msg.getData().split(",");
				int playerId = Integer.parseInt(data[0]);
				String joinedDate = data[3];

				return new PlayerData(playerId, username, password, joinedDate);
			}
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}
		
		return null;
	}

	/**
	 * Get handshake data from server.  Should be run when a server connection
	 * is first established in order to create the network of clients and
	 * server.
	 * @return The handshake data from the server.
	 */
	private static void handshake() {
		try {
			Message msg = waitForMessage(ServerMessage.ServerMessageType.HANDSHAKE.type(), 0);

			mClientId = Integer.valueOf(msg.getData());
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}
	}

	/**
	 * Wait for the peer list from the server.
	 * @param waitTime The amount of time to wait before giving up.  0 to wait
	 * forever.
	 * @return The peer list from the server.
	 * @throws ServerWaitTimeoutException Thrown if timeout exceeded.
	 */
	public static PeerList waitForPeerList(long waitTime) throws ServerWaitTimeoutException {
		Message msg = Server.waitForMessage(ServerMessage.ServerMessageType.PEER_LIST.type(), waitTime);

		PeerList sessionData = new PeerList();

		if (msg.getData().length() > 0) {

			String data[] = msg.getData().split(",");

			for (int i = 0; i < data.length; i += 2) {
				sessionData.add(new PeerData(Integer.parseInt(data[i]), data[i + 1]));
			}
		}

		return sessionData;
	}

	/**
	 * Get the data for the player with the specified username.
	 * @param username The username of the player.
	 * @return The player's data.
	 */
	public static PlayerData getPlayerData(String username) {

		sendMessage(new ServerMessage(mClientId, ServerMessage.ServerMessageType.PLAYER_DATA, username));

		try {
			Message msg = Server.waitForMessage(ServerMessage.ServerMessageType.PLAYER_DATA.type(), 0);

			// Parse the data from the message and extract player data
			if (msg.getData().length() > 0) {
				String data[] = msg.getData().split(",");

				int playerId = Integer.parseInt(data[0]);
				String password = data[2];
				String joinedDate = data[3];

				return new PlayerData(playerId, username, password, joinedDate);
			} else {
				return null;
			}
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}
		
		return null;
	}
	
	/**
	 * Get the stats for the specified game.
	 * @param gameId The ID of the game.
	 * @return The game's stats.
	 */
	public static GameStats getGameStats(int gameId) {

		sendMessage(new ServerMessage(mClientId, ServerMessage.ServerMessageType.GAME_STATS, String.valueOf(gameId)));

		try {
			Message msg = Server.waitForMessage(ServerMessage.ServerMessageType.GAME_STATS.type(), 0);

			// Parse the data from the message and extract player data
			String data[] = msg.getData().split(",");

			long totalDuration = Long.parseLong(data[1]);
			int timesPlayed = Integer.parseInt(data[2]);

			return new GameStats(gameId, totalDuration, timesPlayed);
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}
		
		return null;
	}

	/**
	 * Get a list of available game types from the server.
	 * @return A list of available game types.
	 */
	public static GameList getGameList() {

		// Send request
		sendMessage(new ServerMessage(mClientId,
				ServerMessage.ServerMessageType.GAME_LIST,
				""));

		// Get response
		GameList gameList = new GameList();
		
		try {
			Message msg = waitForMessage(ServerMessage.ServerMessageType.GAME_LIST.type(), 0);

			// Parse response

			String games[] = msg.getData().split(";");

			for (int i = 0; i < games.length; ++i) {
				String split[] = games[i].split(",");

				int id = Integer.parseInt(split[0]);
				String name = split[1];
				int playerCount = Integer.parseInt(split[2]);

				gameList.add(new GameData(id, name, playerCount));
			}
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}

		return gameList;
	}

	/**
	 * Send a message to the server.
	 * @param message The message to send.
	 * @return True if the message sent OK.
	 */
	public static boolean sendMessage(Message message) {
		mToServer.println(message.toString());

		raiseEvent(EventType.SENT_MESSAGE);
		return true;
	}

	/**
	 * Get a message from the server.
	 * @return The message.
	 */
	private static Message getMessageFromSocket() {
		String input = null;

		try {
			if (mFromServer.ready()) {
				input = mFromServer.readLine();
				mLastMessageTime = System.currentTimeMillis();

				raiseEvent(EventType.RECEIVED_MESSAGE);
			}
		} catch (IOException e) {
			// Ignore the exception
		}
		
		Message message = null;
		
		if (input != null) {
			
			// Split up message components
			String split[] = input.split(":");
			String header[] = split[0].split(",");

			int from = Integer.parseInt(header[0]);
			int to = Integer.parseInt(header[1]);
			int type = Integer.parseInt(header[2]);
			String data = "";

			if (split.length > 1) data = split[1];
			
			message = new Message(from, to, type, data);
		}
		
		return message;
	}

	/**
	 * Waits until a message of the specified type arrives.  All other messages
	 * are ignored.
	 * @param messageType The type of message awaited.
	 * @param waitTime Amount of time to wait before the attempt is retrieved.
	 * If 0, the method will wait forever and not time out.
	 * @return A message of the specified type.
	 * @throws ServerWaitTimeoutException Thrown if the waitTime is exceeded.
	 */
	public static Message waitForMessage(int messageType, long waitTime) throws ServerWaitTimeoutException {
		Message msg = null;
		long startTime = System.currentTimeMillis();

		while (true) {
			queueMessages();

			msg = mMessageQueue.pollMessageByType(messageType);

			if (msg != null) return msg;
			
			// Exceeded wait time?
			if (waitTime > 0) {
				if (startTime + waitTime > System.currentTimeMillis()) {
					throw new ServerWaitTimeoutException("Timeout waiting for server response.");
				}
			}
		}
	}

	/**
	 * Get the ID of this client.
	 * @return The ID of this client.
	 */
	public static int getClientId() { return mClientId; }
        
	/**
	 * Register new user on server.
	 * @param username The player's username.
	 * @param password The player's password.
	 * @return The player data from the server.
	 */
	public static PlayerData register(String username, String password) {

		StringBuilder sendMsg = new StringBuilder();
		
		sendMsg.append(username);
		sendMsg.append(",");
		sendMsg.append(password);

		sendMessage(new ServerMessage(mClientId,
				ServerMessage.ServerMessageType.REGISTER,
				sendMsg.toString()));

		try {
			Message msg = waitForMessage(ServerMessage.ServerMessageType.REGISTER.type(), 0);

			// Parse the data from the message and extract player data
			if (msg.getData().length() > 0) {

				// Registration OK
				String data[] = msg.getData().split(",");

				int playerId = Integer.parseInt(data[0]);
				String joinedDate = data[3];

				return new PlayerData(playerId, username, password, joinedDate);
			}
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}
		
		// Registration failed
		return null;
	}

	/**
	 * Get the stats for the current player.
	 * @return The player's stats.
	 */
	public static PlayerStats getPlayerStats() {

		sendMessage(new ServerMessage(mClientId,
				ServerMessage.ServerMessageType.PLAYER_STATS, ""));

		try {
			Message msg = Server.waitForMessage(ServerMessage.ServerMessageType.PLAYER_STATS.type(), 0);

			String data[] = msg.getData().split(",");

			String username = data[0];
			String favouriteGame = data[1];
			String lastGamePlayed = data[2];
			String lastGamePlayedDate = data[3];
			int numberOfGamesPlayed = Integer.parseInt(data[4]);
			int numberOfDisconnects = Integer.parseInt(data[5]);
			int numberOfGamesWon = Integer.parseInt(data[6]);
			String totalTimePlayed = data[7];
			String ranking = data[8];

			PlayerStats playerStats = new PlayerStats(username, favouriteGame, lastGamePlayed,
					lastGamePlayedDate, numberOfGamesPlayed, numberOfDisconnects, numberOfGamesWon,
					totalTimePlayed, ranking);

			return playerStats;
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}
		
		return null;
	}

	/**
	 * Notify server that the game has ended, and supply all scores associated
	 * with the game.
	 * @param resultList List of results to send.
	 */
	public static void sendEndGame(GameResultList resultList) {
		sendMessage(new ServerMessage(mClientId, ServerMessage.ServerMessageType.END_GAME, resultList.toString()));
	}

        
	/**
	 * Get the high scores for a specific game.
	 * @param gameId The ID of the game the user wants highscores for.
	 * @return A highscore data object containing the high scores for the requested game.
	 */
	public static HighScoreData getHighScoreData(int gameId) {

		// Send request for message
		sendMessage(new ServerMessage(mClientId, ServerMessage.ServerMessageType.HIGH_SCORES, String.valueOf(gameId)));

		HighScoreData scores = new HighScoreData(gameId);
		
		try {
			Message msg = Server.waitForMessage(ServerMessage.ServerMessageType.HIGH_SCORES.type(), 0);

			if (msg.getData().length() > 0) {
				String[] data = msg.getData().split(",");

				for (int i = 0; i < data.length; i += 2) {
					scores.add(new HighScore(Integer.parseInt(data[i]), data[i + 1]));
				}
			}
		} catch (ServerWaitTimeoutException e) {
			// Exception cannot be thrown as we wait forever
		}

		return scores;
	}

	/**
	 * Check if the server is dead.
	 * @return True if the server is dead.
	 */
	public static boolean isDead() { return mIsDead; }
}
