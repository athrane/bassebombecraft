package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.toxicMistBook;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.ToxicMist;

/**
 * Book of toxic mist implementation.
 */
public class ToxicMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = ToxicMistBook.class.getSimpleName();
	static final EntityMistActionStrategy STRATEGY = new ToxicMist();

	public ToxicMistBook() {
		super(ITEM_NAME, toxicMistBook, new GenericEntityMist(STRATEGY));
	}
}
