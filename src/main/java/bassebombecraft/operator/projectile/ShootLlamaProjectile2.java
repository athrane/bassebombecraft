package bassebombecraft.operator.projectile;

import static bassebombecraft.event.projectile.RegisteredEntityTypes.LLAMA_PROJECTILE;

import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.entity.projectile.LlamaProjectileEntity;
import bassebombecraft.operator.Operator2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots composite
 * llama projectile(s) from the invoker position.
 * 
 * Sub class of the {@linkplain GenericShootProjectile2} generic projectile
 * shooter operator.
 */
public class ShootLlamaProjectile2 extends GenericShootProjectile2 {

	@Override
	Entity createProjectile(LivingEntity invoker, Vector3d orientation) {
		GenericCompositeProjectileEntity projectile = new LlamaProjectileEntity(LLAMA_PROJECTILE, invoker);
		projectile.shootUsingProjectileConfig(orientation);
		return projectile;
	}

}
