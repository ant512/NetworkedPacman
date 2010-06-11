package util;

/**
 * Print debug information.
 */
public class Debug {

	// Constants
	final private static boolean ENABLED = false;

	/**
	 * Constructor - private to prevent creation.
	 */
	private Debug() { }

	/**
	 * Print the specified message.
	 * @param msg The message to print.
	 */
	public static void print(String msg) {
		if (ENABLED) {
			System.out.println(msg);
		}
	}
}
