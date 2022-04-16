package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ModConfiguration.shootBaconBazookaDuration;
import static bassebombecraft.config.ModConfiguration.shootBaconBazookaProjectileAge;
import static bassebombecraft.config.ModConfiguration.shootBaconBazookaSpawnDisplacement;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;
import static bassebombecraft.potion.effect.RegisteredEffects.BACON_BAZOOKA_EFFECT;

import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a bacon
 * bazooka.
 */
public class ShootBaconBazooka implements RightClickedItemAction {

	/**
	 * Action identifier.
	 */
	public static final String NAME = ShootBaconBazooka.class.getSimpleName();

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
	 * ShootBaconBazooka constructor.
	 */
	public ShootBaconBazooka() {
		super();
		age = shootBaconBazookaProjectileAge.get();
		duration = shootBaconBazookaDuration.get();
		spawnDisplacement = shootBaconBazookaSpawnDisplacement.get();
	}

	@Override
	public void onRightClick(Level world, LivingEntity entity) {

		// create projectile entity
		Pig projectileEntity = EntityType.PIG.create(world);
		projectileEntity.setAge(age);
		projectileEntity.copyPosition(entity);

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		MobEffectInstance effect = new MobEffectInstance(BACON_BAZOOKA_EFFECT.get(), duration);
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
