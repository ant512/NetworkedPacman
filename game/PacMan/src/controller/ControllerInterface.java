package controller;

/**
 * Interface defining the basic methods of a game controller.
 */
public interface ControllerInterface {

	/**
	 * Enum listing bitmask values of keys.
	 */
	public enum KeyMask {
		UP (0x0001),
		DOWN (0x0010),
		LEFT (0x0100),
		RIGHT (0x1000);

		private final int mMask;

		KeyMask(int mask) {
			mMask = mask;
		}

		public int mask() { return mMask; }
	}

	/**
	 * Check if the up button is held.
	 * @return True if up button is held.
	 */
	public boolean heldUp();

	/**
	 * Check if the down button is held.
	 * @return True if down button is held.
	 */
	public boolean heldDown();

	/**
	 * Check if the left button is held.
	 * @return True if left button is held.
	 */
	public boolean heldLeft();

	/**
	 * Check if the right button is held.
	 * @return True if right button is held.
	 */
	public boolean heldRight();

	/**
	 * Set the specified key to held.
	 * @param key The key to hold.
	 */
	public void setKeyHeld(KeyMask key);

	/**
	 * Set the specified key to released.
	 * @param key The key to release.
	 */
	public void setKeyReleased(KeyMask key);
}
