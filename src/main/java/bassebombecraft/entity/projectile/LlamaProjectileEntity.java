package bassebombecraft.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Llama projectile for composite magic.
 */
public class LlamaProjectileEntity extends GenericCompositeProjectileEntity {

	/**
	 * Entity identifier.
	 */
	public static final String NAME = LlamaProjectileEntity.class.getSimpleName();

	/**
	 * Constructor
	 * 
	 * @param type  entity type.
	 * @param world world object.
	 */
	public LlamaProjectileEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 * @param world   world object.
	 */
	public LlamaProjectileEntity(EntityType<?> type, LivingEntity invoker, World world) {
		super(type, invoker, world);
	}

}
