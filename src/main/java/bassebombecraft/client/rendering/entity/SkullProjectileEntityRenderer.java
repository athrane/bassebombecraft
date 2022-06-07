package bassebombecraft.client.rendering.entity;

import static bassebombecraft.client.rendering.RenderingUtils.createEntityTextureResourceLocation;

import bassebombecraft.entity.projectile.SkullProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Client side renderer for projectile {@linkplain SkullProjectileEntity}.
 */
public class SkullProjectileEntityRenderer extends GenericCompositeProjectileEntityRenderer<SkullProjectileEntity> {

	/**
	 * Path to texture.
	 */
	static final ResourceLocation TEXTURE = createEntityTextureResourceLocation(SkullProjectileEntity.class);

	/**
	 * Constructor
	 * 
	 * @param renderer context context.
	 */
	public SkullProjectileEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(SkullProjectileEntity entity) {
		return TEXTURE;
	}

}
