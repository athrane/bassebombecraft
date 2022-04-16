package bassebombecraft.entity.projectile;

import static bassebombecraft.config.ModConfiguration.eggProjectileEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Egg projectile for composite magic.
 */
public class EggProjectileEntity extends GenericCompositeProjectileEntity {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = EggProjectileEntity.class.getSimpleName();

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public EggProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, Level world) {
		super(type, world, eggProjectileEntity);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 */
	public EggProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, LivingEntity invoker) {
		super(type, invoker, eggProjectileEntity);
	}

}
