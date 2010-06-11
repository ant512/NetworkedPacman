package database;

import java.sql.*;

/**
 * Database abstraction layer.  This is currently just a dummy class that
 * simulates the behaviour of the abstraction layer by returning a set of
 * expected values.  The methods need to be replaced with actual database
 * interactions.
 */
public class Database {

	/**
	 * Constructor.  Private to prevent creation.
	 */
	private Database() {
	}

	/**
	 * Get a list of all games in the database.
	 * @return A list of all games in the database.
	 */
	public static GameList getGameList() {

		GameList data = new GameList();

		data.add(new GameData(1, "Pac Man 2P", 2));
		data.add(new GameData(2, "Pac Man 4P", 4));
		data.add(new GameData(3, "Pac Man 6P", 6));
		data.add(new GameData(4, "Pac Man 8P", 8));

		return data;
	}

	/**
	 * Get a player's data by username.
	 * @param username The username of the player to retrieve.
	 * @return The player's data.
	 */
	public static PlayerData getPlayer(String username) {
		return new PlayerData(1, username, username, new Date(System.currentTimeMillis()));
	}

	/**
	 * Create a new entry in the game history table.  Returns a GameHistoryData
	 * object representing the new row.
	 * @return A GameHistoryData object representing the new row.
	 */
	private static GameHistoryData createGameHistory(int gameId, int winnerId, java.sql.Date startDate, long duration) {
		return new GameHistoryData(1, gameId, winnerId, startDate, duration);
	}

	/**
	 * Create a new entry in the game participants table.
	 * @param playerId ID of the player.
	 * @param gameHistoryId ID of the related game history entry.
	 * @param score The player's score.
	 * @param disconnected True if the player disconnected during the game.
	 */
	public static void createParticipant(int playerId, int gameHistoryId, int score, boolean disconnected) {
	}

	/**
	 * Get the rank name for the specified integer.
	 * @param rank The rank to retrieve the name for.
	 * @return The name of the specified rank.
	 */
	public static String getRank(int rank) {
		if (rank < 10) {
			return "Newbie";
		}
		if (rank < 20) {
			return "Beginner";
		}
		if (rank < 30) {
			return "Amateur";
		}
		if (rank < 40) {
			return "Professional";
		}
		if (rank < 50) {
			return "Expert";
		}
		if (rank < 60) {
			return "Junkie";
		}
		return "Nutter";
	}

	/**
	 * Create a player from username and password.
	 * @param username The username of a new player.
	 * @param password The password of a new player.
	 * @return The player's data.
	 */
	public static PlayerData register(String username, String password) {
		return new PlayerData(3, username, password, new Date(System.currentTimeMillis()));
	}
	
	/**
	 * Get a player's favourite game by username.
	 * @param playerId The ID of the player to retrieve.
	 * @return The player's stats.
	 */
	public static String getPlayerFavouriteGame(int playerId) {
		return "Pac Man";
	}
	
	/**
	 * Get the date that the specified user played last.
	 * @param playerId The ID of the player.
	 * @return The date of the last game played.
	 */
	public static String getPlayerLastGamePlayedDate(int playerId) {
		return "2008-01-01";
	}
	
	/**
	 * Get the name of the game that the specified user played last.
	 * @param playerId The ID of the player.
	 * @return The name of the last game played.
	 */
	public static String getPlayerLastGamePlayedName(int playerId) {
		return "Pac Man";
	}

	
	/**
	 * Get the number of times that a user has disconnected.
	 * @param playerId The ID of the player.
	 * @return The number of times the player has disconnected.
	 */
	public static int getPlayerDisconnects(int playerId) {
		return 0;
	}
	
	/**
	 * Get the number of times that a user has won.
	 * @param playerId The ID of the player.
	 * @return The number of times the player has won.
	 */
	public static int getPlayerGamesWon(int playerId) {
		return 0;
	}
	
	/**
	 * Get the number of times that a user has played games.
	 * @param playerId The ID of the player.
	 * @return The number of times the player has played a game.
	 */
	public static int getPlayerGamesPlayed(int playerId) {
		return 0;
	}

	/**
	 * Get the time a player has spent playing.
	 * @param playerId The ID of the player.
	 * @return The time a player has spent playing.
	 */
	public static long getPlayerGameDuration(int playerId) {
		return 0;
	}
	
	/**
	 * Get the number of times that a game has been played.
	 * @param gameId The ID of the game.
	 * @return The number of times a game has been played.
	 */
	public static int getGamesPlayed(int gameId) {
		return 0;
	}
	
	/**
	 * Get the stats for the specified game ID.
	 * @param gameId The ID of the game to retrieve stats for.
	 * @return The stats for the specified game.
	 */
	public static GameStats getGameStats(int gameId) {
		long totalDuration = getGameDuration(gameId);
		int timesPlayed = getGamesPlayed(gameId);
		
		return new GameStats(gameId, totalDuration, timesPlayed);
	}	
	
	/**
	 * Get the total time that a game has been played.
	 * @param gameId The ID of the game.
	 * @return The amount of time a game has been played.
	 */
	public static long getGameDuration(int gameId) {
		return 0;
	}
	
	/**
	 * Get a player's stats by username.
	 * @param username The username of the player to retrieve.
	 * @return The player's stats.
	 */
	public static PlayerStats getPlayerStats(String username) {

		return new PlayerStats(username, "Pac Man", "Pac Man", "10 August 2008",
						12, 2, 3, "16000", "Beginner");
	}
        
	/**
	 * Save the outcome of a game to the database.
	 * @param gameId The ID of the game.
	 * @param winnerId The ID of the game winner (player ID).
	 * @param startDate The date on which the game started.
	 * @param duration The duration of the game.
	 * @param resultList The results to save.
	 */
	public static void saveGameResults(int gameId, int winnerId, long startDate, long duration, GameResultList resultList) {
	}

	/**
	 * Get a list of HighScores by game ID.
	 * @param gameId The ID number of a game.
	 * @return A HighScoreData object containing the highscores for that game.
	 */
    public static HighScoreData getHighScores(int gameId) {

		HighScoreData scores = new HighScoreData(gameId);
		scores.add(new HighScore(750, "Bob"));
		scores.add(new HighScore(740, "Tom"));
		scores.add(new HighScore(700, "Bill"));
		scores.add(new HighScore(650, "Dave"));
		scores.add(new HighScore(625, "Harry"));
		scores.add(new HighScore(600, "Larry"));
		scores.add(new HighScore(575, "Barry"));
		scores.add(new HighScore(550, "Gary"));
		scores.add(new HighScore(500, "Terry"));
		scores.add(new HighScore(450, "Jerry"));

		return scores;
    }
}
            
