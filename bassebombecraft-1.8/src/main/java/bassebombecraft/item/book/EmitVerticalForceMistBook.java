package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.entity.EmitVerticalForceMist;
import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;

/**
 * Book of vertical force mist implementation.
 */
public class EmitVerticalForceMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "EmitVerticalForceMistBook";
	static final EntityMistActionStrategy STRATEGY = new EmitVerticalForceMist();

	public EmitVerticalForceMistBook() {
		super(ITEM_NAME, new GenericEntityMist(STRATEGY));
	}
}
