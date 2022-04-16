package bassebombecraft.client.rendering.entity;

import static bassebombecraft.client.rendering.RenderingUtils.createEntityTextureResourceLocation;

import bassebombecraft.entity.projectile.CircleProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;

/**
 * Client side renderer for projectile {@linkplain CircleProjectileEntity}.
 */
public class CircleProjectileEntityRenderer extends GenericCompositeProjectileEntityRenderer<CircleProjectileEntity> {

	/**
	 * Path to texture.
	 */
	static final ResourceLocation TEXTURE = createEntityTextureResourceLocation(CircleProjectileEntity.class);

	/**
	 * Constructor
	 * 
	 * @param renderManager render manager.
	 */
	public CircleProjectileEntityRenderer(EntityRenderDispatcher renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getTextureLocation(CircleProjectileEntity entity) {
		return TEXTURE;
	}

}
