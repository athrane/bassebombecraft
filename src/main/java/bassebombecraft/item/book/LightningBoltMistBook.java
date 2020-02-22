package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LightningBoltMist;

/**
 * Book of lightning bolt mist implementation.
 */
public class LightningBoltMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = LightningBoltMistBook.class.getSimpleName();
	static final EntityMistActionStrategy STRATEGY = new LightningBoltMist();		
	
	public LightningBoltMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
