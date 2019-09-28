package bassebombecraft.item.baton;

import bassebombecraft.item.action.command.CommandMobs;
import bassebombecraft.item.book.GenericRightClickedBook;

/**
 * Mob Commander's Baton.
 */
public class MobCommandersBaton extends GenericRightClickedBook {

	public final static String ITEM_NAME = MobCommandersBaton.class.getSimpleName();

	public MobCommandersBaton() {
		super(ITEM_NAME, new CommandMobs());
	}

}
