package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.iceBlockBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER11;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

/**
 * Book of ice block implementation.
 */
public class IceBlockBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = IceBlockBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER11);

	public IceBlockBook() {
		super(iceBlockBook, splComposites);
	}
}
