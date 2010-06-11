package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import comms.*;

/**
 * Frame showing the main menu.
 * 
 * @author Joshua
 */
public class Menu extends ClientFrame {

	// Members
	private CenterPanel center;
	private comms.GameList mGameList;

	/**
	 * Constructor.
	 */
	public Menu() {
		super("Menu");

		// Get game list
		mGameList = Server.getGameList();

		center = new CenterPanel();

		// Set up the layout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(center, BorderLayout.CENTER);

		setSize(250, 200);
		this.setResizable(false);
		setVisible(true);
	}

	/**
	 * Panel containing all buttons.
	 */
	private class CenterPanel extends JPanel {

		// Members
		private JButton playgame, viewstatus, viewscore, logout;
		private Choice choice;

		/**
		 * Constructor.
		 */
		public CenterPanel() {
			choice = new Choice();
			choice.setBounds(80, 30, 80, 30);

			// Add games to list
			for (int i = 0; i < mGameList.size(); ++i) {
				choice.addItem(mGameList.get(i).getName());
			}
			
			playgame = new JButton("Play Game");
			viewstatus = new JButton("View Statistics");
			viewscore = new JButton("View High Score");
			logout = new JButton("Log Out");

			logout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logoutButton_actionPerformed(e);
				}
			});

			playgame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					playgameButton_actionPerformed(e);
				}
			});
			
			viewstatus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					viewstatusButton_actionPerformed(e);
				}
			});

			viewscore.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					viewscoreButton_actionPerformed(e);
				}
			});

			add(choice);
			add(playgame);
			add(viewstatus);
			add(viewscore);
			add(logout);
		}

		/**
		 * Layout the panel.
		 */
		@Override
		public void doLayout() {
			choice.setBounds(20, 10, 200, 30);
			playgame.setBounds(20, 40, 200, 30);
			viewstatus.setBounds(20, 70, 200, 30);
			viewscore.setBounds(20, 100, 200, 30);
			logout.setBounds(20, 130, 200, 30);
		}

		/**
		 * Get the game ID.
		 * @return The game ID.
		 */
		public int getGameId() {
			return mGameList.getGameDataByName(choice.getSelectedItem()).getId();
		}
	}

	/**
	 * Handle logout button clicks.
	 * @param evt Event object.
	 */
	public void logoutButton_actionPerformed(ActionEvent evt) {
		client.Main.quit();
	}

	/**
	 * Handle play game button clicks.
	 * @param evt Event object.
	 */
	public void playgameButton_actionPerformed(ActionEvent evt) {
		dispose();
		client.Main.playGame(center.getGameId());
	}

	/**
	 * Handle view status button clicks.
	 * @param evt Event object.
	 */
	public void viewstatusButton_actionPerformed(ActionEvent evt) {
		dispose();
		client.Main.showStats();
	}
	
	/**
	 * Handle view score button clicks.
	 * @param evt Event object.
	 */
	public void viewscoreButton_actionPerformed(ActionEvent evt) {
		dispose();
		client.Main.showHighScores(center.getGameId());
	}
}
