package bassebombecraft.operator.projectile;

import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots wither
 * skull projectile(s) from the invoker position.
 */
public class ShootWitherSkullProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3d orientation) {

		// create and spawn projectile
		DamagingProjectileEntity projectile = EntityType.WITHER_SKULL.create(invoker.getEntityWorld());
		projectile.shootingEntity = invoker;		
		projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		projectile.setMotion(orientation);

		// add acceleration to avoid glitches
		projectile.accelerationX = orientation.x;
		projectile.accelerationY = orientation.y;
		projectile.accelerationZ = orientation.z;

		return projectile;
	}

}
