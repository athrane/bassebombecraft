package bassebombecraft.client.rendering.entity;

import static bassebombecraft.client.rendering.RenderingUtils.createEntityTextureResourceLocation;

import bassebombecraft.entity.projectile.LightningProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Client side renderer for projectile {@linkplain LightningProjectileEntity}.
 */
public class LightningProjectileEntityRenderer
		extends GenericCompositeProjectileEntityRenderer<LightningProjectileEntity> {

	/**
	 * Path to texture.
	 */
	static final ResourceLocation TEXTURE = createEntityTextureResourceLocation(LightningProjectileEntity.class);

	/**
	 * Constructor
	 * 
	 * @param renderer context context.
	 */
	public LightningProjectileEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(LightningProjectileEntity entity) {
		return TEXTURE;
	}

}
