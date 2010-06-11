package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import comms.*;

/**
 * High Scores Frame which is accessed from the menu interface and allows the user
 * to read game scoring information along with game statistics.
 * 
 * @author vxp685
 */
public class HighScoresFrame extends ClientFrame {
	// Declaring buttons, panels and text areas to be used in HighScoresFrame

	private JButton okButton; // Variable to represent a button at the bottom of the screen. Will take the user back to the menu interface.
	private JPanel bottomPanel;
	private JPanel topPanel;
	private JLabel gameLabel; // Will be shown at the top of the HighScoresFrame.
	private JTextArea gameTextArea; // Will store the name of the game chosen in the menu interface
	private DialogPanel dialogPanel; // Will be used to implement all other labels and textAreas

	/**
	 * Constructor,
	 * @param gameId The ID of the game for which to retrieve high scores.
	 */
	public HighScoresFrame(int gameId) {

		//Frame title
		super("High Scores Information");

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
		gameLabel = new JLabel("Game");
		gameTextArea = new JTextArea();
		gameTextArea.setBounds(210, 5, 90, 30);

		// Creating new JPanel object and adding the label/text area to it.
		topPanel = new JPanel();
		topPanel.add(gameLabel);
		topPanel.add(gameTextArea);


		// Setting the top and bottom panels to have borders.
		bottomPanel.setBorder(BorderFactory.createEtchedBorder());
		topPanel.setBorder(BorderFactory.createEtchedBorder());
		// Creating a new DialogPanel object and setting it to the center of the frame.
		dialogPanel = new DialogPanel(gameId);
		// Setting the panel layout using BorderLayout
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(dialogPanel, BorderLayout.CENTER);
		// Setting the size of the main frame and setting it to visible
		setSize(260, 500);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Class DialogPanel deals with the main central panel of the
	 * HighScoresFrame.
	 */
	private class DialogPanel extends JPanel {

		// Declaring the JLabels and JTextAreas to be used
		JLabel playerNameLabel, scoreLabel, gameStatLabel, timesPlayedLabel, totalPlayedLabel;
		JTextArea playerTextArea, scoreTextArea, timesplayedTextArea, totalPlayedTextArea;

		/**
		 * Constructor.
		 * @param gameId ID of the game to fetch scores for.
		 */
		public DialogPanel(int gameId) {

			//Creating new JLabel objects and setting their appearance
			playerNameLabel = new JLabel("Player Name");
			scoreLabel = new JLabel("Score");
			gameStatLabel = new JLabel("Game Statistics");
			timesPlayedLabel = new JLabel("No of times played:");
			totalPlayedLabel = new JLabel("Total time played:");

			// Creating new JTextArea objects (JDBC methods to interact with the database)
			playerTextArea = new JTextArea();
			scoreTextArea = new JTextArea();
			timesplayedTextArea = new JTextArea();
			totalPlayedTextArea = new JTextArea();

			// Adding the new labels and text areas to the dialog panel
			add(playerNameLabel);
			add(scoreLabel);
			add(gameStatLabel);
			add(timesPlayedLabel);
			add(totalPlayedLabel);
			add(playerTextArea);
			add(scoreTextArea);
			add(timesplayedTextArea);
			add(totalPlayedTextArea);

			// Fetch high scores
			HighScoreData scores = Server.getHighScoreData(gameId);

			// Bind high scores
			for (HighScore score : scores) {
				playerTextArea.setText(playerTextArea.getText() + score.getUsername() + "\n");
				scoreTextArea.setText(scoreTextArea.getText() + score.getScore() + "\n");
			}

			// Fetch game stats
			GameStats stats = Server.getGameStats(gameId);
			
			// Bind game stats
			timesplayedTextArea.setText(String.valueOf(stats.getTimesPlayed()));
			totalPlayedTextArea.setText(stats.getTotalDurationString());
		}

		/**
		 * Method to set the layout of the labels and text areas within the
		 * dialog panel
		 */
		@Override
		public void doLayout() {
			playerNameLabel.setBounds(15, 10, 90, 30);
			scoreLabel.setBounds(180, 10, 90, 30);
			gameStatLabel.setBounds(70, 240, 100, 30);
			timesPlayedLabel.setBounds(10, 270, 125, 30);
			totalPlayedLabel.setBounds(10, 320, 120, 30);
			playerTextArea.setBounds(10, 55, 90, 160);
			scoreTextArea.setBounds(150, 55, 90, 160);
			timesplayedTextArea.setBounds(150, 270, 90, 30);
			totalPlayedTextArea.setBounds(150, 320, 90, 30);

			playerTextArea.setEditable(false);
			scoreTextArea.setEditable(false);
			totalPlayedTextArea.setEditable(false);
			timesplayedTextArea.setEditable(false);
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