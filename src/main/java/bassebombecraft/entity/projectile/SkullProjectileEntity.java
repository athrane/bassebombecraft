package bassebombecraft.entity.projectile;

import static bassebombecraft.config.ModConfiguration.skullProjectileEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Skull for composite magic.
 */
public class SkullProjectileEntity extends GenericCompositeProjectileEntity {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = SkullProjectileEntity.class.getSimpleName();

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public SkullProjectileEntity(EntityType<?> type, World world) {
		super(type, world, skullProjectileEntity);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 */
	public SkullProjectileEntity(EntityType<?> type, LivingEntity invoker) {
		super(type, invoker, skullProjectileEntity);
	}

}
