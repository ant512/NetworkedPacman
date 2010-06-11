package client;

import comms.*;
import ui.*;

/**
 * Main class.
 */
public class Main {

	// Members
	private static PlayerData mPlayerData;
	private static int mPort;
	private static String mAddress;

	/**
	 * Main entry point.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			
			// If no command line arguments are passed, the client connects
			// to the default server.
			
			connectToServer("default");
			showLogin();
			
		} else if (args[0].equals("--saveserver") || args[0].equals("-ss") &&
				(args.length == 4)) {
			
			// If the user enters "--saveserver address port filename" as
			// command line arguments. A new xml file is created for that
			// server.
			
			saveServer(args);
			
		} else if (args[0].equals("--server") || args[0].equals("-s") &&
				(args.length == 2)) {
			
			// If the user enters "--server filename" as a command line argument
			// the specified custom server is loaded.
			
			String fileName = args[1];
			
			connectToServer(fileName);
			showLogin();
			
		} else {
			printUsage();
		}
	}

	/**
	 * Shows the login frame.
	 */
	public static void showLogin() {
		new Login();
	}

	/**
	 * Shows the registration frame.
	 */
	public static void showRegister() {
		new Register();
	}
	
	/**
	 * Shows the player stats frame.
	 */
	public static void showStats() { 
		new PlayerStatsFrame();
	}

	/**
	 * Log into the system.
	 * @param login Pointer to the login frame.
	 * @param username The username of the player.
	 * @param password The player's password.
	 */
	public static void login(Login login, String username, String password) {

		mPlayerData = null;

		if (!username.equals("") && (!password.equals(""))) {
			mPlayerData = Server.login(username, password);
		}

		if (mPlayerData != null) {
			login.dispose();

			showMenu();
		} else {
			login.showLoginError();
		}
	}

	/**
	 * Shows the main menu frame.
	 */
	public static void showMenu() {
		new Menu();
	}

	/**
	 * Shows the high score frame.
	 * @param gameId
	 */
	public static void showHighScores(int gameId) {
		new HighScoresFrame(gameId);
	}

	/**
	 * Register a user.
	 * @param register Pointer to the registration frame.
	 * @param username The player's new username.
	 * @param password The player's new password.
	 * @param confirmPassword Confirmation of the player's new password.  Should
	 * be the same password again.
	 */
	public static void register(Register register, String username, String password, String confirmPassword) {

		mPlayerData = null;
		String error = "";

		// Ensure passwords match
		if (password.equals(confirmPassword)) {
			if (!username.equals("") && !password.equals("") && !confirmPassword.equals("")) {
				mPlayerData = Server.register(username, password);

				if (mPlayerData != null) {
					register.dispose();

					new Menu();
					return;
				} else {
					error = "Username already in use.";
				}
			} else {
				error = "Please enter all information.";
			}
		} else {
			error = "Passwords do not match.";
		}

		register.showRegistrationError(error);
	}

	/**
	 * Quit the system and log out.
	 */
	public static void quit() {
		Server.logout();
		System.exit(1);
	}

	/**
	 * Play the specified game.
	 * @param gameId The ID of the game to play.
	 */
	public static void playGame(int gameId) {

		Server.joinGameSession(gameId);

		switch (gameId) {
			case 1:
			case 2:
			case 3:
				pacman.PacManGame.init();
				break;
		}
	}
	
	/**
	 * Connect to a server.  Method should be passed the name of an XML file
	 * containing the address and port that the server is located on.
	 * @param xmlFileName Name of a file containing server name and port.
	 */
	private static void connectToServer(String xmlFileName) {
		String[] server = ServerDetails.getServerDetails(xmlFileName);
		mAddress = server[0];
		mPort = Integer.parseInt(server[1]);
		
		// Listen for server crashes and kill the client if they occur
		Server.addServerListener(new ClientServerListener());

		if (!Server.connect(mAddress, mPort)) {
			util.Debug.print("Cannot connect to server!");
			System.exit(1);
		}
	}
	
	/**
	 * Save server details to an XML file.
	 * @param args Command line arguments with information about server.
	 */
	private static void saveServer(String args[]) {
		String address = args[1];
		String port = args[2];
		String fileName = args[3];
		ServerDetails.saveServerToFile(address, port, fileName);
	}
	
	/**
	 * Print the command line usage of the program.
	 */
	private static void printUsage() {
		System.out.println("Usage: " +
				"\nPacMan.jar [-ss | --saveserver] ADDRESS PORT NAME" +
				"\nPacMan.jar [-s | -server] NAME" +
				"\nPacMan.jar");
	}
}
