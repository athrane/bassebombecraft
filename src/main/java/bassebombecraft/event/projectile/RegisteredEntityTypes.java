package bassebombecraft.event.projectile;

import static bassebombecraft.ModConstants.MODID;

import bassebombecraft.entity.projectile.CircleProjectileEntity;
import bassebombecraft.entity.projectile.LightningProjectileEntity;
import bassebombecraft.entity.projectile.SkullProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holders for registered entity types.
 * 
 * Entity types are registered in {@linkplain EntityTypeRegistryEventHandler}.
 */
@ObjectHolder(MODID)
@Deprecated
public class RegisteredEntityTypes {

	/**
	 * Object holder for {@linkplain LightningProjectileEntity}.
	 */
	@ObjectHolder("lightningprojectileentity")
	public static final EntityType<LightningProjectileEntity> LIGHTNING_PROJECTILE = null;

	/**
	 * Object holder for {@linkplain CircleProjectileEntity}.
	 */
	@ObjectHolder("circleprojectileentity")
	public static final EntityType<CircleProjectileEntity> CIRCLE_PROJECTILE = null;

	/**
	 * Object holder for {@linkplain SkullProjectileEntity}.
	 */
	@ObjectHolder("skullprojectileentity")
	public static final EntityType<SkullProjectileEntity> SKULL_PROJECTILE = null;

}
