package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;
import static bassebombecraft.potion.effect.RegisteredEffects.CREEPER_CANNON_EFFECT;
import static bassebombecraft.potion.effect.RegisteredEffects.PRIMED_CREEPER_CANNON_EFFECT;

import java.util.Random;

import bassebombecraft.config.ModConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a
 * creeper.
 */
public class ShootCreeperCannon implements RightClickedItemAction {

	/**
	 * Action identifier.
	 */
	public static final String NAME = ShootCreeperCannon.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.EVOKER_CAST_SPELL;

	/**
	 * Defines whether creeper is primed.
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
	 */
	public ShootCreeperCannon(boolean isPrimed) {
		this.isPrimed = isPrimed;
		duration = ModConfiguration.shootCreeperCannonDuration.get();
		spawnDisplacement = ModConfiguration.shootCreeperCannonSpawnDisplacement.get();
	}

	@Override
	public void onRightClick(Level world, LivingEntity entity) {

		// create projectile entity
		Creeper projectileEntity = EntityType.CREEPER.create(world);
		projectileEntity.copyPosition(entity);

		// prime
		if (isPrimed)
			projectileEntity.ignite();

		// select potion
		MobEffect potion = null;
		if (isPrimed)
			potion = PRIMED_CREEPER_CANNON_EFFECT.get();
		else
			potion = CREEPER_CANNON_EFFECT.get();

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		MobEffectInstance effect = new MobEffectInstance(potion, duration);
		projectileEntity.addEffect(effect);

		// set no health to trigger death (in max 20 ticks)
		projectileEntity.setHealth(0.0F);

		// add spawn sound
		Random random = getBassebombeCraft().getRandom();
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addFreshEntity(projectileEntity);
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
