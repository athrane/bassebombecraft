package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.lavaSpiralMistDuration;
import static bassebombecraft.config.ModConfiguration.lavaSpiralMistBook;

import java.util.function.Supplier;

import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.LavaSpiralMist;

/**
 * Book of Lava spiral block implementation.
 */
public class LavaSpiralMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = LavaSpiralMistBook.class.getSimpleName();
	static Supplier<Integer> splDuration = () -> lavaSpiralMistDuration.get();

	public LavaSpiralMistBook() {
		super(ITEM_NAME, lavaSpiralMistBook, new GenericBlockSpiralFillMist(new LavaSpiralMist(splDuration)));
	}
}
