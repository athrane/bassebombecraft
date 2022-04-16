package bassebombecraft.operator.projectile;

import static bassebombecraft.entity.RegisteredEntities.LIGHTNING_PROJECTILE;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.entity.projectile.LightningProjectileEntity;
import bassebombecraft.operator.Operator2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots composite
 * lightning projectile(s) from the invoker position.
 * 
 * Sub class of the {@linkplain GenericShootProjectile2} generic projectile
 * shooter operator.
 */
public class ShootLightningProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3 orientation) {
		GenericCompositeProjectileEntity projectile = new LightningProjectileEntity(LIGHTNING_PROJECTILE.get(), invoker);
		projectile.shootUsingProjectileConfig(orientation);
		return projectile;
	}

}
