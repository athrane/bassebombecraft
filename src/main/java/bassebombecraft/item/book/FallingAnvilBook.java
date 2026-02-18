package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.fallingAnvilBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER13;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * Book of anvil block implementation.
 */
public class FallingAnvilBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = FallingAnvilBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER13);

	public FallingAnvilBook() {
		super(fallingAnvilBook, splComposites);
	}
}
