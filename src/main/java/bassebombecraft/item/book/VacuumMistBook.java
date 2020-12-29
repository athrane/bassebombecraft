package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.vacuumMistBook;

import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;

/**
 * Book of vacuum mist implementation.
 */
public class VacuumMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = VacuumMistBook.class.getSimpleName();

	public VacuumMistBook() {
		super(vacuumMistBook, new GenericEntityMist(new VacuumMist()));
	}
}
