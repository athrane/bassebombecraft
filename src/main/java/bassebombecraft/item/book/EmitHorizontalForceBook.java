package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.emitHorizontalForceBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER16;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

/**
 * Book of emit horizontal force implementation.
 */
public class EmitHorizontalForceBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = EmitHorizontalForceBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER16);

	public EmitHorizontalForceBook() {
		super(emitHorizontalForceBook, splComposites);
	}
}
