package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.PROJECTILE5;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * Book of small fireball implementation.
 */
public class SmallFireballBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = SmallFireballBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE5);

	public SmallFireballBook() {
		super(smallFireballBook, splComposites);
	}
}
