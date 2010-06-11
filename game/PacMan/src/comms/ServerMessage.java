package comms;

/**
 * Subclass of message intended for use with messages destined for the server.
 */
public class ServerMessage extends Message {

	/**
	 * Enum defining all server message types.
	 */
	public enum ServerMessageType {
		HANDSHAKE (-1),
		LOGOUT (-2),
		LOGIN (-3),
		PEER_LIST (-4),
		PLAYER_DATA (-5),
		JOIN_GAME (-6),
		GAME_LIST (-7),
		REGISTER (-8),
		PLAYER_STATS (-9),
		END_GAME (-10),
		HIGH_SCORES (-11),
		PING (-12),
		CLIENT_FAILED (-13),
		GAME_STATS(-14);
                
		private final int mType;

		ServerMessageType(int type) {
			mType = type;
		}

		public int type() { return mType; }
	}

	/**
	 * Constructor.
	 * @param from Source id.
	 * @param type Type of the message.
	 * @param data Message data.
	 */
	public ServerMessage(int from, ServerMessageType type, String data) {
		super(from, ADDRESS_SERVER, type.type(), data);
	}

	/**
	 * Constructor.  Less robust as the message type is not type-safe.
	 * @param from Source id.
	 * @param type Type of the message.
	 * @param data Message data.
	 */
	public ServerMessage(int from, int type, String data) {
		super(ADDRESS_SERVER, from, type, data);
	}
}
