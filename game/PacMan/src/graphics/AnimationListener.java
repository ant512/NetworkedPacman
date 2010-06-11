package graphics;

import java.util.EventListener;

/**
 * Interface defining an AnimationListener.
 */
public interface AnimationListener extends EventListener {

	/**
	 * Animation has stopped.
	 * @param e Event object.
	 */
	public void animationStopped(AnimationEvent e);

	/**
	 * Animation has looped.
	 * @param e Event object.
	 */
	public void animationLooped(AnimationEvent e);

	/**
	 * Animation has started.
	 * @param e Event object.
	 */
	public void animationStarted(AnimationEvent e);

	/**
	 * Animation has paused.
	 * @param e Event object.
	 */
	public void animationPaused(AnimationEvent e);
}
