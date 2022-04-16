package bassebombecraft.operator.projectile;

import bassebombecraft.operator.Operator2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots arrow
 * projectile(s) from the invoker position.
 */
public class ShootArrowProjectile2 extends GenericShootProjectile2 {

	/**
	 * Projectile inaccuracy.
	 */
	static final float PROJECTILE_INACCURACY = 1.0F;

	/**
	 * Projectile force.
	 */
	static final float PROJECTILE_FORCE = 15F;

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3 orientation) {

		Arrow projectile = EntityType.ARROW.create(invoker.getCommandSenderWorld());
		projectile.setOwner(invoker);
		projectile.setPos(invoker.getX(), invoker.getY() + invoker.getEyeHeight(), invoker.getZ());
		float velocity = PROJECTILE_FORCE * (float) orientation.length();
		projectile.shoot(orientation.x(), orientation.y(), orientation.z(), velocity, PROJECTILE_INACCURACY);

		return projectile;
	}

}
