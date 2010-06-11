package pacman;

import controller.*;

/**
 * Local player class.  Extends the Player class with keyboard checking.
 */
public class LocalPlayer extends Player {

	// Members
	private KeyController mKeys;
	
	private int mBlockX;
	private int mBlockY;


	/**
	 * Constructor.
	 * @param name Name of the sprite.
	 * @param isGhost Set to true to make the sprite a ghost.
	 * @param clientId ID of this client.
	 */
	public LocalPlayer(String name, boolean isGhost, int clientId) {
		super(name, isGhost, clientId);

		mKeys = new KeyController();
		
		mBlockX = getSprite().getX() / Map.BLOCK_WIDTH;
		mBlockX = getSprite().getY() / Map.BLOCK_HEIGHT;
	}

	/**
	 * Extends Player class' run() method to include keyboard checking.
	 */
	@Override
	public void run() {
		super.run();
		checkKeys();
		
		int spriteBlockX = getSprite().getX() / Map.BLOCK_WIDTH;
		int spriteBlockY = getSprite().getY() / Map.BLOCK_HEIGHT;
		
		if ((mBlockX != spriteBlockX) || (mBlockY != spriteBlockY)) {
			mBlockX = spriteBlockX;
			mBlockY = spriteBlockY;
			
			GameServer.sendPlayerData(this);
		}
	}

	/**
	 * Get a pointer to the controller object.
	 * @return A pointer to the controller object.
	 */
	public KeyController getKeyController() { return mKeys; }

	/**
	 * Process keyboard input.
	 */
	private void checkKeys() {

		// Vertical movement
		if (mKeys.heldUp()) {
			if (getSprite().setDirection(Sprite.Direction.UP)) {
				// Send direction change to server
				GameServer.sendPlayerData(this);
			}
		} else if (mKeys.heldDown()) {
			if (getSprite().setDirection(Sprite.Direction.DOWN)) {
				// Send direction change to server
				GameServer.sendPlayerData(this);
			}
		}

		// Horizonal movement
		if (mKeys.heldLeft()) {
			if (getSprite().setDirection(Sprite.Direction.LEFT)) {
				// Send direction change to server
				GameServer.sendPlayerData(this);
			}
		} else if (mKeys.heldRight()) {
			if (getSprite().setDirection(Sprite.Direction.RIGHT)) {
				// Send direction change to server
				GameServer.sendPlayerData(this);
			}
		}
	}
}
