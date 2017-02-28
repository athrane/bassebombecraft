package bassebombecraft.item.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.ITEM_IDOL_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.config.ConfigUtils.resolveCoolDown;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;
import static bassebombecraft.player.PlayerUtils.isEntityPlayer;
import static bassebombecraft.player.PlayerUtils.isItemHeldInOffHand;
import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import java.util.List;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.item.action.inventory.InventoryItemActionStrategy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when the item is in player hotbar.
 */
public class GenericInventoryItem extends Item {

	/**
	 * Item strategy.
	 */
	InventoryItemActionStrategy strategy;

	/**
	 * Particle repository.
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * Idol item cooldown value.
	 */
	int coolDown;

	/**
	 * GenericInventoryItem constructor.
	 * 
	 * @param name
	 *            item name.
	 * @param strategy
	 *            inventory item strategy.
	 */
	public GenericInventoryItem(String name, InventoryItemActionStrategy strategy) {
		setUnlocalizedName(name);
		this.strategy = strategy;
		registerForRendering(this);
		particleRepository = getBassebombeCraft().getParticleRenderingRepository();

		// get cooldown or default value
		coolDown = resolveCoolDown(name, ITEM_IDOL_DEFAULT_COOLDOWN);
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

		// determine if item is held by player and should activate from off hand
		boolean shouldActivateFromOffHand = false;
		if (isEntityPlayer(entityIn)) {
			EntityPlayer player = (EntityPlayer) entityIn;
			shouldActivateFromOffHand = isItemHeldInOffHand(player, stack);
		}

		// determine if item should activate from selection in hotbar
		boolean shouldActivateFromHotbar = false;

		// exit if item requires selection and it isn't selected
		if (strategy.applyOnlyIfSelected()) {
			if (isSelected)
				shouldActivateFromHotbar = true;
		} else {
			shouldActivateFromHotbar = true;
		}

		if (!(shouldActivateFromOffHand || shouldActivateFromHotbar))
			return;

		// exit if entity isn't a EntityLivingBase
		if (!(entityIn instanceof EntityLivingBase))
			return;

		// exit if entity isn't player
		if (!isEntityPlayer(entityIn))
			return;

		// type cast as player
		EntityPlayer player = (EntityPlayer) entityIn;

		// exit if cooldown is effect
		if (player.getCooldownTracker().hasCooldown(this))
			return;

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// apply effect
		applyEffect(worldIn, (EntityLivingBase) entityIn);

	}

	/**
	 * Returns true if item is in user hotbar.
	 * 
	 * @param itemSlot
	 *            user item slot. the hotbar is between 0 and 8 inclusive.
	 * 
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
	 * Apply effect to creatures within range.
	 * 
	 * @param world
	 *            world object
	 * @param invokingEntity
	 *            entity object
	 */
	void applyEffect(World world, EntityLivingBase invokingEntity) {
		int aoeRange = strategy.getEffectRange();

		// get entities within AABB
		AxisAlignedBB aabb = new AxisAlignedBB(invokingEntity.posX - aoeRange, invokingEntity.posY - aoeRange,
				invokingEntity.posZ - aoeRange, invokingEntity.posX + aoeRange, invokingEntity.posY + aoeRange,
				invokingEntity.posZ + aoeRange);
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

		for (EntityLivingBase foundEntity : entities) {

			// determine if target is invoker
			boolean isInvoker = hasIdenticalUniqueID(invokingEntity, foundEntity);

			// apply effect
			if (strategy.shouldApplyEffect(foundEntity, isInvoker)) {
				strategy.applyEffect(foundEntity, world);

				// render effect
				renderEffect(foundEntity.getPositionVector());
			}
		}
	}

	/**
	 * Render a effect at some position.
	 * 
	 * @param position
	 *            effect position.
	 */
	void renderEffect(Vec3d position) {

		// register particle for rendering
		BlockPos pos = new BlockPos(position);

		// iterate over rendering info's
		for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {
			ParticleRendering particle = getInstance(pos, info);
			particleRepository.add(particle);
		}
	}

}
