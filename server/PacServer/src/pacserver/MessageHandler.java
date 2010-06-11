package pacserver;

import database.*;

/**
 * Deliver messages from the post box to the correct location.
 */
public class MessageHandler {
	
	/**
	 * Process all messages.
	 * @param postBox Post box to fetch messages from.
	 * @param clientList Client list to deliver to.
	 */
	public static void processMessages(PostBox postBox, ClientList clientList) {
		String msg = null;

		while (postBox.size() > 0) {
			msg = postBox.getMessage();
			processMessage(msg, clientList);
		}
	}

	/**
	 * Process a message.
	 * @param msg Message to send.
	 * @param clientList Client list to deliver to.
	 */
	private static void processMessage(String msg, ClientList clientList) {

		// Break message into header and its subcomponents
		String messageSplit[] = msg.split(":");
		String header[] = messageSplit[0].split(",");

		// Check if we have a valid message header
		if (header.length == 3) {

			// Got a valid message; work out where to deliver it
			int from = Integer.valueOf(header[0]);
			int to = Integer.valueOf(header[1]);
			int messageType = Integer.valueOf(header[2]);

			// Deliver message to correct location
			switch (to) {
				case Client.ADDRESS_ALL_CLIENTS:

					Debug.print("Message: to all clients: " + msg.toString());

					// Deliver message to all clients except the originator
					for (int i = 0; i < clientList.size(); ++i) {
						if (clientList.get(i).getId() != from) {
							clientList.get(i).sendMessage(msg);
						}
					}
					break;
				case Client.ADDRESS_SERVER:

					// Message is intended for the server
					
					// Get the client from the client list
					Client client = clientList.getClientByID(from);

					switch (messageType) {
						case Client.MESSAGE_LOGOUT:

							// Client has quit
							client.logout();
							break;

						case Client.MESSAGE_PEER_LIST:

							// Client wants the peer list
							client.sendPeerList(clientList);
							break;

						case Client.MESSAGE_PLAYER_DATA:

							// Client wants player data
							sendPlayerData(client, messageSplit[1]);
							break;
							
						case Client.MESSAGE_LOGIN:
							
							// Client wants to authenticate
							authenticate(client, messageSplit[1]);
							break;

						case Client.MESSAGE_JOIN_GAME:

							// Client wants to join a game
							joinGame(client, messageSplit[1]);
							break;

						case Client.MESSAGE_GAME_LIST:

							// Client wants the list of game types
							client.sendGameList();
							break;

						case Client.MESSAGE_REGISTER:

							// New player wants to register.
							register(client, messageSplit[1]);
							break;

						case Client.MESSAGE_PLAYER_STATS:

							// Client wants their stats.
							client.sendPlayerStats();
							break;

						case Client.MESSAGE_GAME_END:

							// Client reports a game has ended
							endGame(client, clientList, messageSplit[1]);
							break;

						case Client.MESSAGE_HIGH_SCORES:

							// Client wants high scores for a game.
							sendHighScores(client, messageSplit[1]);
							break;
							
						case Client.MESSAGE_PING:
							
							// Client sending a ping
							Debug.print("Pong! " + from);
							break;
							
						case Client.MESSAGE_GAME_STATS:
							
							// Client wants game stats
							sendGameStats(client, messageSplit[1]);
							break;
							
					}
					break;
				default:

					// Message is intended for a specific client
					Client toClient = clientList.getClientByID(to);

					if (toClient != null) {
						toClient.sendMessage(msg);
					}
					break;
			}
		}
	}
	
	/**
	 * Attempt to authenticate the client.
	 * @param client The client to authenticate.
	 * @param data The data associated with an authentication message.
	 */
	private static void authenticate(Client client, String data) {
		String split[] = data.split(",");
		String username = split[0];
		String password = split[1];
		
		client.authenticate(username, password);
	}

	/**
	 * Attempt to send player data to the client.
	 * @param client The client to send the data to.
	 * @param data The data associated with an authentication message.
	 */
	private static void sendPlayerData(Client client, String data) {

		PlayerData playerData = Database.getPlayer(data);

		client.sendPlayerData(playerData);
	}

	/**
	 * Create a new game session.
	 * @param client The client that is creating the session.
	 * @param data The data associated with the create game message.
	 */
	private static void joinGame(Client client, String data) {
		int gameId = Integer.parseInt(data);

		// Find the game data
		GameList gameList = Database.getGameList();

		GameData gameData = gameList.getGameDataByID(gameId);

		// Create a new thread and move the client into it
		GameThread thread = LobbyThread.getLobbyThread().joinGame(gameData, client);

		// Send the session ID back to the client
		client.sendGameSessionId(thread.getSessionId());
	}
        
	/**
	 * Attempt to register a new player.
	 * @param client The client to register.
	 * @param data The data associated with a registration message.
	 */
	private static void register(Client client, String data) {
		String split[] = data.split(",");
		String username = split[0];
		String password = split[1];

		client.register(username, password);
	}

	/**
	 * Game has ended - parse results and store them in the client.
	 * @param client Client to store results in.
	 * @param clientList The clientList that the client belongs to.
	 * @param data Data associated with the end game message.  Formatted as
	 * clientid,score;clientid,score;clientid,score;etc.
	 */
	private static void endGame(Client client, ClientList clientList, String data) {

		// Extract data and store in result list
		String split[] = data.split(";");

		GameResultList resultList = new GameResultList();

		for (int i = 0; i < split.length; ++i) {

			String result[] = split[i].split(",");

			int clientId = Integer.parseInt(result[0]);
			int score = Integer.parseInt(result[1]);

			// Retrieve the player ID from the client list
			int playerId = clientList.getClientByID(clientId).getPlayerId();

			resultList.add(new GameResultData(playerId, score, false));
		}

		client.setResultList(resultList);
	}

	/**
	 * Client wants to see highscores for a game (specified by game Id).
	 * @param client the client.
	 * @param data Data associated with the request. Formatted as gameId.
	 */
	private static void sendHighScores(Client client, String data) {
		int gameId = Integer.parseInt(data);
		client.sendHighScores(gameId);
	}
	
	/**
	 * Client wants game stats.
	 * @param client The client that wants the stats.
	 * @param data Data associated with the request.  Formatted as gameId.
	 */
	private static void sendGameStats(Client client, String data) {
		int gameId = Integer.parseInt(data);
		client.sendGameStats(gameId);
	}
}
