package graphics;

import java.util.EventObject;

/**
 * Event raised by the Animation class.
 */
public class AnimationEvent extends EventObject {

	/**
	 * Constructor.
	 * @param source Pointer to the animation that raised the event.
	 */
	public AnimationEvent(Animation source) {
		super(source);
	}
}
