package pacman;

/**
 * Basic player class.
 */
public abstract class Player {

	// Members
	private Sprite mSprite;
	private int mClientId;

	/**
	 * Constructor.
	 * @param name Name of the sprite.
	 * @param isGhost Set to true to make the sprite a ghost.
	 * @param clientId ID of this client.
	 */
	public Player(String name, boolean isGhost, int clientId) {

		mClientId = clientId;

		if (isGhost) {

			// Choose ghost colour based on number of assigned players
			int size = PacManGame.getGame().getPlayerList().size();
			Ghost.GhostColour colour = Ghost.GhostColour.RED;

			switch ((size - 1) % 4) {
				case 0:
					colour = Ghost.GhostColour.RED;
					break;
				case 1:
					colour = Ghost.GhostColour.BLUE;
					break;
				case 2:
					colour = Ghost.GhostColour.PINK;
					break;
				case 3:
					colour = Ghost.GhostColour.ORANGE;
					break;
			}

			mSprite = new Ghost(name, colour);
		} else {
			mSprite = new PacMan(name);
		}
	}

	/**
	 * Run any player code and execute the sprite's run method.
	 */
	public void run() {
		mSprite.run();
	}

	/**
	 * Get the player's sprite.
	 * @return The player's sprite.
	 */
	public Sprite getSprite() { return mSprite; }

	/**
	 * Get the ID of the client controlling this player.
	 * @return The ID of the client controlling this player.
	 */
	public int getClientId() { return mClientId; }
}
