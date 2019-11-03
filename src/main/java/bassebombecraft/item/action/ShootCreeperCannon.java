package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;

import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.ModConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a
 * creeper.
 */
public class ShootCreeperCannon implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_EVOKER_CAST_SPELL;

	/**
	 * DEfines whether creeper is primed.
	 */
	boolean isPrimed;

	/**
	 * Duration of the potion effect.
	 */
	final int duration;

	/**
	 * Spawn displacement in blocks.
	 */
	int spawnDisplacement;

	/**
	 * ShootCreeperCannon constructor.
	 * 
	 * @param isPrimed defines whether creeper is primed.
	 * @param key      configuration key.
	 * 
	 */
	public ShootCreeperCannon(boolean isPrimed, String key) {
		this.isPrimed = isPrimed;
		Config configuration = getBassebombeCraft().getConfiguration();
		duration = configuration.getInt(key + ".Duration");
		spawnDisplacement = configuration.getInt(key + ".SpawnDisplacement");
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {

		// create projectile entity
		CreeperEntity projectileEntity = EntityType.CREEPER.create(world);
		projectileEntity.copyLocationAndAnglesFrom(entity);

		// prime
		if (isPrimed)
			projectileEntity.ignite();

		// select potion
		Effect potion = null;
		if (isPrimed)
			potion = ModConstants.PRIMED_CREEPER_CANNON_EFFECT;
		else
			potion = ModConstants.CREEPER_CANNON_EFFECT;

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		EffectInstance effect = new EffectInstance(potion, duration);
		projectileEntity.addPotionEffect(effect);

		// set no health to trigger death (in max 20 ticks)
		projectileEntity.setHealth(0.0F);

		// add spawn sound
		Random random = getBassebombeCraft().getRandom();
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(projectileEntity);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
