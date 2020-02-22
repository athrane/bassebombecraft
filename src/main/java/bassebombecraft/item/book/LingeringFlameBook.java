package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LingeringFlameMist;

/**
 * Book of lingering flame implementation.
 */
public class LingeringFlameBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "LingeringFlameBook";
	static final int EXPLOSION_RADIUS = 2;
	static final EntityMistActionStrategy STRATEGY = new LingeringFlameMist(EXPLOSION_RADIUS);

	public LingeringFlameBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
