package bassebombecraft.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Prototype projectile
 * 
 * TODO: Add description.
 */
public class CompositeProjectileEntity extends GenericProjectileEntity {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = CompositeProjectileEntity.class.getSimpleName();

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public CompositeProjectileEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 * @param world   world object.
	 */
	public CompositeProjectileEntity(EntityType<?> type, LivingEntity invoker, World world) {
		super(type, invoker, world);
	}

}
