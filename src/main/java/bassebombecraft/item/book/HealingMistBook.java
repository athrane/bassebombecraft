package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.healingMistBook;

import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.HealingMist;

/**
 * Book of healing mist implementation.
 */
public class HealingMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = HealingMistBook.class.getSimpleName();

	public HealingMistBook() {
		super(healingMistBook, new GenericEntityMist(new HealingMist()));
	}
}
