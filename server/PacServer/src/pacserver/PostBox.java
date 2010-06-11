package pacserver;

import java.util.*;

/**
 * Container for messages that are sent to the server, awaiting processing and
 * delivery.  Wrapper around a LinkedList that provides synchronized add and
 * retrieval.  Functions like a queue, so first messages in are first out.
 */
public class PostBox {
	
	// Members
	private LinkedList<String> mMessages;
	
	/**
	 * Constructor.
	 */
	public PostBox() {
		mMessages = new LinkedList<String>();
	}
	
	/**
	 * Get the first message in the queue.  Removes the message from the queue.
	 * @return The first message.
	 */
	public synchronized String getMessage() {
		return mMessages.poll();
	}
	
	/**
	 * Add a message to the end of the queue.
	 * @param msg The message to add to the queue.
	 */
	public synchronized void addMessage(String msg) {
		mMessages.add(msg);
	}
	
	/**
	 * Get the number of messages in the postbox.
	 * @return The number of messages in the postbox.
	 */
	public synchronized int size() {
		return mMessages.size();
	}
}
