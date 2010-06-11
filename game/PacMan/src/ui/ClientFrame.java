package ui;

import java.util.Timer;
import java.util.TimerTask;
import comms.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Extends JFrame with a timer that pings the server im order to keep the
 * connection alive.
 */
public class ClientFrame extends JFrame {

	// Constants
	
	/** Rate at which the server message queue is checked and pings are sent */
	final public static int REFRESH_RATE = 25;

	// Members
	private Timer mTimer;

	/**
	 * Constructor.
	 * @param name Name for the frame.
	 */
	public ClientFrame(String name) {
		super(name);

		// Create and start the timer that keeps the server connection alive
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new UpdateTask(), REFRESH_RATE, REFRESH_RATE);

		// Anonymous window event class
		addWindowListener(new WindowAdapter() {

			/**
			 * Quit the system correctly when the window closes.
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				client.Main.quit();
			}
		});
	}

	/**
	 * Timer listener - refreshes the display.
	 */
	private class UpdateTask extends TimerTask {
		public void run() {
			Server.processMessages(null);
		}
	}

	/**
	 * Stops the timer and disposes the frame.
	 */
	@Override
	public void dispose() {
		mTimer.cancel();
		super.dispose();
	}
}
