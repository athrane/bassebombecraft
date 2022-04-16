package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.witherSkullBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.PROJECTILE7;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.RegistryObject;

/**
 * Book of wither skull implementation.
 */
public class WitherSkullBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = WitherSkullBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE7);

	public WitherSkullBook() {
		super(witherSkullBook, splComposites);
	}
}
