package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.decoyBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER5;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.RegistryObject;

/**
 * Book of decoy implementation.
 */
public class DecoyBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = DecoyBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER5);

	public DecoyBook() {
		super(decoyBook, splComposites);
	}

}
