package bassebombecraft.rendering;

import static bassebombecraft.player.PlayerUtils.getPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RayTraceResult;

/**
 * Implementation of the {@linkplain RenderingInfo} interface.
 */
public class DefaultRenderingInfo implements RenderingInfo {

	/**
	 * Partial ticks.
	 */
	final float partialTicks;

	/**
	 * Render view entity X coordinate, modified with partial ticks.
	 */
	final double rveModifiedX;

	/**
	 * Render view entity Y coordinate, modified with partial ticks.
	 */
	final double rveModifiedY;

	/**
	 * Render view entity Z coordinate, modified with partial ticks.
	 */
	final double rveModifiedZ;

	/**
	 * Ray tracing result. Can be null.
	 */
	RayTraceResult result;

	public DefaultRenderingInfo(float partialTicks) {
		this.partialTicks = partialTicks;
		Minecraft mcClient = Minecraft.getInstance();
		Entity rve = mcClient.getRenderViewEntity();
		rveModifiedX = rve.lastTickPosX + ((rve.getPosX() - rve.lastTickPosX) * partialTicks);
		rveModifiedY = rve.lastTickPosY + ((rve.getPosY() - rve.lastTickPosY) * partialTicks);
		rveModifiedZ = rve.lastTickPosZ + ((rve.getPosZ() - rve.lastTickPosZ) * partialTicks);
	}

	public DefaultRenderingInfo(float partialTicks, RayTraceResult result) {
		this(partialTicks);
		this.result = result;
	}

	@Override
	public double getRveTranslatedViewX() {
		return -rveModifiedX;
	}

	@Override
	public double getRveTranslatedViewY() {
		return -rveModifiedY;
	}

	@Override
	public double getRveTranslatedViewYOffsetWithPlayerEyeHeight() {
		PlayerEntity player = getPlayer();
		return getRveTranslatedViewY() - player.getEyeHeight();
	}

	@Override
	public double getRveTranslatedViewZ() {
		return -rveModifiedZ;
	}

	@Override
	public float getPartialTicks() {
		return partialTicks;
	}

	@Override
	public RayTraceResult getResult() {
		return result;
	}

	@Override
	public boolean isRayTraceResultDefined() {
		return (result != null);
	}

	/**
	 * Factory method.
	 * 
	 * @param partialTicks partial game ticks.
	 * 
	 * @return rendering info.
	 */
	public static RenderingInfo getInstance(float partialTicks) {
		return new DefaultRenderingInfo(partialTicks);
	}

	/**
	 * Factory method.
	 * 
	 * @param partialTicks partial game ticks.
	 * @param result       ray trace result .
	 * 
	 * @return rendering info.
	 */
	public static RenderingInfo getInstance(float partialTicks, RayTraceResult result) {
		return new DefaultRenderingInfo(partialTicks, result);
	}

}
