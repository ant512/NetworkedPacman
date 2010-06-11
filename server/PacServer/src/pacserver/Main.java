package pacserver;

import java.net.*;
import java.io.*;

/**
 * Main program.
 */
public class Main {

	// Constants
	
	/** Port on which the server listens */
	final public static int PORT_NUMBER = 4444;
	
	/** Version of the server. */
	final public static String VERSION = "1.00";

	/**
	 * Main program loop - waits for and handles connecting clients.
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {
		printStartupText();
		runLobby();
	}

	/**
	 * Print startup information
	 */
	public static void printStartupText() {
		// Print startup info
		printUnderline("PacServer v" + VERSION);
		System.out.println();
		System.out.println("Server started.");
	}

	/**
	 * Create and run the lobby thread.
	 */
	public static void runLobby() {

		ServerSocket serverSocket = null;

		// Create lobby thread that will wait for client connections
		LobbyThread.init();

		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
		} catch (IOException e) {
			System.err.println("Could not listen on port " + PORT_NUMBER + ": " + e);
			System.exit(-1);
		}

		try {
			while (true) {

				// Wait for client connections and add them to the lobby
				LobbyThread.getLobbyThread().addClient(serverSocket.accept());
			}
		} catch (Exception e) {
			// Ignore exception - just continue closing program
		} finally {
			try {

				// Close the server socket
				serverSocket.close();
			} catch (IOException e) {
				// Ignore exception
			}

			// Stop the lobby thread
			LobbyThread.getLobbyThread().interrupt();
		}
	}

	/**
	 * Print the specified string to the console and underline it.
	 * @param text Text to print.
	 */
	private static void printUnderline(String text) {
		System.out.println(text);
		System.out.println(getString('-', text.length()));
	}

	/**
	 * Get a string of chars of the specified length.
	 * @param letter Char to fill the string with.
	 * @param length Length of the string to return.
	 * @return A string populated with the specified char of the specified
	 * length.
	 */
	private static String getString(char letter, int length) {
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < length; ++i) {
			output.append(letter);
		}

		return output.toString();
	}
}
