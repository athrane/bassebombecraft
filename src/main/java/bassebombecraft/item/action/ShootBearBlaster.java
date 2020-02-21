package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;

import java.util.Random;

import bassebombecraft.ModConstants;
import bassebombecraft.config.ModConfiguration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

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
	public void onRightClick(World world, LivingEntity entity) {

		// create projectile entity
		PolarBearEntity projectileEntity = EntityType.POLAR_BEAR.create(world);
		projectileEntity.setGrowingAge(age);
		projectileEntity.copyLocationAndAnglesFrom(entity);

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		EffectInstance effect = new EffectInstance(ModConstants.BEAR_BLASTER_EFFECT, duration);
		projectileEntity.addPotionEffect(effect);

		// set no health to trigger death (in max 20 ticks)
		if (isDead)
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
