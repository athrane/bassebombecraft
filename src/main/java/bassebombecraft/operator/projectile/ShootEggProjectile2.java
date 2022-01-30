package bassebombecraft.operator.projectile;

import bassebombecraft.entity.projectile.EggProjectileEntity;
import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import static bassebombecraft.entity.RegisteredEntities.EGG_PROJECTILE;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots composite
 * egg projectile(s) from the invoker position.
 * 
 * Sub class of the {@linkplain GenericShootProjectile2} generic projectile
 * shooter operator.
 */
public class ShootEggProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vector3d orientation) {
		GenericCompositeProjectileEntity projectile = new EggProjectileEntity(EGG_PROJECTILE.get(), invoker);
		projectile.shootUsingProjectileConfig(orientation);
		return projectile;
	}

}
