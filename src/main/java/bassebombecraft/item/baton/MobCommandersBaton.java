package bassebombecraft.item.baton;

import static bassebombecraft.config.ModConfiguration.*;
import bassebombecraft.item.action.command.CommandMobs;
import bassebombecraft.item.book.GenericRightClickedBook;

/**
 * Mob Commander's Baton.
 */
public class MobCommandersBaton extends GenericRightClickedBook {

	public static final String ITEM_NAME = MobCommandersBaton.class.getSimpleName();

	public MobCommandersBaton() {
		super(mobCommandersBaton, new CommandMobs());
	}

}
