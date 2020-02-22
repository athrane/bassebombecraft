package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.MovingWitherMist;

/**
 * Book of moving wither mist implementation.
 */
public class MovingWitherMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "MovingWitherMistBook";
	static final EntityMistActionStrategy STRATEGY = new MovingWitherMist();

	public MovingWitherMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
