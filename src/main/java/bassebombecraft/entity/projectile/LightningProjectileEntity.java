package bassebombecraft.entity.projectile;

import static bassebombecraft.config.ModConfiguration.lightningProjectileEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Lightning projectile for composite magic.
 */
public class LightningProjectileEntity extends GenericCompositeProjectileEntity {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = LightningProjectileEntity.class.getSimpleName();

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public LightningProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, World world) {
		super(type, world, lightningProjectileEntity);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 */
	public LightningProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, LivingEntity invoker) {
		super(type, invoker, lightningProjectileEntity);
	}

}
