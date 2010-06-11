package pacman;

import java.awt.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Drawing surface for the game.
 */
public class PacPanel extends JPanel {

	// Constants
	
	/** Rate at which the game refreshes */
	final public static int REFRESH_RATE = 25;

	// Members
	private PacManGame mGame;
	private Timer mTimer;

	/**
	 * Constructor.
	 * @param game Pointer to the game.
	 */
	public PacPanel(PacManGame game) {
		super();
		mGame = game;

		// Create and start the timer that controls game updates
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new UpdateTask(), REFRESH_RATE, REFRESH_RATE);
	}

	/**
	 * Update the game and redraw the display.
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		mGame.run();
		mGame.draw(g);
	}


	/**
	 * Timer listener - refreshes the display.
	 */
	private class UpdateTask extends TimerTask {
		public void run() {
			repaint();
		}
	}
}
