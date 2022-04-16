package bassebombecraft.client.rendering.entity;

import static bassebombecraft.client.rendering.RenderingUtils.createEntityTextureResourceLocation;

import bassebombecraft.entity.projectile.EggProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;

/**
 * Client side renderer for projectile {@linkplain EggProjectileEntity}.
 */
public class EggProjectileEntityRenderer extends GenericCompositeProjectileEntityRenderer<EggProjectileEntity> {

	/**
	 * Path to texture.
	 */
	static final ResourceLocation TEXTURE = createEntityTextureResourceLocation(EggProjectileEntity.class);

	/**
	 * Constructor
	 * 
	 * @param renderManager render manager.
	 */
	public EggProjectileEntityRenderer(EntityRenderDispatcher renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(EggProjectileEntity entity) {
		return TEXTURE;
	}

}
