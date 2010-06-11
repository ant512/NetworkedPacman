package database;

/** database.HighScore
 * Stores data for one highscore.
 * Created: 11-Mar-2009
 * @author wob
 * @version 0.1
 */
public class HighScore {
    
    private int score; /** The highscore for a top player. */
    private String username; /** The username for a top player. */
    
    /**
     * Constructor for HighScore objects.
     * @param score An int representing the player's score.
     * @param username A String representing the player's username.
     */
    public HighScore(int score, String username) {
        this.score = score;
        this.username = username;
    }
    
	/**
	 * Get the score.
	 * @return The score.
	 */
    public int getScore() {
        return score;
    }
    
	/**
	 * Get the username.
	 * @return The username.
	 */
    public String getUsername() {
        return username;
    }

	/**
	 * Get a string representation of the object, formatted as:
	 * score,username.
	 * @return A string representation of the object.
	 */    
    @Override
    public String toString() {
        return score + "," + username;
    }
}
