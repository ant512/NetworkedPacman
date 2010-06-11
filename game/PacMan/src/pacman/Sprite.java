package pacman;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import graphics.*;

/**
 * Sprite class.
 */
public abstract class Sprite {

	// Constants

	/** Default movement speed of a sprite */
	final protected static int MOVEMENT_SPEED = 2;

	/** Font used to print the sprite name */
	final protected static String FONT_NAME = "Sans-Serif";

	/** Size of the font used to print the sprite name */
	final protected static int FONT_SIZE = 16;

	/**
	 * Enum defining all possible sprite directions
	 */
	public enum Direction {
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	/** X co-ordinate of the sprite */
	protected int mX;

	/** Y co-ordinate of the sprite */
	protected int mY;

	/** Width of the sprite */
	protected int mWidth;

	/** Height of the sprite */
	protected int mHeight;

	/** Username of the player controlling the sprite */
	protected String mName;

	/** Colour of the text drawn to show the name of the sprite */
	protected Color mColour;

	/** Direction that the sprite is moving in */
	protected Direction mDirection;

	/** Direction that the sprite will move in next, as soon as that direction
	    becomes available */
	protected Direction mBufferedDirection;

	/** Speed at which the sprite is moving */
	protected int mSpeed;

	/** Current score accrued by the sprite */
	protected Score mScore;

	/** If frozen, the sprite cannot move */
	protected boolean mIsFrozen;

	/** Animation shown when sprite is moving right */
	protected Animation mAnimRight;

	/** Animation shown when sprite is moving left */
	protected Animation mAnimLeft;

	/** Animation shown when sprite is moving up */
	protected Animation mAnimUp;

	/** Animation shown when sprite is moving down */
	protected Animation mAnimDown;

	/** Pointer to the current animation */
	protected Animation mAnimation;
	
	/**
	 * Constructor.
	 * @param x Initial x co-ord.
	 * @param y Initial y co-ord.
	 * @param width Width of the sprite.
	 * @param height Height of the sprite.
	 * @param name Name of the sprite.
	 * @param colour Colour of the sprite.
	 */
	public Sprite(int x, int y, int width, int height, String name, Color colour) {
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
		mName = name;
		mColour = new Color(colour.getRGB());
		mDirection = Direction.NONE;
		mBufferedDirection = Direction.NONE;
		mSpeed = MOVEMENT_SPEED;
		mScore = new Score();
		mIsFrozen = false;
	}
	
	/**
	 * Set the direction that the sprite will move in.  Does not take effect
	 * until the sprite can move in that direction.  Until that time, the new
	 * direction is buffered.  The sprite remembers one new buffered direction
	 * at a time.
	 * @param direction The new direction for the sprite.
	 * @return True if the new direction was buffered; false if not.
	 */
	public boolean setDirection(Direction direction) {
		
		// Store the requested direction for later processing
		if ((mBufferedDirection != direction) && (mDirection != direction)) {
			mBufferedDirection = direction;
		
			return true;
		}

		return false;
	}

	/**
	 * Override the current co-ordinates and directions and set them to new
	 * values.  No collision checks are performed.
	 * @param x The new x co-ord.
	 * @param y The new y co-ord.
	 * @param direction The new direction.
	 * @param bufferedDirection The new buffered direction.
	 */
	public void overridePosition(int x, int y, Direction direction, Direction bufferedDirection) {
		mDirection = direction;
		mBufferedDirection = bufferedDirection;
		mX = x;
		mY = y;
	}
	
