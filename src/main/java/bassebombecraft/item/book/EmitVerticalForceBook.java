package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.emitVerticalForceBook;
import static bassebombecraft.item.RegisteredItems.FORMATION1;
import static bassebombecraft.item.RegisteredItems.MODIFIER17;
import static bassebombecraft.item.RegisteredItems.PROJECTILE3;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.RegistryObject;

/**
 * Book of emit vertical force implementation.
 */
public class EmitVerticalForceBook extends GenericCompositeItemsBook {

	public static final String ITEM_NAME = EmitVerticalForceBook.class.getSimpleName();

	/**
	 * Composite items.
	 */
	static Supplier<Stream<RegistryObject<Item>>> splComposites = () -> Stream.of(FORMATION1, PROJECTILE3, MODIFIER17);

	public EmitVerticalForceBook() {
		super(emitVerticalForceBook, splComposites);
	}
}
