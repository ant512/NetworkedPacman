package pacman;

import java.awt.*;
import graphics.*;

/**
 * Ghost sprite.
 */
public class Ghost extends Sprite {

	// Constants
	final private static int START_BLOCK_X = 9;
	final private static int START_BLOCK_Y = 10;
	final private static int EDIBLE_FLASH_MOD = 10;
	final private static int MOVEMENT_SPEED_SLOW = 1;

	// Members
	private Animation mAnimEdible;
	private Animation mAnimEdibleFlash;
	private Animation mAnimEaten;
	private State mState;

	/**
	 * Enum defining all ghost colours.
	 */
	public enum GhostColour {
		RED,
		BLUE,
		PINK,
		ORANGE;
	}

	/**
	 * Enum defining all states a ghost can be in.
	 */
	private enum State {
		NORMAL,
		EDIBLE,
		EDIBLE_FLASHING,
		EATEN;
	}

	/**
	 * Constructor.
	 * @param name Name of the sprite.
	 * @param colour Colour of the ghost.
	 */
	public Ghost(String name, GhostColour colour) {
		super(START_BLOCK_X * Map.BLOCK_WIDTH, START_BLOCK_Y * Map.BLOCK_HEIGHT, Map.BLOCK_WIDTH, Map.BLOCK_HEIGHT, name, new Color(255, 255, 0));

		switch (colour) {
			case RED:
				initRedGhost();
				break;
			case BLUE:
				initBlueGhost();
				break;
			case PINK:
				initPinkGhost();
				break;
			case ORANGE:
				initOrangeGhost();
				break;
		}

		// Initialise ghost edible animation
		mAnimEdible = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimEdible.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_0), 0);
		mAnimEdible.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_1), 0);
		mAnimEdible.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_2), 0);
		mAnimEdible.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_3), 0);
		mAnimEdible.play();

		mAnimEdibleFlash = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimEdibleFlash.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_FLASH_0), 0);
		mAnimEdibleFlash.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_FLASH_1), 0);
		mAnimEdibleFlash.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_FLASH_2), 0);
		mAnimEdibleFlash.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EDIBLE_FLASH_3), 0);
		mAnimEdibleFlash.play();

		// Initialise ghost eaten animation
		mAnimEaten = new Animation(1, Animation.LoopType.NONE, 0);
		mAnimEaten.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EYES_U), 0);
		mAnimEaten.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EYES_D), 0);
		mAnimEaten.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EYES_L), 0);
		mAnimEaten.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_EYES_R), 0);

		// Set initial animation
		mAnimation = mAnimRight;

		mState = State.NORMAL;
	}

	/**
	 * Initialise the ghost as a red ghost.
	 */
	private void initRedGhost() {
		mAnimRight = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_RIGHT_0), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_RIGHT_1), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_RIGHT_2), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_RIGHT_3), 0);

		mAnimLeft = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_LEFT_0), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_LEFT_1), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_LEFT_2), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_LEFT_3), 0);

		mAnimUp = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_UP_0), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_UP_1), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_UP_2), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_UP_3), 0);

		mAnimDown = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_DOWN_0), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_DOWN_1), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_DOWN_2), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_RED_DOWN_3), 0);

		mAnimLeft.play();
		mAnimRight.play();
		mAnimUp.play();
		mAnimDown.play();

		mColour = new Color(255, 0, 0);
	}

	/**
	 * Initialise the ghost as a blue ghost.
	 */
	private void initBlueGhost() {
		mAnimRight = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_RIGHT_0), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_RIGHT_1), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_RIGHT_2), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_RIGHT_3), 0);

		mAnimLeft = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_LEFT_0), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_LEFT_1), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_LEFT_2), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_LEFT_3), 0);

		mAnimUp = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_UP_0), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_UP_1), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_UP_2), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_UP_3), 0);

		mAnimDown = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_DOWN_0), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_DOWN_1), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_DOWN_2), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_BLUE_DOWN_3), 0);

		mAnimLeft.play();
		mAnimRight.play();
		mAnimUp.play();
		mAnimDown.play();

		mColour = new Color(0, 252, 255);
	}

	/**
	 * Initialise the ghost as a pink ghost.
	 */
	private void initPinkGhost() {
		mAnimRight = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_RIGHT_0), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_RIGHT_1), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_RIGHT_2), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_RIGHT_3), 0);

		mAnimLeft = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_LEFT_0), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_LEFT_1), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_LEFT_2), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_LEFT_3), 0);

		mAnimUp = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_UP_0), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_UP_1), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_UP_2), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_UP_3), 0);

		mAnimDown = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_DOWN_0), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_DOWN_1), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_DOWN_2), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_PINK_DOWN_3), 0);

		mAnimLeft.play();
		mAnimRight.play();
		mAnimUp.play();
		mAnimDown.play();

		mColour = new Color(255, 122, 122);
	}

	/**
	 * Initialise the ghost as a orange ghost.
	 */
	private void initOrangeGhost() {
		mAnimRight = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_RIGHT_0), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_RIGHT_1), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_RIGHT_2), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_RIGHT_3), 0);

		mAnimLeft = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_LEFT_0), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_LEFT_1), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_LEFT_2), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_LEFT_3), 0);

		mAnimUp = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_UP_0), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_UP_1), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_UP_2), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_UP_3), 0);

		mAnimDown = new Animation(1, Animation.LoopType.LOOP, 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_DOWN_0), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_DOWN_1), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_DOWN_2), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.GHOST_ORANGE_DOWN_3), 0);

		mAnimLeft.play();
		mAnimRight.play();
		mAnimUp.play();
		mAnimDown.play();

		mColour = new Color(255, 115, 0);
	}

	/**
	 * Check for a collision with a solid block.  Allows ghosts to move through
	 * ghost walls.
	 * @param x X co-ord of the rect based on screen co-ords, not layout co-ords.
	 * @param y Y co-ord of the rect based on screen co-ords, not layout co-ords.
	 * @return True if a collision occurs.
	 */
	@Override
	protected boolean checkMapCollision(int x, int y) {
		int block;

		block = PacManGame.getGame().getMap().getBlock(x, y);
		if ((block >= Map.BLOCK_WALL) && (block != Map.BLOCK_GHOSTWALL)) return true;

		block = PacManGame.getGame().getMap().getBlock(x + mWidth - 1, y);
		if ((block >= Map.BLOCK_WALL) && (block != Map.BLOCK_GHOSTWALL)) return true;

		block = PacManGame.getGame().getMap().getBlock(x, y + mHeight - 1);
		if ((block >= Map.BLOCK_WALL) && (block != Map.BLOCK_GHOSTWALL)) return true;

		block = PacManGame.getGame().getMap().getBlock(x + mWidth -1, y + mHeight - 1);
		if ((block >= Map.BLOCK_WALL) && (block != Map.BLOCK_GHOSTWALL)) return true;

		return false;
	}

	/**
	 * Reset sprite to its initial location.
	 */
	@Override
	public void reset() {
		overridePosition(START_BLOCK_X * Map.BLOCK_WIDTH, START_BLOCK_Y * Map.BLOCK_HEIGHT, Direction.NONE, Direction.NONE);
		mAnimation = mAnimRight;
		mState = State.NORMAL;
		setFrozen(false);
	}

	/** Chooses correct animation based on current status and direction.
	 * @param direction Direction that sprite is moving in.
	 */
	public void switchAnimation(Direction direction) {
		switch (mState) {
			case NORMAL:
				switch (direction) {
					case RIGHT:
						mAnimation = mAnimRight;
						break;
					case LEFT:
						mAnimation = mAnimLeft;
						break;
					case UP:
						mAnimation = mAnimUp;
						break;
					case DOWN:
						mAnimation = mAnimDown;
						break;
				}
				break;
			case EDIBLE:
			case EDIBLE_FLASHING:
				break;
			case EATEN:
				
				// As there are no animations for an eaten ghost, each direction
				// is stored in a single frame in the eaten animation.  We just
				// need to jump to the right frame
				mAnimation = mAnimEaten;
				switch (direction) {
					case UP:
						mAnimation.goToFrame(0);
						break;
					case DOWN:
						mAnimation.goToFrame(1);
						break;
					case LEFT:
						mAnimation.goToFrame(2);
						break;
					case RIGHT:
						mAnimation.goToFrame(3);
						break;
				}

				// Ensure the animation shows a static frame
				mAnimation.stop();
				break;
		}
	}

	/**
	 * Extend base method so that it updates sprite animations.
	 * @param x The new x co-ord.
	 * @param y The new y co-ord.
	 * @param direction The new direction.
	 * @param bufferedDirection The new buffered direction.
	 */
	@Override
	public void overridePosition(int x, int y, Direction direction, Direction bufferedDirection) {
		super.overridePosition(x, y, direction, bufferedDirection);
		switchAnimation(bufferedDirection);
	}

	/**
	 * Extends setDirection to change animation, so ghost looks in the direction
	 * in which it will move next.
	 * @param direction The new direction for the sprite.
	 * @return True if the new direction was buffered; false if not.
	 */
	@Override
	public boolean setDirection(Direction direction) {

		// Store the requested direction for later processing
		if ((mBufferedDirection != direction) && (mDirection != direction)) {
			mBufferedDirection = direction;

			switchAnimation(direction);

			return true;
		}

		return false;
	}

	/**
	 * Make the ghost edible; called when PacMan collects a power pill.
	 */
	public void makeEdible() {
		if (mState == State.NORMAL) {
			mAnimation = mAnimEdible;
			mSpeed = MOVEMENT_SPEED_SLOW;
			mState = State.EDIBLE;
		}
	}

	/**
	 * Make the edible ghost flash; called when a powerpill is running out.
	 */
	public void makeFlash() {
		if (mState == State.EDIBLE) mState = State.EDIBLE_FLASHING;
	}

	/**
	 * Make the ghost eaten; called when PacMan eats the ghost.
	 */
	public void makeEaten() {
		if ((mState == State.EDIBLE) || (mState == State.EDIBLE_FLASHING)) {
			mSpeed = MOVEMENT_SPEED;
			mState = State.EATEN;

			// Swap animation
			if (mBufferedDirection != Direction.NONE) {
				switchAnimation(mBufferedDirection);
			} else if (mDirection != Direction.NONE) {
				switchAnimation(mDirection);
			} else {
				mAnimation = mAnimRight;
			}
		}
	}

	/**
	 * Make the ghost normal; called when an eaten ghost returns home or the
	 * powerpill timer runs out.
	 */
	public void makeNormal() {
	
		mSpeed = MOVEMENT_SPEED;
		mState = State.NORMAL;

		// Swap animation
		if (mBufferedDirection != Direction.NONE) {
			switchAnimation(mBufferedDirection);
		} else if (mDirection != Direction.NONE) {
			switchAnimation(mDirection);
		} else {
			mAnimation = mAnimRight;
		}
	}

	/**
	 * Check if the ghost is normal or not.
	 * @return True if the ghost is normal.
	 */
	public boolean isNormal() {
		return (mState == State.NORMAL);
	}

	/**
	 * Check if the ghost is edible or not.
	 * @return True if the ghost is edible.
	 */
	public boolean isEdible() {
		return (mState == State.EDIBLE) | (mState == State.EDIBLE_FLASHING);
	}

	/**
	 * Check if the ghost has been eaten or not.
	 * @return True if the ghost has been eaten.
	 */
	public boolean isEaten() {
		return (mState == State.EATEN);
	}

	/**
	 * Check if the ghost if flashing.
	 * @return True if the ghost is flashing.
	 */
	public boolean isFlashing() {
		return (mState == State.EDIBLE_FLASHING);
	}

	/**
	 * Extend base method with edible timer handling.
	 */
	@Override
	public void run() {

		super.run();

		switch (mState) {
			case NORMAL:
			case EDIBLE:
			case EDIBLE_FLASHING:
				break;
			case EATEN:

				// Check if the ghost is back home - reset state to normal if so
				int blockX = mX / Map.BLOCK_WIDTH;
				int blockY = mY / Map.BLOCK_HEIGHT;

				if ((blockX == START_BLOCK_X) && (blockY == START_BLOCK_Y)) {
					makeNormal();
				}
				break;
		}
	}

	/**
	 * Extend the draw method to support flashing when ghosts are nearing the
	 * end of an edible period.
	 * @param g Graphics object to draw to.
	 */
	@Override
	public void draw(Graphics g) {

		// Perform state-dependent operations
		switch (mState) {
			case NORMAL:
			case EDIBLE:
			case EATEN:

				// Draw animation frame
				g.drawImage(mAnimation.getCurrentBitmap(), mX, mY, null);
				break;
			case EDIBLE_FLASHING:

				// Flash the ghost
				if (PowerPillTimer.getTimer() % EDIBLE_FLASH_MOD < EDIBLE_FLASH_MOD >> 1) {
					mAnimation = mAnimEdibleFlash;
				} else {
					mAnimation = mAnimEdible;
				}
				g.drawImage(mAnimation.getCurrentBitmap(), mX, mY, null);
				break;
		}

		drawName(g);
	}
}
