package bassebombecraft.item.composite;

import static bassebombecraft.util.function.Functions.getFnCastToCompositeItem;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fmllegacy.RegistryObject;

/**
 * Default implementation of the {@linkplain CompositeItemsResolver} interface.
 * 
 * Composite items are resolved on first invocation.
 */
public class DefaultCompositeItemResolver implements CompositeItemsResolver {

	/**
	 * Combination length.
	 */
	static final int COMBINATION_MAX_LENGTH = 6;

	/**
	 * Stream of unresolved composite items.
	 */
	Supplier<Stream<RegistryObject<Item>>> splUnresolvedComposites;

	/**
	 * Operator.
	 */
	Operator2 operator;

	/**
	 * Item stacks for rendering.
	 */
	ItemStack[] itemsStacks;

	/**
	 * Status flag for resolution state.
	 */
	boolean isResolved;

	/**
	 * Constructor
	 * 
	 * @param splComposites stream of registry objects for composite items which can
	 *                      be resolved to item stacks and operators.
	 */
	DefaultCompositeItemResolver(Supplier<Stream<RegistryObject<Item>>> splComposites) {
		this.splUnresolvedComposites = splComposites;
		this.isResolved = false;
	}

	/**
	 * Resolve registry objects to item stacks and operator.
	 */
	void doInternalResolution() {
		resolveCompositeItems();
		resolveOperators();

		// set state to resolved
		isResolved = true;
	}

	/**
	 * Resolve composite item stacks from composite item stream.
	 */
	void resolveCompositeItems() {

		// create stream of item stacks from item registry objects
		Stream<RegistryObject<Item>> composites = splUnresolvedComposites.get();
		Stream<ItemStack> isStream = composites.map(ro -> ro.get()).map(i -> new ItemStack(i));

		// create padding stream of "empty" item stacks
		Supplier<ItemStack> splItemStack = () -> new ItemStack(Items.AIR);
		Stream<ItemStack> emptyStream = Stream.generate(splItemStack);

		// create array of item stacks from concat'ed streams
		itemsStacks = Streams.concat(isStream, emptyStream).limit(COMBINATION_MAX_LENGTH).toArray(ItemStack[]::new);
	}

	/**
	 * Resolve operators from composite item stream.
	 */
	void resolveOperators() {

		// create stream of operators from item registry objects
		Stream<RegistryObject<Item>> composites = splUnresolvedComposites.get();
		Function<Item, GenericCompositeNullItem> fnTypeCast = getFnCastToCompositeItem();
		Stream<Operator2> opStream = composites.map(ro -> ro.get()).map(fnTypeCast).map(co -> co.createOperator());

		// create operator array
		Operator2[] opArray = opStream.toArray(Operator2[]::new);

		// create sequence operator
		operator = new Sequence2(opArray);
	}

	@Override
	public ItemStack[] getItemStacks() {
		if (isResolved)
			return itemsStacks;

		// otherwise resolve
		doInternalResolution();
		return itemsStacks;
	}

	@Override
	public Operator2 getOperator() {
		if (isResolved)
			return operator;

		// otherwise resolve		
		doInternalResolution();
		return operator;
	}

	/**
	 * Factory method.
	 * 
	 * @param splComposites stream of registry objects for composite items which can
	 *                      be resolved to item stacks and operators.
	 * @return resolver instance.
	 */
	public static CompositeItemsResolver getInstance(Supplier<Stream<RegistryObject<Item>>> splComposites) {
		return new DefaultCompositeItemResolver(splComposites);
	}
}
