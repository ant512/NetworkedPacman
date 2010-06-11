package pacman;

import comms.*;

/**
 * Class for remotely-controlled players.
 */
public class RemotePlayer extends Player {

	/**
	 * Constructor.
	 * @param name Name of the sprite.
	 * @param isGhost Set to true to make the sprite a ghost.
	 * @param clientId clientId of the remote player controlling the sprite.
	 */
	public RemotePlayer(String name, boolean isGhost, int clientId) {
		super(name, isGhost, clientId);
	}

	/**
	 * Extract new position data from the supplied message and update the sprite
	 * location.
	 * @param message The message to process
	 * @return True if the message was processed
	 */
	public boolean processSpriteDataMessage(Message message) {
		
		// Validate the message
		if (message.getFrom() != getClientId()) return false;
		if (message.getType() != GameMessage.GameMessageType.SPRITE_DATA.type()) return false;
		
		// Message OK, extract data
		String split[] = message.getData().split(",");

		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);
		Sprite.Direction direction = Sprite.Direction.valueOf(split[2]);
		Sprite.Direction bufferedDirection = Sprite.Direction.valueOf(split[3]);

		// Update sprite properties
		getSprite().overridePosition(x, y, direction, bufferedDirection);

		return true;
	}
}
