package controller;

import java.awt.event.*;

/**
 * Reads keyboard input via KeyListener messages and remembers the status of
 * the keyboard.
 */
public class KeyController extends KeyAdapter implements ControllerInterface {

	// Constants
	final static private int KEY_UP = 38;
	final static private int KEY_DOWN = 40;
	final static private int KEY_LEFT = 37;
	final static private int KEY_RIGHT = 39;

	// Members
	private int mHeldKeys = 0;
	
	/**
	 * Is the up key held?
	 * @return True if the up key is held.
	 */
	public boolean heldUp() { return (mHeldKeys & KeyMask.UP.mask()) > 0; }

	/**
	 * Is the down key held?
	 * @return True if the down key is held.
	 */
	public boolean heldDown() { return (mHeldKeys & KeyMask.DOWN.mask()) > 0; }

	/**
	 * Is the left key held?
	 * @return True if the left key is held.
	 */
	public boolean heldLeft() { return (mHeldKeys & KeyMask.LEFT.mask()) > 0; }

	/**
	 * Is the right key held?
	 * @return True if the right key is held.
	 */
	public boolean heldRight() { return (mHeldKeys & KeyMask.RIGHT.mask()) > 0; }

	/**
	 * Hold the specified key.
	 * @param key Key to become held.
	 */
	public void setKeyHeld(KeyMask key) { mHeldKeys |= key.mask(); }

	/**
	 * Release the specified key.
	 * @param key Key to become released.
	 */
	public void setKeyReleased(KeyMask key) { mHeldKeys &= ~key.mask(); }
	
	/**
	 * Handle a key press event
	 * @param evt Event to handle
	 */
	@Override
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
			case KEY_UP:
				setKeyHeld(KeyMask.UP);
				break;
			case KEY_DOWN:
				setKeyHeld(KeyMask.DOWN);
				break;
			case KEY_LEFT:
				setKeyHeld(KeyMask.LEFT);
				break;
			case KEY_RIGHT:
				setKeyHeld(KeyMask.RIGHT);
				break;
		}
	}
		
	/**
	 * Handle a key release event
	 * @param evt Event to handle
	 */
	@Override
	public void keyReleased(KeyEvent evt) {
		switch (evt.getKeyCode()) {
			case KEY_UP:
				setKeyReleased(KeyMask.UP);
				break;
			case KEY_DOWN:
				setKeyReleased(KeyMask.DOWN);
				break;
			case KEY_LEFT:
				setKeyReleased(KeyMask.LEFT);
				break;
			case KEY_RIGHT:
				setKeyReleased(KeyMask.RIGHT);
				break;
		}
	}
}
