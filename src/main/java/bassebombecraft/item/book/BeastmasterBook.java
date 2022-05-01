package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.beastmasterBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER3;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

/**
 * Book of beast master.
 */
public class BeastmasterBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = BeastmasterBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER3);

	public BeastmasterBook() {
		super(beastmasterBook, splComposites);
	}
}
