package bassebombecraft.client.rendering.entity;

import static bassebombecraft.client.rendering.RenderingUtils.createEntityTextureResourceLocation;

import bassebombecraft.entity.projectile.LightningProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

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
	 * @param renderManager render manager.
	 */
	public LightningProjectileEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getEntityTexture(LightningProjectileEntity entity) {
		return TEXTURE;
	}

}
