package bassebombecraft.item.action;

import java.util.Random;

import bassebombecraft.geom.GeometryUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoots five arrows.
 */
public class ShootMultipleArrows implements RightClickedItemAction {

	static final float INACCURACY = 1.0F;
	static final String SOUND = "mob.ghast.fireball";
	static final int ROTATE_DEGREES_M2 = -6;
	static final int ROTATE_DEGREES_M1 = -3;
	static final int ROTATE_DEGREES_1 = 3;
	static final int ROTATE_DEGREES_2 = 6;
	static final float FORCE_MODIFIER = 1.5F;
	static final float ARROW_FORCE = 1.5F;
	static Random random = new Random();

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		Vec3 playerLook = entity.getLook(1);

		EntityArrow projectile = new EntityArrow(world, entity, ARROW_FORCE);
		world.playSoundAtEntity(entity, SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		world.spawnEntityInWorld(projectile);

		// rotate player look vector and create new rotated arrow
		Vec3 orientation = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(ROTATE_DEGREES_M2, playerLook);
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
	 * @param world
	 *            world object
	 * @param entity
	 *            entity object.
	 * @param orientation
	 *            modified entity orientation for the direction of the arrow.
	 */
	void spawnArrow(World world, EntityLivingBase entity, Vec3 orientation) {
		EntityArrow projectile;
		projectile = new EntityArrow(world, entity, ARROW_FORCE);
		float velocity = ARROW_FORCE * FORCE_MODIFIER;
		projectile.setThrowableHeading(orientation.xCoord, orientation.yCoord, orientation.zCoord, velocity,
				INACCURACY);
		world.playSoundAtEntity(entity, SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		world.spawnEntityInWorld(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
