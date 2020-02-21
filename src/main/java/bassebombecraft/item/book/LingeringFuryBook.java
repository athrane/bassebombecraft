package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LingeringFlameMist;

/**
 * Book of lingering flame implementation.
 */
public class LingeringFuryBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "LingeringFuryBook";
	static final int EXPLOSION_RADIUS = 5;	
	static final EntityMistActionStrategy STRATEGY = new LingeringFlameMist(EXPLOSION_RADIUS);
	
	public LingeringFuryBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
