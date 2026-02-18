package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.digMobHoleBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER7;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * Book of mob hole.
 */
public class DigMobHoleBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = DigMobHoleBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER7);

	public DigMobHoleBook() {
		super(digMobHoleBook, splComposites);
	}
}
