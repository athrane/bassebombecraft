package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.setProjectileEntityPosition;
import static bassebombecraft.potion.MobEffects.CREEPER_CANNON_POTION;
import static bassebombecraft.potion.MobEffects.PRIMED_CREEPER_CANNON_POTION;

import java.util.Random;

import com.typesafe.config.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a
 * creeper.
 */
public class ShootCreeperCannon implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;

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
		EntityCreeper projectileEntity = new EntityCreeper(world);
		projectileEntity.copyLocationAndAnglesFrom(entity);

		// prime
		if (isPrimed)
			projectileEntity.ignite();

		// select potion
		Potion potion = null;
		if (isPrimed)
			potion = PRIMED_CREEPER_CANNON_POTION;
		else
			potion = CREEPER_CANNON_POTION;

		// calculate spawn projectile spawn position
		setProjectileEntityPosition(entity, projectileEntity, spawnDisplacement);

		// add potion effect
		PotionEffect effect = new PotionEffect(potion, duration);
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
