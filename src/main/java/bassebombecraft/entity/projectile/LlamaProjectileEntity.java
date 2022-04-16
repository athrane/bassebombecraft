package bassebombecraft.entity.projectile;

import static bassebombecraft.config.ModConfiguration.llamaProjectileEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

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
	public LlamaProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, Level world) {
		super(type, world, llamaProjectileEntity);
	}

	/**
	 * Constructor
	 * 
	 * @param type    entity type.
	 * @param invoker projectile invoker.
	 */
	public LlamaProjectileEntity(EntityType<? extends GenericCompositeProjectileEntity> type, LivingEntity invoker) {
		super(type, invoker, llamaProjectileEntity);
	}
	
}
