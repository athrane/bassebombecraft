package bassebombecraft.item.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;
import static bassebombecraft.player.PlayerUtils.isItemHeldInOffHand;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.InventoryItemConfig;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.item.action.inventory.InventoryItemActionStrategy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when the item is in player hotbar.
 */
public class GenericInventoryItem extends Item {

	/**
	 * Item properties which places item in tab.
	 */
	public static final Properties ITEM_PROPERTIES = new Item.Properties().group(getItemGroup());

	/**
	 * Item strategy.
	 */
	InventoryItemActionStrategy strategy;

	/**
	 * Idol item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	String tooltip;

	/**
	 * Rendering infos.
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Effect range.
	 */
	int range;

	/**
	 * Constructor.
	 * 
	 * @param name     item name.
	 * @param config   inventory item configuration.
	 * @param strategy inventory item strategy.
	 */
	public GenericInventoryItem(String name, InventoryItemConfig config, InventoryItemActionStrategy strategy) {
		super(ITEM_PROPERTIES);
		doCommonItemInitialization(this, name);
		this.strategy = strategy;
		infos = createFromConfig(config.particles);
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
		range = config.range.get();
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		// only apply the action at server side since we updates the world
		if (isLogicalClient(worldIn))
			return;

		// exit if item isn't in hotbar
		if (!isInHotbar(itemSlot))
			return;

		// determine if item is held by player and should activate from off hand
		boolean shouldActivateFromOffHand = false;
		if (isTypePlayerEntity(entityIn)) {
			PlayerEntity player = (PlayerEntity) entityIn;
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

		// exit if entity isn't a LivingEntity
		if (!(isTypeLivingEntity(entityIn)))
			return;

		// exit if entity isn't player
		if (!isTypePlayerEntity(entityIn))
			return;

		// type cast as player
		PlayerEntity player = (PlayerEntity) entityIn;

		// exit if cooldown is effect
		if (player.getCooldownTracker().hasCooldown(this))
			return;

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), player.getGameProfile().getName());

		// apply effect
		applyEffect(worldIn, (LivingEntity) entityIn);
	}

	/**
	 * Returns true if item is in user hotbar.
	 * 
	 * @param itemSlot user item slot. the hotbar is between 0 and 8 inclusive.
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
	 * @param world          world object
	 * @param invokingEntity entity object
	 */
	void applyEffect(World world, LivingEntity invokingEntity) {

		// get entities within AABB
		AxisAlignedBB aabb = new AxisAlignedBB(invokingEntity.getPosX() - range, invokingEntity.getPosY() - range,
				invokingEntity.getPosZ() - range, invokingEntity.getPosX() + range, invokingEntity.getPosY() + range,
				invokingEntity.getPosZ() + range);
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		for (LivingEntity foundEntity : entities) {

			// determine if target is invoker
			boolean isInvoker = hasIdenticalUniqueID(invokingEntity, foundEntity);

			// apply and render effect
			if (strategy.shouldApplyEffect(foundEntity, isInvoker)) {

				strategy.applyEffect(foundEntity, world, invokingEntity);
				renderEffect(foundEntity.getPositionVector(), world);
			}
		}
	}

	/**
	 * Render a effect at some position.
	 * 
	 * @param position effect position.
	 * @param world    world object
	 */
	void renderEffect(Vec3d position, World world) {
		try {
			// create position
			BlockPos pos = new BlockPos(position);

			// iterate over rendering info's
			for (ParticleRenderingInfo info : infos) {
				// send particle rendering info to client
				ParticleRendering particle = getInstance(pos, info);
				getProxy().getNetworkChannel(world).sendAddParticleRenderingPacket(particle);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		ITextComponent text = new TranslationTextComponent(TextFormatting.GREEN + this.tooltip);
		tooltip.add(text);
	}

	/**
	 * Return cooldown value for item.
	 * 
	 * @return cooldown value for item.
	 */
	public int getCoolDown() {
		return coolDown;
	}

}
