package comms;

/**
 * Basic message class.
 * Message format: header:data
 * Header format: from,to,messageID
 * Data format: Method specific
 * From/to: 0 for server, -1 for all clients, >0 for specific client
 */
public class Message {

	// Constants
	
	/** Address of the server */
	public static final int ADDRESS_SERVER = 0;
	
	/** Messages to this address get sent to all clients */
	public static final int ADDRESS_ALL_CLIENTS = -1;

	// Members
	private int mTo;
	private int mFrom;
	private int mType;
	private String mData;

	/**
	 * Constructor.
	 * @param from Source id.
	 * @param to Destination id.
	 * @param type Type of the message.
	 * @param data Data of the message.
	 */
	public Message(int from, int to, int type, String data) {
		mTo = to;
		mFrom = from;
		mType = type;
		mData = data;
	}

	/**
	 * Get the address that the message is sent to.  A value of 0 means the
	 * message is being sent to the server.  -1 means that the message is being
	 * sent to all clients.  Values greater than 0 mean the message is being
	 * sent to the client with that id.
	 * @return The id of the recipient.
	 */
	public int getTo() { return mTo; }

	/**
	 * Get the address that the message is being sent from.  A value of 0 means
	 * the message is being sent to the server.  -1 means that the message is
	 * being sent to all clients.  Values greater than 0 mean the message is
	 * being sent to the client with that id.
	 * @return The id of the sender.
	 */
	public int getFrom() { return mFrom; }

	/**
	 * Get the type of the message.
	 * @return The type of the message.
	 */
	public int getType() { return mType; }

	/**
	 * Get the message data.
	 * @return The message data.
	 */
	public String getData() { return mData; }

	/**
	 * Get the string representation of the message.
	 * @return The string representation of the message.
	 */
	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder();

		msg.append(mFrom);
		msg.append(",");
		msg.append(mTo);
		msg.append(",");
		msg.append(mType);
		msg.append(":");
		msg.append(mData);

		return msg.toString();
	}
}
