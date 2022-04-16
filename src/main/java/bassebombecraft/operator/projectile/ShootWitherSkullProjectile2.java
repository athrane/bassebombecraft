package bassebombecraft.operator.projectile;

import bassebombecraft.operator.Operator2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots wither
 * skull projectile(s) from the invoker position.
 * 
 * Sub class of the {@linkplain GenericShootProjectile2} generic projectile
 * shooter operator.
 */
public class ShootWitherSkullProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3 orientation) {

		// create and spawn projectile
		AbstractHurtingProjectile projectile = EntityType.WITHER_SKULL.create(invoker.getCommandSenderWorld());
		projectile.setOwner(invoker);		
		projectile.setPos(invoker.getX(), invoker.getY() + invoker.getEyeHeight(), invoker.getZ());
		projectile.setDeltaMovement(orientation);

		// add acceleration to avoid glitches
		projectile.xPower = orientation.x;
		projectile.yPower = orientation.y;
		projectile.zPower = orientation.z;

		return projectile;
	}

}