	/**
	 * Attempt to change to the buffered direction, if any.
	 */
	private void changeDirection() {
		if (mBufferedDirection != Direction.NONE) {
			switch (mBufferedDirection) {
				case UP:
					if (canIncreaseY(-mSpeed)) {
						mDirection = mBufferedDirection;
						mBufferedDirection = Direction.NONE;
					}
					break;
				case DOWN:
					if (canIncreaseY(mSpeed)) {
						mDirection = mBufferedDirection;
						mBufferedDirection = Direction.NONE;
					}
					break;
				case LEFT:
					if (canIncreaseX(-mSpeed)) {
						mDirection = mBufferedDirection;
						mBufferedDirection = Direction.NONE;
					}
					break;
				case RIGHT:
					if (canIncreaseX(mSpeed)) {
						mDirection = mBufferedDirection;
						mBufferedDirection = Direction.NONE;
					}
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Check if x co-ord can be increased as requested without a collision
	 * occuring.  Does not validate sprites jumping over entire walls.
	 * @param deltaX Amount to increase x by.
	 * @return True if x can be increased.
	 */
	private boolean canIncreaseX(int deltaX) {
		return !checkMapCollision(mX + deltaX, mY);
	}
	
	/**
	 * Check if y co-ord can be increased as requested without a collision
	 * occuring.  Does not validate sprites jumping over entire walls.
	 * @param deltaY Amount to increase y by.
	 * @return True if y can be increased.
	 */
	private boolean canIncreaseY(int deltaY) {
		return !checkMapCollision(mX, mY + deltaY);
	}
	
	/**
	 * Run all sprite logic for a single iteration of the main program loop.
	 */
	public void run() {

		if (!mIsFrozen) {

			// Attempt to switch to new buffered direction
			changeDirection();

			// Move in current direction
			switch (mDirection) {
				case UP:
					increaseY(-mSpeed);
					break;
				case DOWN:
					increaseY(mSpeed);
					break;
				case LEFT:
					increaseX(-mSpeed);
					break;
				case RIGHT:
					increaseX(mSpeed);
					break;
				case NONE:
					break;
			}
		}

		mAnimation.run();
	}

	/**
	 * Check for a collision with a solid block.
	 * @param x X co-ord of the rect based on screen co-ords, not layout co-ords.
	 * @param y Y co-ord of the rect based on screen co-ords, not layout co-ords.
	 * @return True if a collision occurs.
	 */
	protected boolean checkMapCollision(int x, int y) {
		if (PacManGame.getGame().getMap().getBlock(x, y) >= Map.BLOCK_WALL) return true;
		if (PacManGame.getGame().getMap().getBlock(x + mWidth - 1, y) >= Map.BLOCK_WALL) return true;
		if (PacManGame.getGame().getMap().getBlock(x, y + mHeight - 1) >= Map.BLOCK_WALL) return true;
		if (PacManGame.getGame().getMap().getBlock(x + mWidth -1, y + mHeight - 1) >= Map.BLOCK_WALL) return true;

		return false;
	}
	
	/**
	 * Increase x co-ord by deltaX; use negatives to decrease x
	 * @param deltaX Amount to increase x by
	 */
	protected boolean increaseX(int deltaX) {
		
		int newX = mX + deltaX;
		int oldX = mX;
		
		if (checkMapCollision(newX, mY)) {
			
			// Move to max X within the current block
			if (deltaX < 0) {
				mX = (mX / Map.BLOCK_WIDTH) * Map.BLOCK_WIDTH;
			} else if (deltaX > 0) {
				mX = (newX / Map.BLOCK_WIDTH) * Map.BLOCK_WIDTH;
			}
		} else {
			mX += deltaX;
		}

		// Wrap-around
		if (mX + mWidth < 0) {
			mX = Map.getWidth() - mSpeed;
		} else if (mX >= Map.getWidth()) {
			mX = mSpeed - mWidth;
		}

		return (oldX != mX);
	}
	
	/**
	 * Increase y co-ord by deltaY; use negatives to decrease y
	 * @param deltaY Amount to increase y by
	 */
	protected boolean increaseY(int deltaY) {
		
		int newY = mY + deltaY;
		int oldY = mY;
		
		if (checkMapCollision(mX, newY)) {

			
			
			// Move to max Y within the current block
			if (deltaY < 0) {
				mY = (mY / Map.BLOCK_HEIGHT) * Map.BLOCK_HEIGHT;
			} else if (deltaY > 0) {
				mY = (newY / Map.BLOCK_HEIGHT) * Map.BLOCK_HEIGHT;
			}
	
		} else {
			mY += deltaY;
		}

		return (oldY != mY);
	}

	/**
	 * Get the sprite's x co-ordinate.
	 * @return The sprite's x co-ordinate.
	 */
	public int getX() { return mX; }

	/**
	 * Get the sprite's y co-ordinate.
	 * @return The sprite's y co-ordinate.
	 */
	public int getY() { return mY; }

	/**
	 * Get the sprite's width.
	 * @return The sprite's width.
	 */
	public int getWidth() { return mWidth; }

	/**
	 * Get the sprite's height.
	 * @return The sprite's height.
	 */
	public int getHeight() { return mHeight; }

	/**
	 * Set the sprite's frozen state.
	 * @param frozen True to make the sprite frozen and immobile.
	 */
	public void setFrozen(boolean frozen) { mIsFrozen = frozen; }

	/**
	 * Get the sprite's name.
	 * @return The sprite's name.
	 */
	public String getName() { return mName; }

	/**
	 * Check if the sprite is frozen.
	 * @return True if the sprite is frozen.
	 */
	public boolean isFrozen() { return mIsFrozen; }

	/**
	 * Get the direction that the sprite is currently moving in.
	 * @return The direction that the sprite is currently moving in.
	 */
	public Direction getDirection() { return mDirection; }

	/**
	 * Get the direction that the sprite will be moving in when the direction
	 * becomes available.
	 * @return The future direction of the sprite as soon as the direction
	 * becomes available.
	 */
	public Direction getBufferedDirection() { return mBufferedDirection; }

	/**
	 * Get a pointer to the current animation.
	 * @return A pointer to the current animation.
	 */
	public Animation getAnimation() { return mAnimation; }

	/**
	 * Get a pointer to the sprite's color.
	 * @return A pointer to the sprite's color.
	 */
	public Color getColor() { return mColour; }
	
	/**
	 * Draw the sprite using the supplied graphics object
	 * @param g Graphics object to draw to.
	 */
	public void draw(Graphics g) {
		
		// Draw animation frame
		g.drawImage(mAnimation.getCurrentBitmap(), mX, mY, null);

		drawName(g);
	}

	/**
	 * Draw the sprite name above the sprite.
	 * @param g Graphics object to draw to.
	 */
	protected void drawName(Graphics g) {

		g.setColor(mColour);
		
		// Prepate to player name - set new font
		g.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));

		// Get width of the string when printed
		Graphics2D g2d = (Graphics2D)g;
		Rectangle2D rect = g.getFont().getStringBounds(mName, g2d.getFontRenderContext());
		int textX = mX - ((int)rect.getWidth() - mWidth) / 2;

		// Draw the player name
		g.drawString(mName, textX, mY - 2);
	}
	
	/**
	 * Check if the supplied rectangle collides with this sprite.
	 * @param x X co-ord of the rectangle.
	 * @param y Y co-ord of the rectangle.
	 * @param width Width of the rectangle.
	 * @param height Height of the rectangle.
	 * @return True if a collision occurs.
	 */
	public boolean checkCollision(int x, int y, int width, int height) {
		if (x + width <= getX()) return false;
		if (y + height <= getY()) return false;
		if (x >= getX() + getWidth()) return false;
		if (y >= getY() + getHeight()) return false;
		return true;
	}
	
	/**
	 * Check if the supplied sprite collides with this.
	 * @param sprite Sprite to check for collisions.
	 * @return True if the sprite collides with this.
	 */
	public boolean checkCollision(Sprite sprite) {
		return checkCollision(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}

	/**
	 * Reset sprite to its initial location.
	 */
	public abstract void reset();

	/**
	 * Get the score object.
	 * @return A pointer to the score object.
	 */
	public Score getScore() { return mScore; }

	/**
	 * Get the score.
	 * @return The score.
	 */
	public int getScoreValue() { return mScore.getScore(); }
}
