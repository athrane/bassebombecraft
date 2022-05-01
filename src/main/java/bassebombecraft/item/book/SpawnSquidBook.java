package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.spawnSquidBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER20;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

/**
 * Book of spawn squid implementation.
 */
public class SpawnSquidBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = SpawnSquidBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER20);

	public SpawnSquidBook() {
		super(spawnSquidBook, splComposites);
	}
}
