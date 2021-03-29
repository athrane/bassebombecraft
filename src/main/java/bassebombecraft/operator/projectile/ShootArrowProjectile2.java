package bassebombecraft.operator.projectile;

import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.vector.Vector3d;

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
	Entity createProjectile(LivingEntity invoker, Vector3d orientation) {

		ArrowEntity projectile = EntityType.ARROW.create(invoker.getEntityWorld());
		projectile.setShooter(invoker);
		projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		float velocity = PROJECTILE_FORCE * (float) orientation.length();
		projectile.shoot(orientation.getX(), orientation.getY(), orientation.getZ(), velocity, PROJECTILE_INACCURACY);

		return projectile;
	}

}
