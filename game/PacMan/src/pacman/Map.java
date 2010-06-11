package pacman;

import java.awt.*;

/**
 * Map class.  Stores the layout of the game environment.
 */
public class Map {

	/** No block */
	final static public int BLOCK_NONE = 0;
	
	/** Block is a standard pill */
	final static public int BLOCK_PILL = 1;
	
	/** Block is empty */
	final static public int BLOCK_NOPILL = 2;
	
	/** Block is a power pill */
	final static public int BLOCK_POWERPILL = 3;
	
	/** Block is a wall */
	final static public int BLOCK_WALL = 5;
	
	/** Block is a wall but can be traversed by ghosts */
	final static public int BLOCK_GHOSTWALL = 7;

	/** Width of a block */
	final static public int BLOCK_WIDTH = 16;
	
	/** Height of a block */
	final static public int BLOCK_HEIGHT = 16;

	/** Width of a standard pill */
	final static public int PILL_WIDTH = 4;
	
	/** Height of a standard pill */
	final static public int PILL_HEIGHT = 4;
	
	/** Width of a power pill */
	final static public int POWERPILL_WIDTH = 8;
	
	/** Height of a power pill */
	final static public int POWERPILL_HEIGHT = 8;

	/** Background colour */
	final static public Color COLOR_BACKGROUND = new Color(0, 0, 0);
	
	/** Pill colour */
	final static public Color COLOR_PILL = new Color(255, 255, 255);
	
	final static private int[][] LAYOUT = {
				{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5},
				{5,3,1,1,1,1,1,1,1,5,1,1,1,1,1,1,1,3,5},
				{5,1,5,5,1,5,5,5,1,5,1,5,5,5,1,5,5,1,5},
				{5,1,5,5,1,5,5,5,1,5,1,5,5,5,1,5,5,1,5},
				{5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5},
				{5,1,5,5,1,5,1,5,5,5,5,5,1,5,1,5,5,1,5},
				{5,1,1,1,1,5,1,1,1,5,1,1,1,5,1,1,1,1,5},
				{5,5,5,5,1,5,5,5,2,5,2,5,5,5,1,5,5,5,5},
				{2,2,2,5,1,5,2,2,2,2,2,2,2,5,1,5,2,2,2},
				{5,5,5,5,1,5,2,5,5,7,5,5,2,5,1,5,5,5,5},
				{2,2,2,2,1,2,2,5,2,2,2,5,2,2,1,2,2,2,2},
				{5,5,5,5,1,5,2,5,5,5,5,5,2,5,1,5,5,5,5},
				{2,2,2,5,1,5,2,2,2,2,2,2,2,5,1,5,2,2,2},
				{5,5,5,5,1,5,2,5,5,5,5,5,2,5,1,5,5,5,5},
				{5,1,1,1,1,1,1,1,1,5,1,1,1,1,1,1,1,1,5},
				{5,1,5,5,1,5,5,5,1,5,1,5,5,5,1,5,5,1,5},
				{5,3,1,5,1,1,1,1,1,1,1,1,1,1,1,5,1,3,5},
				{5,5,1,5,1,5,1,5,5,5,5,5,1,5,1,5,1,5,5},
				{5,1,1,1,1,5,1,1,1,5,1,1,1,5,1,1,1,1,5},
				{5,1,5,5,5,5,5,5,1,5,1,5,5,5,5,5,5,1,5},
				{5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5},
				{5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5}};
	
	// Members
	private int mLayout[][];
	private Image mBackdropImage;
	private Image mImage;
	private int mPillCount;
	
	/**
	 * Constructor.
	 */
	public Map(Image image) {
		
		// Copy the default layout into a new layout
		mLayout = getLayoutCopy();

		// Load the backdrop
		mBackdropImage = BitmapServer.getBitmap(BitmapServer.Bitmap.BACKDROP);

		mPillCount = getTotalPillCount();

		mImage = image;
		drawMap(mImage.getGraphics());
	}

	/**
	 * Get a copy of the layout.
	 * @return A copy of the layout.
	 */
	private int[][] getLayoutCopy() {

		int[][] copy = new int[LAYOUT.length][LAYOUT[0].length];
		
		for (int i = 0; i < LAYOUT.length; ++i) {
			for (int j = 0; j < LAYOUT[i].length; j++) {
				copy[i][j] = LAYOUT[i][j];
			}
		}

		return copy;
	}
	
