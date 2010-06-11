package pacman;

import comms.*;

/**
 * Handles any incoming messages asynchronously.
 */
public class MessageHandler implements comms.MessageHandlerInterface {

	/**
	 * Constructor.
	 */
	public MessageHandler() {
	}

	/**
	 * Handle the supplied message appropriately.
	 * @param msg The message to handle.
	 */
	public void handleMessage(Message msg) {

		// Choose appropriate action based on message type
		if (msg.getType() == GameMessage.GameMessageType.SPRITE_DATA.type()) {
			handleSpriteData(msg);
		} else if (msg.getType() == ServerMessage.ServerMessageType.CLIENT_FAILED.type()) {
			handleClientFailure(Integer.parseInt(msg.getData()));
		}
	}

	/**
	 * Message received was client data - use this to control a remote sprite.
	 * @param clientId
	 * @param msg
	 */
	private void handleSpriteData(Message msg) {
		util.Debug.print("handleSpriteData: " + msg);
		
		// Get the remote sprite
		RemotePlayer remotePlayer = (RemotePlayer)PacManGame.getGame().getPlayerList().getPlayerByClientID(msg.getFrom());

		if (remotePlayer != null) {

			// Send the message to the sprite
			remotePlayer.processSpriteDataMessage(msg);
		} else {
			util.Debug.print("Cannot find remote sprite");
		}
	}
	
	/**
	 * Handle a client failure message.
	 * @param clientId The ID of the client that failed.
	 */
	public void handleClientFailure(int clientId) {
		util.Debug.print("handleClientFailure: " + clientId);
		
		// Get the remote player
		RemotePlayer remotePlayer = (RemotePlayer)PacManGame.getGame().getPlayerList().getPlayerByClientID(clientId);
		
		// Remove the player from the game
		PacManGame.getGame().getPlayerList().remove(remotePlayer);
		
		// Can the game continue?
		if (remotePlayer.getSprite().getClass().getName().equals("PacMan")) {
			
			// Player was PacMan, so abort the game
			PacManGame.getGame().endGame();
		} else if (PacManGame.getGame().getPlayerList().size() < 2) {
			
			// Only one player left, so abort game
			PacManGame.getGame().endGame();
		}	
	}
}
