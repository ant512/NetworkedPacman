package pacman;

import java.awt.*;
import comms.*;

/**
 * Main PacMan game class.
 */
public class PacManGame {

	/**
	 * Enum defining all possible states for the game.
	 */
	private enum GameState {
		WAITING_FOR_PLAYERS,
		INTRO_START,
		INTRO_RUNNING,
		RUNNING,
		LOST_LIFE,
		GAME_OVER;
	}

	// Static members
	private static PacManGame mGame;
	
	// Members
	private PacFrame mFrame;
	private PlayerList mPlayerList;
	private Map mMap;
	private LocalPlayer mPlayer;
	private MessageHandler mMessageHandler;
	private GameState mGameState;
	private ScoreDisplay mScoreDisplay;

	/**
	 * Get a pointer to the list of sprites in the game.
	 * @return A pointer to the list of sprites in the game.
	 */
	public PlayerList getPlayerList() { return mPlayerList; }

	/**
	 * Get a pointer to the map.
	 * @return A pointer to the map.
	 */
	public Map getMap() { return mMap; }

	/**
	 * Get a pointer to the local player.
	 * @return A pointer to the local player.
	 */
	public LocalPlayer getLocalPlayer() { return mPlayer; }

	/**
	 * Constructor.  Private to enforce singleton pattern.
	 */
    private PacManGame() { }

	/**
	 * Initialise static game.
	 */
	public static void init() {
		mGame = new PacManGame();

		SoundPlayer.init();
		BitmapServer.init();

		mGame.mGameState = GameState.WAITING_FOR_PLAYERS;

		mGame.mPlayerList = new PlayerList();

		mGame.mScoreDisplay = new ScoreDisplay();

		// Create game window
		mGame.mFrame = new PacFrame(mGame);

		mGame.draw(mGame.mFrame.getGraphics());

		// Get game session data from server
		PeerList peerList = null;
		
		try {
			peerList = Server.waitForPeerList(0);
		} catch (ServerWaitTimeoutException e) {
			util.Debug.print(e.toString());
		}

		mGame.mMap = new Map(mGame.mFrame.createImage(Map.getWidth(), Map.getHeight()));
		mGame.mMessageHandler = new MessageHandler();

		mGame.addPlayers(peerList);

		// Wire up keyboard to player
		mGame.mFrame.addKeyListener(mGame.mPlayer.getKeyController());

		mGame.mGameState = GameState.INTRO_START;
	}

	/**
	 * Get a pointer to the game singleton.
	 * @return Pointer to the game singleton.
	 */
	public static PacManGame getGame() {
		return mGame;
	}
	
	/**
	 * Draw the game.
	 * @param g Graphics object to draw to.
	 */
	public void draw(Graphics g) {

		// Choose what to draw based on current state of the game
		switch (mGameState) {
			case WAITING_FOR_PLAYERS:

				// Game not yet ready - waiting for other players to connect
				g.clearRect(0, 0, PacFrame.FRAME_WIDTH, PacFrame.FRAME_HEIGHT);
				g.drawString("Waiting for players...", 70, 120);
				break;

			case INTRO_START:

				// Play the intro sound
				SoundPlayer.playSound(SoundPlayer.Sound.START);

				mGameState = GameState.INTRO_RUNNING;
			case INTRO_RUNNING:

				// Intro starts - draw the game
				drawGame(g);

				if (!SoundPlayer.isSoundPlaying(SoundPlayer.Sound.START)) {
					mGameState = GameState.RUNNING;
				}
				
				break;
				
			case RUNNING:
			case LOST_LIFE:

				// Game in action
				drawGame(g);
				break;

			case GAME_OVER:

				drawGame(g);
				g.setColor(new Color(255, 255, 255));
				g.drawString("Game Over", 110, 205);
				break;
		}
	}
	
	/**
	 * Process a single iteration of the game.
	 */
	public void run() {

		// Run the powerpill timer
		PowerPillTimer.run();
		
		switch (mGameState) {
			case LOST_LIFE:

				// Player has lost a life - wait until gameover sound stops
				// before resuming
				if (!SoundPlayer.isSoundPlaying(SoundPlayer.Sound.DEATH)) {
					resetPlayers();
					mGameState = GameState.RUNNING;
				}
				break;

			case GAME_OVER:

				// Game over - wait until gameover sound stops
				// before quitting
				if (!SoundPlayer.isSoundPlaying(SoundPlayer.Sound.DEATH)) {
					shutdown();
					new ui.Menu();
				}

		}

		// Run all sprites
		for (int i = 0; i < mPlayerList.size(); ++i) {
			mPlayerList.get(i).run();
		}

		// Process all messages
		Server.processMessages(mMessageHandler);

		// Check that the server is running
		if (Server.isDead()) {
			shutdown();
			new ui.Menu();
		}
	}

	/**
	 * Add all players to the game.
	 * @param peerList List of the peers in this game.
	 */
	private void addPlayers(PeerList peerList) {

		// Add a player for each entry in the game session data object's list of
		// clients
		PeerData clientData = null;
		boolean isGhost = false;

		for (int i = 0; i < peerList.getPeerDataCount(); ++i) {

			// Choose sprite properties based on whether the client is a ghost
			// or pacman
			isGhost = (i > 0);

			clientData = peerList.get(i);

			// Is this a remote or local player?
			if (clientData.getClientId() == Server.getClientId()) {

				// Local player
				mPlayer = new LocalPlayer(
						clientData.getUsername(),
						isGhost,
						clientData.getClientId());
				mPlayerList.add(mPlayer);
			} else {

				// Remote player
				mPlayerList.add(new RemotePlayer(
						clientData.getUsername(),
						isGhost,
						peerList.get(i).getClientId()));
			}
		}
	}

	/**
	 * Reset players to their default locations
	 */
	public void resetPlayers() {
		for (int i = 0; i < mPlayerList.size(); ++i) {
			mPlayerList.get(i).getSprite().reset();
		}
	}

	/**
	 * Draw the components of the game.
	 * @param g Graphics object to draw to.
	 */
	private void drawGame(Graphics g) {
		mMap.draw(g);

		// Draw all sprites
		for (int i = 0; i < mPlayerList.size(); ++i) {
			mPlayerList.get(i).getSprite().draw(g);
		}
		
		mScoreDisplay.draw(g);
	}

	/**
	 * Switch to lost life mode.
	 */
	public void loseLife() {

		// Ensure the ghost scared sound is not running
		PowerPillTimer.stop();

		mGameState = GameState.LOST_LIFE;
		SoundPlayer.playSound(SoundPlayer.Sound.DEATH);

		mPlayerList.freeze();
	}

	/**
	 * Switch to game over mode.
	 */
	public void endGame() {

		// Ensure the ghost scared sound is not running
		PowerPillTimer.stop();

		SoundPlayer.playSound(SoundPlayer.Sound.DEATH);
		mPlayerList.freeze();

		mGameState = GameState.GAME_OVER;
	}

	/**
	 * Quit the game - sends end game message to server.
	 */
	public void shutdown() {

		// Only send results if game is over
		if (mGameState == GameState.GAME_OVER) {

			// Create results list
			GameResultList resultList = new GameResultList();

			for (int i = 0; i < mPlayerList.size(); ++i) {
				Player player = mPlayerList.get(i);
				resultList.add(new GameResultData(player.getClientId(), player.getSprite().getScoreValue()));
			}
			
			Server.sendEndGame(resultList);
		}

		// Free sounds
		SoundPlayer.shutdown();

		mFrame.dispose();
	}
}
