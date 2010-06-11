package pacman;

import java.awt.*;

/**
 * Class that draws the players' scores to the screen.
 */
public class ScoreDisplay {

	// Constants
	final private static Color COLOR_BACKGROUND = new Color(0, 0, 0);

	// Members
	private int mX;
	private int mY;
	private int mWidth;
	private int mHeight;

	/**
	 * Constructor.
	 */
	public ScoreDisplay() {
		mX = Map.getWidth();
		mY = 0;
		mHeight = Map.getHeight();
		mWidth = 300;
	}

	/**
	 * Draw the display.
	 * @param g The object to draw to.
	 */
	public void draw(Graphics g) {

		// Background
		g.setColor(COLOR_BACKGROUND);
		g.fillRect(mX, mY, mWidth, mHeight);

		// Title
		int fontHeight = g.getFontMetrics().getHeight();
		int deltaHeight = fontHeight > Map.BLOCK_HEIGHT ? fontHeight : Map.BLOCK_HEIGHT;
		int nameX = mX + (Map.BLOCK_WIDTH << 1);
		int nameY = mY + fontHeight;
		int spriteX = mX + (Map.BLOCK_WIDTH >> 1);
		int spriteY = mY + (fontHeight << 1) + ((fontHeight - Map.BLOCK_HEIGHT) >> 1);

		g.setColor(new Color(255, 255, 255));
		g.drawString("Score", spriteX, nameY);

		// Player names, scores and sprites
		nameY += (fontHeight << 1);

		String nameScore;

		for (int i = 0; i < PacManGame.getGame().getPlayerList().size(); ++i) {
			Sprite sprite = PacManGame.getGame().getPlayerList().get(i).getSprite();

			// Name and score
			nameScore = sprite.getName() + ": " + sprite.getScoreValue();

			g.setColor(sprite.getColor());
			g.drawString(nameScore, nameX, nameY);

			// Sprite
			g.drawImage(sprite.getAnimation().getCurrentBitmap(), spriteX, spriteY, null);

			nameY += deltaHeight;
			spriteY += deltaHeight;
		}

		// Lives
		g.setColor(new Color(255, 255, 255));
		nameY += deltaHeight;
		g.drawString("Lives", spriteX, nameY);
		
		// Find pacman
		Sprite pacSprite = PacManGame.getGame().getPlayerList().get(0).getSprite();
		
		if (pacSprite.getClass() == PacMan.class) {
			PacMan pacman = (PacMan)pacSprite;
			
			// Draw lives
			spriteY += deltaHeight * 3;

			for (int i = 0; i < pacman.getLives(); ++i) {
				g.drawImage(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_RIGHT_3), spriteX, spriteY, null);
				spriteX += Map.BLOCK_WIDTH;
			}
		}
	}
}
