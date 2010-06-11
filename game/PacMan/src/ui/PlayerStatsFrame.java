package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import comms.*;

/**
 * Frame showing player statistics.
 */
public class PlayerStatsFrame extends ClientFrame {
	
	// Members
	private JButton okButton; // Variable to represent a button at the bottom of the screen. Will take the user back to the menu interface.
	private JPanel bottomPanel;
	private JPanel topPanel;
	private JLabel frameLabel; // Will be shown at the top of the frame.
	private DialogPanel dialogPanel; // Will be used to implement all other labels and textAreas

	/**
	 * Constructor,
	 */
	public PlayerStatsFrame() {

		//Frame title
		super("Player Statistics");

		// Setting the button in the bottom panel
		okButton = new JButton("Ok");
		bottomPanel = new JPanel();
		
		//Adding 'ok' button to the panel at the bottom of the HighScoresFrame
		bottomPanel.add(okButton);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});

		// Setting the gameLabel and textArea
		frameLabel = new JLabel("Player Statistics");

		// Creating new JPanel object and adding the label/text area to it.
		topPanel = new JPanel();
		topPanel.add(frameLabel);

		// Setting the top and bottom panels to have borders.
		bottomPanel.setBorder(BorderFactory.createEtchedBorder());
		topPanel.setBorder(BorderFactory.createEtchedBorder());
		// Creating a new DialogPanel object and setting it to the center of the frame.
		dialogPanel = new DialogPanel();
		// Setting the panel layout using BorderLayout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(dialogPanel, BorderLayout.CENTER);
		// Setting the size of the main frame and setting it to visible
		setSize(340, 300);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Class DialogPanel deals with the main central panel of the
	 * PlayerStatsFrame.
	 */
	private class DialogPanel extends JPanel {

		// Declaring the JLabels and JTextAreas to be used
		JLabel playerNameLabel, favouriteGameLabel, lastGameNameLabel,
				lastGameDateLabel, timesPlayedLabel, durationPlayedLabel,
				disconnectsLabel, gamesWonLabel, rankLabel,
				playerNameDataLabel, favouriteGameDataLabel,
				lastGameNameDataLabel, lastGameDateDataLabel,
				timesPlayedDataLabel, durationPlayedDataLabel,
				disconnectsDataLabel, gamesWonDataLabel, rankDataLabel;

		/**
		 * Constructor.
		 */
		public DialogPanel() {

			// Create static labels
			playerNameLabel = new JLabel("Player Name");
			favouriteGameLabel = new JLabel("Favourite Game");
			lastGameNameLabel = new JLabel("Last Game Played");
			lastGameDateLabel = new JLabel("Last Time Played:");
			timesPlayedLabel = new JLabel("Total Games Played:");
			durationPlayedLabel = new JLabel("Total Game Time:");
			disconnectsLabel = new JLabel("Number of Disconnects:");
			gamesWonLabel = new JLabel("Number of Wins:");
			rankLabel = new JLabel("Rank:");
			
			// Fetch data
			PlayerStats stats = Server.getPlayerStats();
			
			// Bind data
			playerNameDataLabel = new JLabel(stats.getUsername());
			favouriteGameDataLabel = new JLabel(stats.getFavouriteGame());
			lastGameNameDataLabel = new JLabel(stats.getLastGamePlayed());
			lastGameDateDataLabel = new JLabel(stats.getLastGamePlayedDate());
			timesPlayedDataLabel = new JLabel(String.valueOf(stats.getNumberOfGamesPlayed()));
			durationPlayedDataLabel = new JLabel(stats.getTotalTimePlayedString());
			disconnectsDataLabel = new JLabel(String.valueOf(stats.getNumberOfDisconnects()));
			gamesWonDataLabel = new JLabel(String.valueOf(stats.getNumberOfGamesWon()));
			rankDataLabel = new JLabel(stats.getRanking());
			
			add(playerNameLabel);
			add(favouriteGameLabel);
			add(lastGameNameLabel);
			add(lastGameDateLabel);
			add(timesPlayedLabel);
			add(durationPlayedLabel);
			add(disconnectsLabel);
			add(gamesWonLabel);
			add(rankLabel);
			add(playerNameDataLabel);
			add(favouriteGameDataLabel);
			add(lastGameNameDataLabel);
			add(lastGameDateDataLabel);
			add(timesPlayedDataLabel);
			add(durationPlayedDataLabel);
			add(disconnectsDataLabel);
			add(gamesWonDataLabel);
			add(rankDataLabel);
		}

		/**
		 * Method to set the layout of the labels and text areas within the
		 * dialog panel
		 */
		@Override
		public void doLayout() {
			playerNameLabel.setBounds(15, 10, 150, 20);
			favouriteGameLabel.setBounds(15, 30, 150, 20);
			lastGameNameLabel.setBounds(15, 50, 150, 20);
			lastGameDateLabel.setBounds(15, 70, 150, 20);
			timesPlayedLabel.setBounds(15, 90, 150, 20);
			durationPlayedLabel.setBounds(15, 110, 150, 20);
			disconnectsLabel.setBounds(15, 130, 150, 20);
			gamesWonLabel.setBounds(15, 150, 150, 20);
			rankLabel.setBounds(15, 170, 150, 20);
			
			playerNameDataLabel.setBounds(180, 10, 150, 20);
			favouriteGameDataLabel.setBounds(180, 30, 150, 20);
			lastGameNameDataLabel.setBounds(180, 50, 150, 20);
			lastGameDateDataLabel.setBounds(180, 70, 150, 20);
			timesPlayedDataLabel.setBounds(180, 90, 150, 20);
			durationPlayedDataLabel.setBounds(180, 110, 150, 20);
			disconnectsDataLabel.setBounds(180, 130, 150, 20);
			gamesWonDataLabel.setBounds(180, 150, 150, 20);
			rankDataLabel.setBounds(180, 170, 150, 20);
		}
	}

	/**
	 * Handle OK button presses.
	 * @param evt Event object.
	 */
	public void okButton_actionPerformed(ActionEvent evt) {
		dispose();
		client.Main.showMenu();
	}
}