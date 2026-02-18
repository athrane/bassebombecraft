package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.receiveAggroBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER14;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * Book of receive mob aggro implementation.
 */
public class ReceiveAggroBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = ReceiveAggroBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER14);

	public ReceiveAggroBook() {
		super(receiveAggroBook, splComposites);
	}

}
