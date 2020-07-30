package bassebombecraft.operator.projectile;

import bassebombecraft.event.projectile.RegisteredEntityTypes;
import bassebombecraft.operator.Operator2;
import bassebombecraft.projectile.CompositeProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots arrow
 * projectile(s) from the invoker position.
 */
public class ShootCompositieProjectile2 extends GenericShootProjectile2 {

	/**
	 * Projectile inaccuracy.
	 */
	static final float PROJECTILE_INACCURACY = 1.0F;

	/**
	 * Projectile force.
	 */
	static final float PROJECTILE_FORCE = 15F;

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3d orientation) {

		CompositeProjectileEntity projectile = new CompositeProjectileEntity(RegisteredEntityTypes.COMPOSITE_PROJECTILE, invoker,
				invoker.getEntityWorld());
		projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		float velocity = PROJECTILE_FORCE * (float) orientation.length();
		projectile.shoot(orientation.getX(), orientation.getY(), orientation.getZ(), velocity, PROJECTILE_INACCURACY);

		return projectile;
	}

}
