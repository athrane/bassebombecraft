package bassebombecraft.inventory.container;

import static bassebombecraft.ModConstants.COMPOSITE_MAX_SIZE;

import bassebombecraft.item.composite.CompositeMagicItem;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Implementation of capabilities supported by {@linkplain CompositeMagicItem}.
 * 
 * The provider only supports one capability ITEM_HANDLER for storing the
 * {@linkplain CompositeMagicItemItemStackHandler} used to store composite
 * items.
 */
public class CompositeMagicItemCapabilityProvider implements ICapabilitySerializable<Tag> {

	/**
	 * Null direction.
	 */
	final static Direction NO_SPECIFIC_SIDE = null;

	/**
	 * Cached instance.
	 * 
	 * Initially null until our first call to getCachedInventory
	 */
	CompositeMagicItemItemStackHandler itemStackHandler;

	/**
	 * Supplier which returns the result of getCachedInventory().
	 */
	final LazyOptional<IItemHandler> lazyInitialisionSupplier = LazyOptional.of(this::getCachedInventory);

	/**
	 * Factory method to return cached instance.
	 * 
	 * @return cached inventory instance
	 */
	CompositeMagicItemItemStackHandler getCachedInventory() {
		if (itemStackHandler == null)
			itemStackHandler = new CompositeMagicItemItemStackHandler(COMPOSITE_MAX_SIZE);
		return itemStackHandler;
	}

	@Override
	public Tag serializeNBT() {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(getCachedInventory(), NO_SPECIFIC_SIDE);
	}

	@Override
	public void deserializeNBT(Tag nbt) {
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(getCachedInventory(), NO_SPECIFIC_SIDE, nbt);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, lazyInitialisionSupplier);
	}

}
