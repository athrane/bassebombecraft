package bassebombecraft.operator.projectile;

import static bassebombecraft.event.projectile.RegisteredEntityTypes.SKULL_PROJECTILE;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.entity.projectile.SkullProjectileEntity;
import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots composite
 * skull projectile(s) from the invoker position.
 * 
 * Sub class of the {@linkplain GenericShootProjectile2} generic projectile
 * shooter operator.
 */
public class ShootSkullProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3d orientation) {
		GenericCompositeProjectileEntity projectile = new SkullProjectileEntity(SKULL_PROJECTILE, invoker);
		projectile.doShoot(orientation);
		return projectile;
	}

}