	/**
	 * Get the block at the specified screen co-ordinates (not layout co-ords).
	 * @param x X co-ord of the block as it appears on screen.
	 * @param y Y co-ord of the block as it appears on screen.
	 * @return Integer of the block at the specified co-ordinates.
	 */
	public int getBlock(int x, int y) {
		int mapX = x / BLOCK_WIDTH;
		int mapY = y / BLOCK_HEIGHT;
		
		// Bounds checks
		if (mapX < 0) return BLOCK_NONE;
		if (mapX >= mLayout[0].length) return BLOCK_NONE;
		if (mapY < 0) return BLOCK_NONE;
		if (mapY >= mLayout.length) return BLOCK_NONE;

		return mLayout[mapY][mapX];
	}

	/**
	 * Clear the block at the specified screen co-ords to have no pill.
	 * @param x X co-ord of the block as it appears on screen.
	 * @param y Y co-ord of the block as it appears on screen.
	 * @return True if the block was cleared.
	 */
	public boolean clearBlock(int x, int y) {
		int mapX = x / BLOCK_WIDTH;
		int mapY = y / BLOCK_HEIGHT;

		// Bounds checks
		if (mapX < 0) return false;
		if (mapX >= mLayout[0].length) return false;
		if (mapY < 0) return false;
		if (mapY >= mLayout.length) return false;

		if (mLayout[mapY][mapX] != BLOCK_NOPILL) {
			if ((mLayout[mapY][mapX] == BLOCK_PILL) || (mLayout[mapY][mapX] == BLOCK_POWERPILL)) {
				mPillCount--;
			}
			
			mLayout[mapY][mapX] = BLOCK_NOPILL;

			// Erase block from image
			Graphics g = mImage.getGraphics();
			g.setColor(COLOR_BACKGROUND);
			g.fillRect(mapX * BLOCK_WIDTH, mapY * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);

			return true;
		}

		return false;
	}

	/**
	 * Draw the map.
	 * @param g The graphics object to draw to.
	 */
	public void draw(Graphics g) {
		g.drawImage(mImage, 0, 0, null);

	}

	/**
	 * Draw the map to he graphics object.
	 * @param g Graphics object to draw to.
	 */
	private void drawMap(Graphics g) {

		// Draw backdrop image
		g.drawImage(mBackdropImage, 0, 0, null);

		for (int i = 0; i < mLayout.length; ++i) {
			for (int j = 0; j < mLayout[i].length; ++j) {
				switch (mLayout[i][j]) {
					case BLOCK_PILL:

						// Draw pill
						g.setColor(COLOR_PILL);
						g.fillRect(j * BLOCK_WIDTH + ((BLOCK_WIDTH - PILL_WIDTH) / 2), i * BLOCK_HEIGHT + ((BLOCK_HEIGHT - PILL_HEIGHT) / 2), PILL_WIDTH, PILL_HEIGHT);
						break;
					case BLOCK_POWERPILL:

						// Draw pill
						g.setColor(COLOR_PILL);
						g.fillOval(j * BLOCK_WIDTH + ((BLOCK_WIDTH - POWERPILL_WIDTH) / 2), i * BLOCK_HEIGHT + ((BLOCK_HEIGHT - POWERPILL_HEIGHT) / 2), POWERPILL_WIDTH, POWERPILL_HEIGHT);
						break;
				}
			}
		}
	}
	
	/**
	 * Get the width of the map in pixels.
	 * @return The width of the map in pixels.
	 */
	public static int getWidth() { return LAYOUT[0].length * BLOCK_WIDTH; }

	/**
	 * Get the height of the map in pixels.
	 * @return The height of the map in pixels.
	 */
	public static int getHeight() { return LAYOUT.length * BLOCK_HEIGHT; }

	/**
	 * Get the total number of pills in the default layout.
	 * @return The total number of pills in the default layout.
	 */
	private static int getTotalPillCount() {

		int count = 0;

		for (int i = 0; i < LAYOUT.length; ++i) {
			for (int j = 0; j < LAYOUT[i].length; ++j) {
				if ((LAYOUT[i][j] == BLOCK_PILL) || (LAYOUT[i][j] == BLOCK_POWERPILL)) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Get the number of uncollected pills.
	 * @return The number of uncollected pills.
	 */
	public int getPillCount() { return mPillCount; }
}
