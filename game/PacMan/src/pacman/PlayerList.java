package pacman;

import java.util.*;

/**
 * List of players.  Wraps around an array list in order to allow synchronized
 * access.
 */
public class PlayerList {

	// Members
	private ArrayList<Player> mList;

	/**
	 * Constructor.
	 */
	public PlayerList() {
		mList = new ArrayList<Player>();
	}

	/**
	 * Retrieve a player by the ID of the client controlling it.
	 * @param id The id of the sprite's controlling client.
	 * @return The sprite.
	 */
	public synchronized Player getPlayerByClientID(int id) {
		for (Player player : mList) {
			if (player.getClientId() == id) return player;
		}

		return null;
	}

	/**
	 * Get the player at the specified index.
	 * @param index Index of the player to retrieve,
	 * @return The player at the specified index.
	 */
	public synchronized Player get(int index) {
		return mList.get(index);
	}
	
	/**
	 * Remove the player from the list.
	 * @param player The player to remove.
	 * @return True if the player was successfully removed.
	 */
	public synchronized boolean remove(Player player) {
		return mList.remove(player);
	}

	/**
	 * Get the size of the list.
	 * @return The size of the list.
	 */
	public synchronized int size() {
		return mList.size();
	}

	/**
	 * Add a player to the list.
	 * @param player The player to add to the list.
	 * @return True if the player was added successfully.
	 */
	public synchronized boolean add(Player player) {
		return mList.add(player);
	}

	/**
	 * Make all sprites frozen.
	 */
	public synchronized void freeze() {
		for (Player player : mList) {
			player.getSprite().setFrozen(true);
		}
	}

	/**
	 * Make all normal ghosts edible.
	 */
	public synchronized void makeNormalEdible() {
		for (Player player : mList) {
			try {
				if (player.getSprite().getClass() == Class.forName("pacman.Ghost")) {
					((Ghost)player.getSprite()).makeEdible();
				}
			} catch (ClassNotFoundException e) {
				util.Debug.print("Error: Ghost class does not exist: " + e);
			}
		}
	}

	/**
	 * Make all edible ghosts flash.
	 */
	public synchronized void makeEdibleFlash() {
		for (Player player : mList) {
			try {
				if (player.getSprite().getClass() == Class.forName("pacman.Ghost")) {
					((Ghost)player.getSprite()).makeFlash();
				}
			} catch (ClassNotFoundException e) {
				util.Debug.print("Error: Ghost class does not exist: " + e);
			}
		}
	}

	/**
	 * Make all flashing ghosts normal.
	 */
	public synchronized void makeFlashingNormal() {
		for (Player player : mList) {
			try {
				if (player.getSprite().getClass() == Class.forName("pacman.Ghost")) {
					if (((Ghost)player.getSprite()).isFlashing()) {
						((Ghost)player.getSprite()).makeNormal();
					}
				}
			} catch (ClassNotFoundException e) {
				util.Debug.print("Error: Ghost class does not exist: " + e);
			}
		}
	}
}
