package bassebombecraft.item.basic;

import bassebombecraft.item.action.NullRightClickedItemAction;

/**
 * Terminator eye implementation.
 */
public class TerminatorEyeItem extends GenericNullItem {

	/**
	 * Item identifier.
	 */
	public final static String NAME = TerminatorEyeItem.class.getSimpleName();

	/**
	 * TerminatorEyeItem no-arg constructor,
	 */
	public TerminatorEyeItem() {
		super(NAME, new NullRightClickedItemAction());
	}

}
