package bassebombecraft.entity.projectile;

import static bassebombecraft.config.ModConfiguration.*;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Circle for composite magic.
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
	public CircleProjectileEntity(EntityType<?> type, World world) {
		super(type, world, circleProjectileEntity);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 */
	public CircleProjectileEntity(EntityType<?> type, LivingEntity invoker) {
		super(type, invoker, circleProjectileEntity);
	}

}
