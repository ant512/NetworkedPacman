package pacserver;

import java.util.*;

/**
 * List of game threads.  Wrapper around the ArrayList class offering
 * synchronized access.
 */
public class GameThreadList {

	// Members
	private ArrayList<GameThread> mList;

	/**
	 * Constructor.
	 */
	public GameThreadList() {
		mList = new ArrayList<GameThread>();
	}

	/**
	 * Get the thread at the specified index.
	 * @param i The index of the thread to retrieve.
	 * @return The thread at the specified index.
	 */
	public synchronized GameThread get(int i) {
		if (i < mList.size()) return mList.get(i);
		return null;
	}

	/**
	 * Add the supplied thread to the list.
	 * @param thread Thread to add to the list.
	 */
	public synchronized void add(GameThread thread) {
		mList.add(thread);
	}

	/**
	 * Remove the specified thread from the list.
	 * @param thread The thread to remove.
	 */
	public synchronized void remove(GameThread thread) {
		mList.remove(thread);
	}

	/**
	 * Retrieve a thread by its session ID.
	 * @param id The session id of the thread.
	 * @return The thread.
	 */
	public synchronized GameThread getGameThreadBySessionID(int id) {
		for (GameThread thread : mList) {
			if (thread.getSessionId() == id) return thread;
		}

		return null;
	}

	/**
	 * Get the number of threads in the list.
	 * @return The number of threads in the list.
	 */
	public synchronized int size() {
		return mList.size();
	}
}
