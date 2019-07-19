package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import com.typesafe.config.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a 360
 * degrees ring of small fireballs.
 */
public class ShootSmallFireballRing implements RightClickedItemAction {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = ShootSmallFireballRing.class.getSimpleName();

	/**
	 * Initial vector.
	 */
	static final Vec3d INITIAL_VECTOR = new Vec3d(1, 0, 0);

	static final SoundEvent SOUND = SoundEvents.ENTITY_GHAST_SHOOT;
	static Random random = new Random();

	/**
	 * Number fireballs.
	 */
	int number;

	/**
	 * ShootSmallFireballRing constructor
	 */
	public ShootSmallFireballRing() {
		Config configuration = getBassebombeCraft().getConfiguration();
		number = configuration.getInt(CONFIG_KEY + ".Number");
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		int displacement = 360 / number;

		for (int index = 0; index < number; index++) {

			double yaw = (index * displacement) * 0.017453292F;

			Vec3d rotatedVec = INITIAL_VECTOR.rotateYaw((float) yaw);

			EntitySmallFireball projectile = new EntitySmallFireball(world, entity.posX,
					entity.posY + entity.getEyeHeight(), entity.posZ, rotatedVec.x, rotatedVec.y, rotatedVec.z);
			projectile.shootingEntity = entity;
			entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);
			world.spawnEntity(projectile);
		}

	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
