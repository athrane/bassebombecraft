package bassebombecraft.client.rendering.entity;

import static bassebombecraft.client.rendering.RenderingUtils.createEntityTextureResourceLocation;

import bassebombecraft.entity.projectile.EggProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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
	 * @param renderer context context.
	 */
	public EggProjectileEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(EggProjectileEntity entity) {
		return TEXTURE;
	}

}
