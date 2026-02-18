package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.spawnFlamingChickenBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER22;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

/**
 * Book of failed phoenix.
 */
public class SpawnFlamingChickenBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = SpawnFlamingChickenBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER22);

	public SpawnFlamingChickenBook() {
		super(spawnFlamingChickenBook, splComposites);
	}

}
