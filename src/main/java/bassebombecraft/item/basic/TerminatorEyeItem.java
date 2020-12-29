package bassebombecraft.item.basic;

import bassebombecraft.item.action.NullRightClickedItemAction;

import static bassebombecraft.config.ModConfiguration.terminatorEyeItem;;

/**
 * Terminator eye implementation.
 */
public class TerminatorEyeItem extends GenericNullItem {

	public static final String NAME = TerminatorEyeItem.class.getSimpleName();

	public TerminatorEyeItem() {
		super(terminatorEyeItem, new NullRightClickedItemAction());
	}

}
