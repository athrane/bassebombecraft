package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.multipleArrowsBook;
import static bassebombecraft.item.RegisteredItems.FORMATION4;
import static bassebombecraft.item.RegisteredItems.PROJECTILE6;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * Book of multiple arrows implementation.
 */
public class MultipleArrowsBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = MultipleArrowsBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION4, PROJECTILE6);

	public MultipleArrowsBook() {
		super(multipleArrowsBook, splComposites);
	}
}
