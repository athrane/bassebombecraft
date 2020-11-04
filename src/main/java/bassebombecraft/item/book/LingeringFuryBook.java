package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.lingeringFuryBook;

import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LingeringFlameMist;

/**
 * Book of lingering flame implementation.
 */
public class LingeringFuryBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = LingeringFuryBook.class.getSimpleName();
	static final int EXPLOSION_RADIUS = 5;
	static final EntityMistActionStrategy STRATEGY = new LingeringFlameMist(EXPLOSION_RADIUS);

	public LingeringFuryBook() {
		super(lingeringFuryBook, new GenericEntityMist(STRATEGY));
	}
}
