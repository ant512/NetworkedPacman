package pacman;

import java.awt.event.*;
import javax.swing.*;
import controller.*;

/**
 * Main game window.
 */
public class PacFrame extends JFrame {

	// Constants
	
	/** Width of the frame */
	final public static int FRAME_WIDTH = 564;
	
	/** Height of the frame */
	final public static int FRAME_HEIGHT = 374;

	// Members
	private PacPanel mPanel;

	/**
	 * Constructor.
	 * @param game Pointer to the game.
	 */
	public PacFrame(PacManGame game) {
		super();
		
		setTitle("Go");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);

		mPanel = new PacPanel(game);
		getContentPane().add(mPanel);

		setVisible(true);

		// Anonymous window event class
		addWindowListener(new WindowAdapter() {

			/**
			 * Quit the game correctly when the window closes.
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				PacManGame.getGame().shutdown();
				client.Main.quit();
			}

			/**
			 * Forget all keys when the window deactivates (ie. loses focus)
			 */
			@Override
			public void windowDeactivated(WindowEvent e) {

				// Prevent against null pointers during initialisation
				if (PacManGame.getGame() == null) return;
				if (PacManGame.getGame().getLocalPlayer() == null) return;
				if (PacManGame.getGame().getLocalPlayer() == null) return;

				// Deactivate keys
				controller.KeyController keys = PacManGame.getGame().getLocalPlayer().getKeyController();
				keys.setKeyReleased(KeyController.KeyMask.UP);
				keys.setKeyReleased(KeyController.KeyMask.DOWN);
				keys.setKeyReleased(KeyController.KeyMask.LEFT);
				keys.setKeyReleased(KeyController.KeyMask.RIGHT);
			}
		});
	}
		
	/**
	 * Ensure that the panel adds the same listeners as the frame.
	 * @param l The listener to add.
	 */
	@Override
	public void addKeyListener(KeyListener l) {
		super.addKeyListener(l);
		this.getGlassPane().addKeyListener(l);
	}
}
