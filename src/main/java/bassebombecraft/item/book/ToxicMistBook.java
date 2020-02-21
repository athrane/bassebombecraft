package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.ToxicMist;

/**
 * Book of toxic mist implementation.
 */
public class ToxicMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "ToxicMistBook";
	static final EntityMistActionStrategy STRATEGY = new ToxicMist();		
	
	public ToxicMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
