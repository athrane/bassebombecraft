package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.lightningBoltBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER19;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

/**
 * Book of lightning bolt implementation.
 */
public class LightningBoltBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = LightningBoltBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER19);

	public LightningBoltBook() {
		super(lightningBoltBook, splComposites);
	}
}
