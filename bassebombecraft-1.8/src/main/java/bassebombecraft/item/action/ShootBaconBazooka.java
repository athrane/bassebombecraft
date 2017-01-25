package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.potion.MobEffects.BACON_BAZOOKA_POTION;

import java.util.Random;

import com.typesafe.config.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a bacon
 * bazooka.
 */
public class ShootBaconBazooka implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;

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

	long lastTime;

	public ShootBaconBazooka() {
		super();
		Config configuration = getBassebombeCraft().getConfiguration();
		age = configuration.getInt("ShootBaconBazooka.Age");
		duration = configuration.getInt("ShootBaconBazooka.Duration");
		spawnDisplacement = configuration.getInt("ShootBaconBazooka.SpawnDisplacement");
		lastTime = System.currentTimeMillis();
	}

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {

		//long time = System.currentTimeMillis();
		//long delta = time - lastTime;		
		//lastTime = System.currentTimeMillis();
		//System.out.println("delta="+delta);
		//if(delta < 2000) return;
		//System.out.println("lastime="+lastTime);
		
		Vec3d lookVec = entity.getLookVec();

		// get random
		Random random = entity.getRNG();

		EntityPig projectileEntity = new EntityPig(world);
		projectileEntity.setGrowingAge(age);
		projectileEntity.copyLocationAndAnglesFrom(entity);

		// calculate spawn projectile spawn position
		double x = entity.posX + (lookVec.xCoord * spawnDisplacement);
		double y = entity.posY + entity.getEyeHeight();
		double z = entity.posZ + (lookVec.zCoord * spawnDisplacement);

		// set spawn position
		projectileEntity.posX = x;
		projectileEntity.posY = y;
		projectileEntity.posZ = z;
		projectileEntity.prevRotationYaw = projectileEntity.rotationYaw = projectileEntity.rotationYawHead = entity.rotationYaw;
		projectileEntity.prevRotationPitch = projectileEntity.rotationPitch = entity.rotationPitch;

		// add potion effect
		PotionEffect effect = new PotionEffect(BACON_BAZOOKA_POTION, duration);
		projectileEntity.addPotionEffect(effect);

		// set no health to trigger death (in max 20 ticks)
		projectileEntity.setHealth(0.0F);

		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.spawnEntity(projectileEntity);
		
		
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}
}
