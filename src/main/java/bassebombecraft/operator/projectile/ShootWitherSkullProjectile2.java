package bassebombecraft.operator.projectile;

import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots wither
 * skull projectile(s) from the invoker position.
 * 
 * Sub class of the {@linkplain GenericShootProjectile2} generic projectile
 * shooter operator.
 */
public class ShootWitherSkullProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vector3d orientation) {

		// create and spawn projectile
		DamagingProjectileEntity projectile = EntityType.WITHER_SKULL.create(invoker.getEntityWorld());
		projectile.setShooter(invoker);		
		projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		projectile.setMotion(orientation);

		// add acceleration to avoid glitches
		projectile.accelerationX = orientation.x;
		projectile.accelerationY = orientation.y;
		projectile.accelerationZ = orientation.z;

		return projectile;
	}

}
