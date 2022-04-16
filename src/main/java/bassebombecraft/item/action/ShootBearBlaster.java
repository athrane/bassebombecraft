package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;
import static bassebombecraft.potion.effect.RegisteredEffects.BEAR_BLASTER_EFFECT;

import java.util.Random;

import bassebombecraft.config.ModConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a polar
 * bear.
 */
public class ShootBearBlaster implements RightClickedItemAction {

	/**
	 * Action identifier.
	 */
	public static final String NAME = ShootBearBlaster.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.EVOKER_CAST_SPELL;

	/**
	 * Pig age.
	 */
	final int age;

	/**
	 * Duration of the potion effect.
	 */
	final int duration;

	/**
	 * Spawn displacement in blocks.
	 */
	int spawnDisplacement;

	/**
	 * Is bear dead?
	 */
	boolean isDead;

	/**
	 * ShootBaconBazooka constructor.
	 */
	public ShootBearBlaster() {
		super();
		age = ModConfiguration.shootBearBlasterProjectileAge.get();
		duration = ModConfiguration.shootBearBlasterDuration.get();
		spawnDisplacement = ModConfiguration.shootBearBlasterSpawnDisplacement.get();
		isDead = true;
	}

	@Override
	public void onRightClick(Level world, LivingEntity entity) {

		// create projectile entity
		PolarBear projectileEntity = EntityType.POLAR_BEAR.create(world);
		projectileEntity.setAge(age);
		projectileEntity.copyPosition(entity);

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		MobEffectInstance effect = new MobEffectInstance(BEAR_BLASTER_EFFECT.get(), duration);
		projectileEntity.addEffect(effect);

		// set no health to trigger death (in max 20 ticks)
		if (isDead)
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
