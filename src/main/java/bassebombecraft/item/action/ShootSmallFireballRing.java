package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import bassebombecraft.config.ModConfiguration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
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
	 * Action identifier.
	 */
	public final static String NAME = ShootSmallFireballRing.class.getSimpleName();

	/**
	 * Initial vector.
	 */
	static final Vec3d INITIAL_VECTOR = new Vec3d(1, 0, 0);

	/**
	 * Sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_GHAST_SHOOT;

	/**
	 * Number fireballs.
	 */
	int number;

	/**
	 * ShootSmallFireballRing constructor
	 */
	public ShootSmallFireballRing() {
		number = ModConfiguration.shootSmallFireballRingFireballs.get();
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		int displacement = 360 / number;

		for (int index = 0; index < number; index++) {

			double yaw = (index * displacement) * 0.017453292F;
			Vec3d rotatedVec = INITIAL_VECTOR.rotateYaw((float) yaw);

			SmallFireballEntity projectile = EntityType.SMALL_FIREBALL.create(world);
			projectile.setPosition(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ());
			projectile.setMotion(rotatedVec);

			// add spawn sound
			Random random = getBassebombeCraft().getRandom();
			entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

			// spawn
			world.addEntity(projectile);
		}

	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
