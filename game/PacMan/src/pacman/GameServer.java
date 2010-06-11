package pacman;

import comms.Server;

/**
 * Handles all messages that need to be sent to the server.
 */
public class GameServer {

	/**
	 * Constructor.  Private to prevent construction.
	 */
	private GameServer() { }


	/**
	 * Send all data from the player to the server, to be broadcast to all other
	 * clients.
	 * @param player Player to send to server.
	 */
	public static void sendPlayerData(Player player) {
		
		// Message data
		StringBuilder data = new StringBuilder();

		data.append(player.getSprite().getX());
		data.append(",");
		data.append(player.getSprite().getY());
		data.append(",");
		data.append(player.getSprite().getDirection());
		data.append(",");
		data.append(player.getSprite().getBufferedDirection());

		GameMessage message = new GameMessage(player.getClientId(),
				GameMessage.ADDRESS_ALL_CLIENTS,
				GameMessage.GameMessageType.SPRITE_DATA,
				data.toString());
			
		Server.sendMessage(message);
	}
}
