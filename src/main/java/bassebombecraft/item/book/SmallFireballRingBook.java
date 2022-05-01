package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballRingBook;
import static bassebombecraft.item.RegisteredItems.FORMATION3;
import static bassebombecraft.item.RegisteredItems.PROJECTILE5;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

/**
 * Book of small fireball ring implementation.
 */
public class SmallFireballRingBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = SmallFireballRingBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION3, PROJECTILE5);

	public SmallFireballRingBook() {
		super(smallFireballRingBook, splComposites);
	}
}
