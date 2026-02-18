package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.teleportBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER1;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * Book of teleport implementation.
 */
public class TeleportBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = TeleportBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER1);

	public TeleportBook() {
		super(teleportBook, splComposites);
	}
}
