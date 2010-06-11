package graphics;

import java.awt.image.*;
import java.util.*;

/**
 * Animation class.  Animates sequences of Images.
 */
public class Animation {
	private int mSpeed;
	private int mFrameTimer;
	private int mCurrentFrame;
	private Status mStatus;
	private LoopType mLoopType;
	private int mFrameInc;
	private int mRequestedLoops;
	private int mLoopCount;
	private ArrayList<AnimFrame> mFrames;
	private ArrayList<AnimationListener> mListeners;

	/**
	 * Enum defining all loop behaviour of the animation.
	 */
	public enum LoopType {
		NONE,
		LOOP,
		PING_PONG;
	}

	/**
	 * Enum defining all statuses of the animation.
	 */
	public enum Status {
		STOPPED,
		PLAYING,
		PAUSED;
	}

	/**
	 * Enum listing all events the animation can raise.
	 */
	public enum EventType {
		STOPPED,
		STARTED,
		PAUSED,
		LOOPED;
	}

	/**
	 * Private class representing a single frame of the animation.
	 */
	private class AnimFrame {
		private BufferedImage mBitmap;
		private int mDelay;

		/**
		 * Constructor.
		 * @param bitmap Image for this frame.
		 * @param delay Amount of time the frame will be displayed for in
		 * addition to the usual frame time.
		 */
		public AnimFrame(BufferedImage bitmap, int delay) {
			mBitmap = bitmap;
			mDelay = delay;
		}

		/**
		 * Get the frame's bitmap.
		 * @return The frame's bitmap.
		 */
		public BufferedImage getBitmap() { return mBitmap; }

		/**
		 * Get the delay associated with this frame.
		 * @return The frame delay.
		 */
		public int getDelay() { return mDelay; }
	}

	/**
	 * Constructor.
	 * @param speed Speed of the animation, measured in refreshes.
	 * @param loopType Type of loop used.
	 * @param loops Number of loops.  If set to 0, and if loop type is not none,
	 * the animation will loop indefinitely.
	 */
	public Animation(int speed, LoopType loopType, int loops) {
		mSpeed = speed;
		mFrameTimer = speed;
		mCurrentFrame = 0;
		mStatus = Status.STOPPED;
		mLoopType = loopType;
		mFrameInc = 1;
		mRequestedLoops = loops;
		mLoopCount = 0;
		mFrames = new ArrayList<AnimFrame>();
		mListeners = new ArrayList<AnimationListener>();
	}

	/**
	 * Add a frame to the animation.
	 * @param image Image to add.
	 * @param delay Delay associated with the frame; added to the standard frame
	 * rate to allow a frame to be displayed for longer than usual.
	 */
	public void addFrame(BufferedImage image, int delay) {
		mFrames.add(new AnimFrame(image, delay));
	}

	/**
	 * Run the animation; should be called every screen refresh.
	 */
	public void run() {
		if (mStatus == Status.PLAYING) {

			// Show new frame?
			if (mFrameTimer > 0) {
				mFrameTimer--;
			} else {
				// New frame due
				if (((mCurrentFrame == mFrames.size() - 1) && (mFrameInc > 0)) ||
						((mCurrentFrame == 0) && (mFrameInc < 0))) {

					// Hit the end of the animation; try to loop
					loop();
				} else {

					// Advance to next frame
					mCurrentFrame += mFrameInc;
					mFrameTimer = mSpeed + mFrames.get(mCurrentFrame).getDelay();
				}
			}
		}
	}

	/**
	 * Attempt to loop the animation
	 * @return True if the animation looped
	 */
	private boolean loop() {
		switch (mLoopType) {
			case NONE:
				// Stop the animation
				stop();
				return false;

			case LOOP:
				// Standard loop
				mCurrentFrame = 0;
				mLoopCount++;
				break;

			case PING_PONG:
				// Ping pong loop
				mFrameInc = 0 - mFrameInc;
				mCurrentFrame += mFrameInc;
				mLoopCount++;
				break;
		}

		// Exceeded max number of loops?
		if ((mRequestedLoops > 0) && (mLoopCount > mRequestedLoops)) {
			stop();
			return false;
		}

		// Reset frame timer to standard time plus frame delay
		mFrameTimer = mSpeed + mFrames.get(mCurrentFrame).getDelay();

		raiseEvent(EventType.LOOPED);
		return true;
	}

	/**
	 * Start the animation playing.
	 */
	public void play() {

		// Need to reset the current frame?
		if (mStatus == Status.STOPPED) {
			mCurrentFrame = 0;
		}

		mStatus = Status.PLAYING;
		mFrameTimer = mSpeed * mFrames.get(mCurrentFrame).getDelay();

		raiseEvent(EventType.STARTED);
	}

	/**
	 * Stop the animation from playing.
	 */
	public void stop() {
		mStatus = Status.STOPPED;
		mFrameTimer = 0;
		mLoopCount = 0;

		raiseEvent(EventType.STOPPED);
	}

	/**
	 * Pause the animation.
	 */
	public void pause() {
		mStatus = Status.PAUSED;
		raiseEvent(EventType.PAUSED);
	}

	/**
	 * Jump to the specified frame.  Frame is not bounds checked, so it must be
	 * a valid frame index.
	 * @param frame Frame number to jump to.
	 */
	public void goToFrame(int frame) {
		mCurrentFrame = frame;
		mFrameTimer = mSpeed + mFrames.get(mCurrentFrame).getDelay();
	}

	/**
	 * Get a pointer to the current frame's bitmap.
	 * @return A pointer to the current frame's bitmap.
	 */
	public BufferedImage getCurrentBitmap() { return mFrames.get(mCurrentFrame).getBitmap(); }

	/**
	 * Get the animation's status.
	 * @return The animation's status.
	 */
	public Status getStatus() { return mStatus; }

	/**
	 * Get the number of frames in the animation.
	 * @return The number of frames in the animation.
	 */
	public int getFrameCount() { return mFrames.size(); }

	/**
	 * Add a listener to the animation.
	 * @param listener The listener to add.
	 */
	public void addListener(AnimationListener listener) {
		mListeners.add(listener);
	}

	/**
	 * Remove a listener from the animation.
	 * @param listener The listener to remove.
	 */
	public void removeListener(AnimationListener listener) {
		mListeners.remove(listener);
	}

	/**
	 * Raise an event to all listeners.
	 * @param type Type of the event.
	 */
	private void raiseEvent(EventType type) {
		for (AnimationListener listener : mListeners) {
			switch (type) {
				case LOOPED:
					listener.animationLooped(new AnimationEvent(this));
					break;
				case PAUSED:
					listener.animationPaused(new AnimationEvent(this));
					break;
				case STARTED:
					listener.animationStarted(new AnimationEvent(this));
					break;
				case STOPPED:
					listener.animationStopped(new AnimationEvent(this));
					break;
			}
		}
	}
}
