package bassebombecraft.event.projectile;

import static bassebombecraft.ModConstants.*;
import bassebombecraft.projectile.CompositeProjectileEntity;
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
	
}
