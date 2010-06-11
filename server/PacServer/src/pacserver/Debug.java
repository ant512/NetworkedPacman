package pacserver;

/**
 * Print debug information.
 */
public class Debug {

	/** Switch to false to disable debug messages */
	final private static boolean ENABLED = false;

	/**
	 * Constructor.  Private to prevent creation.
	 */
	private Debug() { }

	/**
	 * Print a message.
	 * @param msg The message to print.
	 */
	public static void print(String msg) {
		if (ENABLED) {
			System.out.println(msg);
		}
	}
}
