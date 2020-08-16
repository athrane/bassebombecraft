package bassebombecraft.event.projectile;

import static bassebombecraft.ModConstants.*;

import bassebombecraft.entity.projectile.CompositeProjectileEntity;
import bassebombecraft.entity.projectile.LightningProjectileEntity;
import bassebombecraft.entity.projectile.LlamaProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holders for registered entity types.
 * 
 * Entity types are registered in {@linkplain EntityTypeRegistryEventHandler}.
 */
@ObjectHolder(MODID)
public class RegisteredEntityTypes {
	
	/**
	 * Object holder for {@linkplain CompositeProjectileEntity}.
	 */
	@ObjectHolder("compositeprojectileentity")
	public static final EntityType<CompositeProjectileEntity> COMPOSITE_PROJECTILE = null;

	/**
	 * Object holder for {@linkplain LlamaProjectileEntity}.
	 */
	@ObjectHolder("llamaprojectileentity")
	public static final EntityType<LlamaProjectileEntity> LLAMA_PROJECTILE = null;

	/**
	 * Object holder for {@linkplain LightningProjectileEntity}.
	 */
	@ObjectHolder("lightningprojectileentity")
	public static final EntityType<LightningProjectileEntity> LIGHTNING_PROJECTILE = null;
	
}
