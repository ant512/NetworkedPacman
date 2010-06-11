package pacman;

import java.awt.*;
import graphics.*;

/**
 * PacMan sprite.
 */
public class PacMan extends Sprite {

	// Constants
	final private static int START_BLOCK_X = 9;
	final private static int START_BLOCK_Y = 12;
	final private static int MAX_LIVES = 3;

	// Members
	private int mLives;
	private Animation mAnimDead;
	private State mState;

	/**
	 * Enum listing all states PacMan can be in.
	 */
	public enum State {
		NORMAL,
		DEAD;
	}

	/**
	 * Constructor.
	 * @param name Name of the sprite.
	 */
	public PacMan(String name) {
		super(START_BLOCK_X * Map.BLOCK_WIDTH, START_BLOCK_Y * Map.BLOCK_HEIGHT, Map.BLOCK_WIDTH, Map.BLOCK_HEIGHT, name, new Color(255, 255, 0));

		// Set up animations
		mAnimRight = new Animation(1, Animation.LoopType.PING_PONG, 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_RIGHT_3), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_RIGHT_2), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_RIGHT_1), 0);
		mAnimRight.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN), 0);

		mAnimLeft = new Animation(1, Animation.LoopType.PING_PONG, 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_LEFT_3), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_LEFT_2), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_LEFT_1), 0);
		mAnimLeft.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN), 0);

		mAnimUp = new Animation(1, Animation.LoopType.PING_PONG, 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_UP_3), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_UP_2), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_UP_1), 0);
		mAnimUp.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN), 0);

		mAnimDown = new Animation(1, Animation.LoopType.PING_PONG, 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DOWN_3), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DOWN_2), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DOWN_1), 0);
		mAnimDown.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN), 0);

		mAnimDead = new Animation(3, Animation.LoopType.NONE, 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_0), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_1), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_2), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_3), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_4), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_5), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_6), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_7), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_8), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_9), 0);
		mAnimDead.addFrame(BitmapServer.getBitmap(BitmapServer.Bitmap.PACMAN_DEAD_10), 0);

		mAnimation = mAnimRight;

		mLives = MAX_LIVES;
		mState = State.NORMAL;
	}

	/**
	 * Increase x co-ord by deltaX; use negatives to decrease x.  Extends method
	 * so that PacMan picks up pills as he moves.
	 * @param deltaX Amount to increase x by
	 */
	@Override
	protected boolean increaseX(int deltaX) {
		
		if (super.increaseX(deltaX)) {

			// Increase score if we cross a pill
			checkPill();

			// Handle animation changes
			if (deltaX >= 0) {
				// Going right
				mAnimation = mAnimRight;
			} else {
				// Going left
				mAnimation = mAnimLeft;
			}

			mAnimation.play();
			return true;
		} else {
			mAnimation.pause();
			return false;
		}
	}

	/**
	 * Increase y co-ord by deltaY; use negatives to decrease y.  Extends method
	 * so that PacMan picks up pills as he moves.
	 * @param deltaY Amount to increase y by
	 */
	@Override
	protected boolean increaseY(int deltaY) {

		if (super.increaseY(deltaY)) {

			// Increase score if we cross a pill
			checkPill();

			// Handle animation changes
			if (deltaY >= 0) {
				// Going down
				mAnimation = mAnimDown;
			} else {
				// Going up
				mAnimation = mAnimUp;
			}

			mAnimation.play();
			return true;
		} else {
			mAnimation.pause();
			return false;
		}
	}

	/**
	 * Extend run method to include collision checks with other sprites.
	 */
	@Override
	public void run() {

		super.run();

		if (mState == State.NORMAL) {

			PlayerList players = PacManGame.getGame().getPlayerList();
			Ghost ghost;

			// Check for collisions with all ghosts
			for (int i = 0; i < players.size(); ++i) {
				if (players.get(i).getSprite() != this) {

					ghost = (Ghost)players.get(i).getSprite();

					if (checkCollision(ghost)) {

						// Is the ghost edible?
						if (ghost.isEdible()) {

							// Eat the ghost
							ghost.makeEaten();

							// Increase the score and remember the ghost was eaten
							PowerPillTimer.addGhostEaten();
							mScore.add(PowerPillTimer.getGhostsEaten() * Score.SCORE_EAT_GHOST);

							SoundPlayer.playSound(SoundPlayer.Sound.EAT_GHOST);
						} else if (ghost.isEaten()) {

							// Ignore collisions with ghosts that have been eaten
						} else {

							// Increase the ghost's score
							ghost.getScore().add(Score.SCORE_EAT_PACMAN);

							// Lose a life
							loseLife();
						}
					}
				}
			}
		}
	}

	private void loseLife() {
		mLives--;
		mAnimation = mAnimDead;
		mAnimation.stop();
		mAnimation.play();
		mState = State.DEAD;

		// Game over?
		if (mLives > 0) {

			// More lives left
			PacManGame.getGame().loseLife();
		} else {

			// Game ends
			PacManGame.getGame().endGame();
		}
	}

	/**
	 * Check if Pac Man has collected a pill.
	 */
	private void checkPill() {
		
		// Get block that sprite is centred on
		int blockX = mX + (mWidth >> 1);
		int blockY = mY + (mHeight >> 1);

		switch (PacManGame.getGame().getMap().getBlock(blockX, blockY)) {
			case Map.BLOCK_POWERPILL:

				// Collect a powerpill
				PowerPillTimer.start();

				// Increase score
				mScore.add(Score.SCORE_POWER_PILL);
			case Map.BLOCK_PILL:
				// Increase score
				mScore.add(Score.SCORE_PILL);
				break;
		}
		
		// Clear the block
		if (PacManGame.getGame().getMap().clearBlock(blockX, blockY)) {
			SoundPlayer.playSound(SoundPlayer.Sound.EAT_PILL);

			// Out of pills?
			if (PacManGame.getGame().getMap().getPillCount() == 0) {
				PacManGame.getGame().endGame();
			}
		}
	}

	/**
	 * Reset sprite to its initial location.
	 */
	@Override
	public void reset() {
		overridePosition(START_BLOCK_X * Map.BLOCK_WIDTH, START_BLOCK_Y * Map.BLOCK_HEIGHT, Direction.NONE, Direction.NONE);
		mAnimation.pause();
		mAnimation = mAnimRight;
		setFrozen(false);
		mState = State.NORMAL;
	}
	
	/**
	 * Extends base method with checking for pills.
	 * @param x The new x co-ord.
	 * @param y The new y co-ord.
	 * @param direction The new direction.
	 * @param bufferedDirection The new buffered direction.
	 */
	@Override
	public void overridePosition(int x, int y, Direction direction, Direction bufferedDirection) {
		super.overridePosition(x, y, direction, bufferedDirection);
		
		// Increase score if we cross a pill
		checkPill();
	}

	/**
	 * Get lives remaining.
	 * @return The number of lives remaining.
	 */
	public int getLives() { return mLives; }
}
