package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.WitherMist;

/**
 * Book of wither mist implementation.
 */
public class WitherMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "WitherMistBook";
	static final EntityMistActionStrategy STRATEGY = new WitherMist();	
	
	public WitherMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
