package bassebombecraft.entity.projectile;

import static bassebombecraft.config.ModConfiguration.eggProjectileEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Egg projectile for composite magic.
s */
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
	public EggProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, World world) {
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
