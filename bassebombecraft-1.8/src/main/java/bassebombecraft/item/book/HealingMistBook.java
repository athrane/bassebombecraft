package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.HealingMist;

/**
 * Book of healing mist implementation.
 */
public class HealingMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "HealingMistBook";
	static final EntityMistActionStrategy STRATEGY = new HealingMist();	
	
	public HealingMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
