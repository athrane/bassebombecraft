package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.largeFireballBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.PROJECTILE4;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.RegistryObject;

/**
 * Book of large fireball implementation.
 */
public class LargeFireballBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = LargeFireballBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE4);

	public LargeFireballBook() {
		super(largeFireballBook, splComposites);
	}
}
