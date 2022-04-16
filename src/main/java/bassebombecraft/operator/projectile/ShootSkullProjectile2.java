package bassebombecraft.operator.projectile;

import static bassebombecraft.entity.RegisteredEntities.SKULL_PROJECTILE;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.entity.projectile.SkullProjectileEntity;
import bassebombecraft.operator.Operator2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots composite
 * skull projectile(s) from the invoker position.
 * 
 * Sub class of the {@linkplain GenericShootProjectile2} generic projectile
 * shooter operator.
 */
public class ShootSkullProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3 orientation) {
		GenericCompositeProjectileEntity projectile = new SkullProjectileEntity(SKULL_PROJECTILE.get(), invoker);
		projectile.shootUsingProjectileConfig(orientation);
		return projectile;
	}

}
