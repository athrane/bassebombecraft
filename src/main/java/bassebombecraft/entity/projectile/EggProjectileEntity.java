package bassebombecraft.entity.projectile;

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
	public EggProjectileEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 * @param world   world object.
	 */
	public EggProjectileEntity(EntityType<?> type, LivingEntity invoker, World world) {
		super(type, invoker, world);
	}

}
