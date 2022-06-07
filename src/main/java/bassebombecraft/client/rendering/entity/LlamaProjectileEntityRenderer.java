package bassebombecraft.client.rendering.entity;

import static bassebombecraft.client.rendering.RenderingUtils.createEntityTextureResourceLocation;

import bassebombecraft.entity.projectile.LlamaProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Client side renderer for projectile {@linkplain LlamaProjectileEntity}.
 */
public class LlamaProjectileEntityRenderer extends GenericCompositeProjectileEntityRenderer<LlamaProjectileEntity> {

	/**
	 * Path to texture.
	 */
	static final ResourceLocation TEXTURE = createEntityTextureResourceLocation(LlamaProjectileEntity.class);

	/**
	 * Constructor
	 * 
	 * @param renderer context context.
	 */
	public LlamaProjectileEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(LlamaProjectileEntity entity) {
		return TEXTURE;
	}

}
