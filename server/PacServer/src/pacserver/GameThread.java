package pacserver;

import database.*;
import java.util.*;

/**
 * Class for handling connections to clients in a separate thread.
 */
public class GameThread extends PacThread {

	// Members
	private GameData mGameData;
	private int mSessionId;
	private long mStartTime;
	private boolean mRunning;
	private boolean mIsFull;
	
	/**
	 * Constructor.
	 * @param gameData Data describing the type of game to be created.
	 * @param sessionId ID of this game session.
	 */
	public GameThread(GameData gameData, int sessionId) {
		super("GameThread");
		
		mGameData = gameData;
		mSessionId = sessionId;
		mStartTime = System.currentTimeMillis();
		mRunning = true;
		mIsFull = false;

		Debug.print("Game: New thread created");
	}

	/**
	 * Extend base run method to implement game over checking.
	 */
	@Override
	public void run() {

		while (mRunning) {

			// Runs client code for all clients.
			for (int i = 0; i < getClientCount(); ++i) {
				getClientList().get(i).run();
			}

			// Process any pending messages
			MessageHandler.processMessages(getPostBox(), getClientList());

			removeDeadClients();

			// Check for game over scenario
			handleGameOver();
		}
		
		Debug.print("Exiting game session");

		LobbyThread.getLobbyThread().removeThread(this);

		interrupt();
	}

	/**
	 * Add a new client to the thread and redirect its postbox.
	 * @param client The client to add to this thread
	 */
	@Override
	public void addClient(Client client) {

		if (!mIsFull) {
			super.addClient(client);
			client.setPostBox(getPostBox());

			// If we have enough clients, start the game
			if (getClientCount() == mGameData.getPlayers()) {
				mIsFull = true;
				startGame();
			}
		}
	}

	/**
	 * Start the game - sends game session data to all connected clients.
	 */
	private void startGame() {
		for (int i = 0; i < getClientCount(); ++i) {
			getClientList().get(i).sendPeerList(getClientList());
		}
	}

	/**
	 * Move clients back to the lobby and redirect them back to the lobby
	 * postbox.
	 */
	private void endGame() {

		Debug.print("Moving clients back to lobby");

		// Move clients back to lobby
		while (getClientCount() > 0) {
			Client client = getClientList().get(getClientCount() - 1);

			removeClient(client);
			LobbyThread.getLobbyThread().addClient(client);
		}

		mRunning = false;
	}

	/**
	 * Check if all clients are reporting game over.
	 */
	private boolean isGameOver() {

		// If no clients left, game is over
		if (getClientCount() == 0) return true;

		// Check if any clients not reporting game over
		for (int i = 0; i < getClientCount(); ++i) {

			// If game not ended, return false
			if (!getClientList().get(i).isGameOver()) return false;
		}

		// Game ended in all clients, so return true
		return true;
	}

	/**
	 * Handle game over state, if applicable.
	 */
	private void handleGameOver() {

		// Check for game over scenario
		if (isGameOver()) {

			Debug.print("Game over");

			// Calculate game duration
			long duration = System.currentTimeMillis() - mStartTime;

			// Get the validated results from all clients
			GameResultList resultList = getValidatedResults();

			// Append the list of clients that disconnected
			appendDeadClients(resultList);

			// Save the results
			Database.saveGameResults(mGameData.getId(), mGameData.getPlayers(), mStartTime, duration, resultList);

			endGame();
		}
	}

	/**
	 * Get the ID of this game session.
	 * @return The ID of this game session.
	 */
	public int getSessionId() { return mSessionId; }

	/**
	 * Use "byzantine generals" approach to determine correct final score.
	 * Method checks all results of all clients for discrepancies, and if it
	 * finds any it goes with the majority.  If no majority can be reached, it
	 * chooses the first set of results.  This aims to prevent against hacking
	 * attempts, and ensures all clients are treated as peers.
	 */
	private GameResultList getValidatedResults() {

		Debug.print("Validating game results");

		GameResultList outputList = new GameResultList();

		// Loop through all results (there should be as many results as there
		// are clients)
		for (int i = 0; i < getClientCount(); ++i) {

			// Hash map stores collated result for each player.  Key is score,
			// frequency of score is value
			HashMap<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

			// Get the ID of the player being examined
			int playerId = getClientList().get(0).getResultList().get(i).getPlayerId();

			// Collate scores for this player ID from all clients
			for (int j = 0; j < getClientCount(); ++j) {

				// Get the score for this player as reported by client j
				int score = getClientList().get(j).getResultList().getScoreForPlayer(playerId);
				
				// Try to retrieve an existing frequency from the map
				Integer storedFrequency = resultMap.get(score);

				// Does the frequency already exist?
				if (storedFrequency == null) {

					// New frequency, so store it in the map
					storedFrequency = new Integer(1);

					resultMap.put(score, storedFrequency);
				} else {

					// Existing frequency, so increase it.
					resultMap.remove(score);
					storedFrequency++;
					resultMap.put(score, storedFrequency);
				}
			}

			// Validate the scores
			Set<Integer> keySet = resultMap.keySet();
			Iterator<Integer> iterator = keySet.iterator();

			// Any discrepancies detected?
			if (keySet.size() > 1) Debug.print("Error in scores detected!");

			int highestFrequency = 0;
			int bestScore = 0;

			// Loop through all scores identifying the most frequent.  In the
			// case of a tie, the first observed score is chosen
			while (iterator.hasNext()) {

				int score = iterator.next();
				int frequency = resultMap.get(score);

				if (frequency > highestFrequency) {
					bestScore = score;
					highestFrequency = frequency;
				}
			}

			// Store the validated scores in the output list
			outputList.add(new GameResultData(playerId, bestScore, false));

			Debug.print("Player: " + playerId + " Score: " + bestScore);
		}

		return outputList;
	}

	/**
	 * Append list of dead client results to the supplied results list.
	 * Disconnected clients are marked as such, and given a score of 0.
	 * @param resultList The list to add to.
	 */
	private void appendDeadClients(GameResultList resultList) {
		for (int i = 0; i < getDeadClientList().size(); ++i) {
			resultList.add(new GameResultData(getDeadClientList().get(i).getPlayerId(), 0, true));
		}
	}

	/**
	 * Check if this thread is waiting for players.
	 * @return True if the thread is waiting for players
	 */
	public boolean isWaitingForPlayers() {
		return (getClientCount() < mGameData.getPlayers());
	}

	/**
	 * Get the ID of the game being played.
	 * @return The ID of the game being played.
	 */
	public int getGameId() {
		return mGameData.getId();
	}
	
	/**
	 * Remove dead clients from the client list and store them in the dead
	 * client list.  Notifies all remaining clients of the new dead clients.
	 */
	@Override
	protected void removeDeadClients() {
		
		// Get the count of current dead clients
		int currentDeadClients = getDeadClientList().size();
		
		// Move all dead clients
		int i = 0;
		while (i < getClientCount()) {
			
			Client client = getClientList().get(i);
			
			if (client.isDead()) {
				
				// Client dead, so move to the dead client list
				getClientList().remove(client);
				
				client.setPostBox(null);
				
				getDeadClientList().add(client);
				--i;
			}
			
			++i;
		}
		
		// Notify remaining clients of dead clients
		for (int k = currentDeadClients; k < getDeadClientList().size(); ++k) {

			Client deadClient = getDeadClientList().get(k);

			for (int j = 0; j < getClientCount(); ++j) {
				Client client = getClientList().get(j);

				client.sendClientFailure(deadClient.getId());
			}
		}
	}
}

