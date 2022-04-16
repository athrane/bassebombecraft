package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.cobwebBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER10;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.RegistryObject;

/**
 * Book of cobweb implementation.
 */
public class CobwebBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = CobwebBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER10);

	public CobwebBook() {
		super(cobwebBook, splComposites);
	}
}
