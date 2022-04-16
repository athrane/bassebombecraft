package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.lavaBlockBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER12;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.RegistryObject;

/**
 * Book of lava block implementation.
 */
public class LavaBlockBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = LavaBlockBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER12);

	public LavaBlockBook() {
		super(lavaBlockBook, splComposites);
	}
}
