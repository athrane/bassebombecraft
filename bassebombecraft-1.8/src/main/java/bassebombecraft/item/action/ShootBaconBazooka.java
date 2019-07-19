package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;
import static bassebombecraft.potion.MobEffects.BACON_BAZOOKA_POTION;

import java.util.Random;

import com.typesafe.config.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a bacon
 * bazooka.
 */
public class ShootBaconBazooka implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = ShootBaconBazooka.class.getSimpleName();

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
		Config configuration = getBassebombeCraft().getConfiguration();
		age = configuration.getInt(CONFIG_KEY + ".Age");
		duration = configuration.getInt(CONFIG_KEY + ".Duration");
		spawnDisplacement = configuration.getInt(CONFIG_KEY + ".SpawnDisplacement");
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {

		// create projectile entity
		EntityPig projectileEntity = new EntityPig(world);
		projectileEntity.setGrowingAge(age);
		projectileEntity.copyLocationAndAnglesFrom(entity);

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		PotionEffect effect = new PotionEffect(BACON_BAZOOKA_POTION, duration);
		projectileEntity.addPotionEffect(effect);

		// set no health to trigger death (in max 20 ticks)
		projectileEntity.setHealth(0.0F);

		// add spawn sound
		Random random = entity.getRNG();
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.spawnEntity(projectileEntity);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}
}
