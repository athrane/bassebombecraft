package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;

/**
 * Book of mob vacuum mist implementation.
 */
public class VacuumMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "VacuumMistBook";
	static final EntityMistActionStrategy STRATEGY = new VacuumMist();		
	
	public VacuumMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
