package bassebombecraft.util.function;

import java.util.function.Function;

import bassebombecraft.item.book.GenericCompositeItemsBook;
import bassebombecraft.item.composite.GenericCompositeNullItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

/**
 * Reusable functions.
 */
public class Functions {

	/**
	 * Cast {@linkplain LivingEntity} to {@linkplain Entity}.
	 * 
	 * @return return {@linkplain Entity}.
	 * 
	 * @throws ClassCastException if the object is not null and is not assignable to
	 *                            {@linkplain Entity}.
	 */
	public static Function<LivingEntity, Entity> getFnCastToEntity() {
		return le -> Entity.class.cast(le);
	}

	/**
	 * Cast {@linkplain Item} to {@linkplain GenericCompositeNullItem}.
	 * 
	 * @return return {@linkplain GenericCompositeNullItem}.
	 * 
	 * @throws ClassCastException if the object is not null and is not assignable to
	 *                            {@linkplain GenericCompositeNullItem}.
	 */
	public static Function<Item, GenericCompositeNullItem> getFnCastToCompositeItem() {
		return le -> GenericCompositeNullItem.class.cast(le);
	}

	/**
	 * Cast {@linkplain Item} to {@linkplain GenericCompositeItemsBook}.
	 * 
	 * @return return {@linkplain GenericCompositeItemsBook}.
	 * 
	 * @throws ClassCastException if the object is not null and is not assignable to
	 *                            {@linkplain GenericCompositeItemsBook}.
	 */
	public static Function<Item, GenericCompositeItemsBook> getFnCastToGenericCompositeItemsBook() {
		return le -> GenericCompositeItemsBook.class.cast(le);
	}
	
}
