package bassebombecraft.entity.projectile;

import static bassebombecraft.config.ModConfiguration.circleProjectileEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Circle projectile for composite magic.
 */
public class CircleProjectileEntity extends GenericCompositeProjectileEntity {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = CircleProjectileEntity.class.getSimpleName();

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public CircleProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, Level world) {
		super(type, world, circleProjectileEntity);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 */
	public CircleProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, LivingEntity invoker) {
		super(type, invoker, circleProjectileEntity);
	}

}
