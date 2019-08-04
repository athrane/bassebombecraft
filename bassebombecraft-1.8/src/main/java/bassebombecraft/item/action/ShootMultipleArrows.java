package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import bassebombecraft.geom.GeometryUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoots five
 * arrows.
 */
public class ShootMultipleArrows implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_SKELETON_SHOOT;
	static final float INACCURACY = 1.0F;
	static final int ROTATE_DEGREES_M2 = -6;
	static final int ROTATE_DEGREES_M1 = -3;
	static final int ROTATE_DEGREES_1 = 3;
	static final int ROTATE_DEGREES_2 = 6;
	static final float FORCE_MODIFIER = 1.5F;
	static final float ARROW_FORCE = 1.5F;

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		Vec3d playerLook = entity.getLook(1);

		Random random = getBassebombeCraft().getRandom();

		ArrowEntity projectile = EntityType.ARROW.create(world);
		entity.playSound(SOUND, 1.0F, 1.0F / random.nextFloat() * 0.4F + 0.8F);
		world.addEntity(projectile);

		// rotate player look vector and create new rotated arrow
		Vec3d orientation = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(ROTATE_DEGREES_M2, playerLook);
		spawnArrow(world, entity, orientation);

		orientation = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(ROTATE_DEGREES_M1, playerLook);
		spawnArrow(world, entity, orientation);

		orientation = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(ROTATE_DEGREES_1, playerLook);
		spawnArrow(world, entity, orientation);

		orientation = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(ROTATE_DEGREES_2, playerLook);
		spawnArrow(world, entity, orientation);
	}

	/**
	 * Spawn an arrow.
	 * 
	 * @param world       world object
	 * @param entity      entity object.
	 * @param orientation modified entity orientation for the direction of the
	 *                    arrow.
	 */
	void spawnArrow(World world, LivingEntity entity, Vec3d orientation) {
		Random random = getBassebombeCraft().getRandom();

		ArrowEntity projectile = EntityType.ARROW.create(world);
		float velocity = ARROW_FORCE * FORCE_MODIFIER;
		projectile.shoot(orientation.x, orientation.y, orientation.z, velocity, INACCURACY);
		entity.playSound(SOUND, 1.0F, 1.0F / random.nextFloat() * 0.4F + 0.8F);
		world.addEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
