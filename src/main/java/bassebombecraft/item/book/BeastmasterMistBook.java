package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.BeastmasterMist;
import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;

/**
 * Book of beast master. 
 */
public class BeastmasterMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "BeastmasterMistBook";
	static final EntityMistActionStrategy STRATEGY = new BeastmasterMist();	
	
	public BeastmasterMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
