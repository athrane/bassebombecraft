package bassebombecraft.item.inventory;

import static bassebombecraft.ModConstants.MODID;

import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when the item is in player hotbar.
 */
public class GenericInventoryItem extends Item {

	/**
	 * Rendering frequency in ticks.
	 */
	static final int RENDERING_FREQUENCY = 5;

	/**
	 * Effect frequency when effect is invoked. Frequency is measured in ticks.
	 */
	static final int EFFECT_UPDATE_FREQUENCY = 200; // Measured in ticks

	/**
	 * Ticks counter.
	 */
	int ticksCounter = 0;

	/**
	 * Item action.
	 */
	RightClickedItemAction action;

	/**
	 * Generic book constructor.
	 * 
	 * @param name
	 *            item name.
	 * @param action
	 *            item action object which is invoked when item is right
	 *            clicked.
	 */
	public GenericInventoryItem(String name, RightClickedItemAction action) {
		setUnlocalizedName(name);
		this.action = action;
		registerForRendering(this);
	}

	/**
	 * Register item for rendering.
	 * 
	 * @param item
	 *            item to be registered.
	 */
	void registerForRendering(Item item) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation location;
		location = new ModelResourceLocation(MODID + ":" + getUnlocalizedName().substring(5), "inventory");
		renderItem.getItemModelMesher().register(item, 0, location);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// only apply the action at server side since we updates the world
		if (isWorldAtClientSide(worldIn))
			return;

		// exit if item isn't in hotbar
		if (!isInHotbar(itemSlot))
			return;

		// render effect
		if (ticksCounter % RENDERING_FREQUENCY == 0) {
			// render(worldIn);
		}

		// update game effect
		if (ticksCounter % EFFECT_UPDATE_FREQUENCY == 0) {

			// exit if entity isn't a EntityLivingBase
			if (!(entityIn instanceof EntityLivingBase))
				return;
			EntityLivingBase entityLivingBase = (EntityLivingBase) entityIn;

			// do action
			action.onRightClick(worldIn, entityLivingBase);
		}

		// do update
		// action.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		ticksCounter++;
	}

	/**
	 * Returns true if item is is user hotbar.
	 * 
	 * @param itemSlot
	 *            user item slot. the hotbar is between 0 and 8 inclusive.
	 * @return true if item is is user hotbar
	 */
	boolean isInHotbar(int itemSlot) {
		if (itemSlot < 0)
			return false;
		if (itemSlot > 8)
			return false;
		return true;
	}

	/**
	 * return true if world is located at client side.
	 * 
	 * @return true if world is located at client side.
	 */
	boolean isWorldAtClientSide(World world) {
		return world.isRemote;
	}

}
