package bassebombecraft.item.inventory;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ConfigUtils.createInfoFromConfig;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;
import static bassebombecraft.player.PlayerUtils.isItemHeldInOffHand;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.InventoryItemConfig;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.item.action.inventory.InventoryItemActionStrategy;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
	 * Idol item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	String tooltip;

	/**
	 * Effect range.
	 */
	int range;

	/**
	 * Projectile particle rendering ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports addParticlesPorts;

	/**
	 * Client side projectile generator operator.
	 */
	Operator2 addParticlesOp;

	/**
	 * Constructor.
	 * 
	 * @param config   inventory item configuration.
	 * @param strategy inventory item strategy.
	 */
	public GenericInventoryItem(InventoryItemConfig config, InventoryItemActionStrategy strategy) {
		super(new Item.Properties().tab(getItemGroup()));
		this.strategy = strategy;
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
		range = config.range.get();
		ParticleRenderingInfo info = createInfoFromConfig(config.particles);
		addParticlesOp = new AddParticlesFromPosAtClient2(info);
		addParticlesPorts = getInstance();
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		// only apply the action at server side since we updates the world
		if (isLogicalClient(worldIn))
			return;

		// exit if item isn't in hotbar
		if (!isInHotbar(itemSlot))
			return;

		// determine if item is held by player and should activate from off hand
		boolean shouldActivateFromOffHand = false;
		if (isTypePlayerEntity(entityIn)) {
			Player player = (Player) entityIn;
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
		Player player = (Player) entityIn;

		// exit if cooldown is effect
		if (player.getCooldowns().isOnCooldown(this))
			return;

		// add cooldown
		ItemCooldowns tracker = player.getCooldowns();
		tracker.addCooldown(this, coolDown);

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
	void applyEffect(Level world, LivingEntity invokingEntity) {

		// get entities within AABB
		AABB aabb = new AABB(invokingEntity.getX() - range, invokingEntity.getY() - range,
				invokingEntity.getZ() - range, invokingEntity.getX() + range, invokingEntity.getY() + range,
				invokingEntity.getZ() + range);
		List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, aabb);

		for (LivingEntity foundEntity : entities) {

			// determine if target is invoker
			boolean isInvoker = hasIdenticalUniqueID(invokingEntity, foundEntity);

			// apply and render effect
			if (strategy.shouldApplyEffect(foundEntity, isInvoker)) {
				strategy.applyEffect(foundEntity, world, invokingEntity);
				addParticles(foundEntity.position());
			}
		}
	}

	/**
	 * Add particle on update tick.
	 * 
	 * @param position effect position.
	 */
	void addParticles(Vec3 position) {
		BlockPos pos = new BlockPos(position);
		addParticlesPorts.setBlockPosition1(pos);
		run(addParticlesPorts, addParticlesOp);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		Component text = new TranslatableComponent(ChatFormatting.GREEN + this.tooltip);
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
