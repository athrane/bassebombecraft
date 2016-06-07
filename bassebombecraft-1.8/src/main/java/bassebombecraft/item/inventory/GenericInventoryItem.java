package bassebombecraft.item.inventory;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;

import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
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

	final static int AOE_RANGE = 5;
	
	/**
	 * Ticks counter.
	 */
	int ticksCounter = 0;

	/**
	 * Item action.
	 */
	RightClickedItemAction action;

	/**
	 * GenericInventoryItem constructor.
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

	boolean isEffectAppliedToInvoker() {
		return false;
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

	/**
	 * Apply effect to creatures within range.
	 * 
	 * @param world
	 *            world object
	 * @param invokingEntity
	 *            entity object
	 */
	void applyEffect(World world, EntityLivingBase invokingEntity) {

		// get entities within AABB
		AxisAlignedBB aabb = AxisAlignedBB.fromBounds(invokingEntity.posX - AOE_RANGE, invokingEntity.posY - AOE_RANGE,
				invokingEntity.posZ - AOE_RANGE, invokingEntity.posX + AOE_RANGE, invokingEntity.posY + AOE_RANGE,
				invokingEntity.posZ + AOE_RANGE);
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

		for (EntityLivingBase foundEntity : entities) {

			// skip invoking entity if strategy specifies it
			if (hasIdenticalUniqueID(invokingEntity, foundEntity)) {
				if (!isEffectAppliedToInvoker())
					continue;
			}

			// apply effect
			action.onRightClick(world, foundEntity);

			/**
			// exit if strategy is one shot effect
			if (strategy.isOneShootEffect()) {
				isActive = false;
				return;
			}
			**/
		}
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

			// apply effect
			applyEffect(worldIn, entityLivingBase);						
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
