package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.vacuumMistBook;
import static bassebombecraft.config.ModConfiguration.*;

import java.util.function.Supplier;

import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;

/**
 * Book of vacuum mist implementation.
 */
public class VacuumMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = VacuumMistBook.class.getSimpleName();
	static Supplier<Integer> splDuration = () -> vacuumMistDuration.get();
	static Supplier<Integer> splForce = () -> vacuumMistForce.get();

	public VacuumMistBook() {
		super(ITEM_NAME, vacuumMistBook, new GenericEntityMist(new VacuumMist(splDuration, splForce)));
	}
}
