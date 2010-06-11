package comms;

import java.util.*;

/**
 * Maintains a queue of incoming messages.  Synchronized wrapper around a linked
 * list.
 */
public class MessageQueue {
	
	private LinkedList<Message> mList;
	
	/**
	 * Constructor.
	 */
	public MessageQueue() {
		mList = new LinkedList<Message>();
	}
	
	/**
	 * Retrieve and remove the head of the list.
	 * @return The message at the head of the list.
	 */
	public synchronized Message poll() { 
		return mList.poll();
	}
	
	/**
	 * Append the message to the end of the list.
	 * @param msg The message to add.
	 * @return True if the message was added.
	 */
	public synchronized boolean add(Message msg) {
		return mList.add(msg);
	}

	/**
	 * Locate, remove and return the first instance of a message of the
	 * specified type.
	 * @param messageType Type of the message to return.
	 * @return Message of the specified type.
	 */
	public synchronized Message pollMessageByType(int messageType) {
		for (Message message : mList) {
			if (message.getType() == messageType) {

				// Remove and return the message
				mList.remove(message);
				return message;
			}
		}

		return null;
	}
}
