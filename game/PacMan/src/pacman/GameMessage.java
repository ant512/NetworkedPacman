package pacman;

import comms.*;

/**
 * Subclass of message intended for use with messages destined for other
 * clients.
 */
public class GameMessage extends Message {

	/**
	 * Enum listing all game message types.
	 */
	public enum GameMessageType {
		SPRITE_DATA (1);

		private final int mType;

		GameMessageType(int type) {
			mType = type;
		}

		public int type() { return mType; }
	}

	/**
	 * Constructor.
	 * @param from Destination id.
	 * @param to Source id.
	 * @param type Type of the message.
	 * @param data Message data.
	 */
	public GameMessage(int from, int to, GameMessageType type, String data) {
		super(from, to, type.type(), data);
	}

	/**
	 * Constructor.  Less robust as the message type is not type-safe.
	 * @param from Destination id.
	 * @param to Source id.
	 * @param type Type of the message.
	 * @param data Message data.
	 */
	public GameMessage(int from, int to, int type, String data) {
		super(from, to, type, data);
	}
}
