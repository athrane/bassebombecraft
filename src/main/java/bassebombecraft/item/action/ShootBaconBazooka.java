package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ModConfiguration.shootBaconBazookaDuration;
import static bassebombecraft.config.ModConfiguration.shootBaconBazookaProjectileAge;
import static bassebombecraft.config.ModConfiguration.shootBaconBazookaSpawnDisplacement;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;

import java.util.Random;

import bassebombecraft.ModConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

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
	static final SoundEvent SOUND = SoundEvents.ENTITY_EVOKER_CAST_SPELL;

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
	public void onRightClick(World world, LivingEntity entity) {

		// create projectile entity
		PigEntity projectileEntity = EntityType.PIG.create(world);
		projectileEntity.setGrowingAge(age);
		projectileEntity.copyLocationAndAnglesFrom(entity);

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		EffectInstance effect = new EffectInstance(ModConstants.BACON_BAZOOKA_EFFECT, duration);
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
