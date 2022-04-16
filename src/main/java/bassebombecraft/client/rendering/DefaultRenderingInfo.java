package bassebombecraft.client.rendering;

import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;

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
	HitResult result;

	public DefaultRenderingInfo(float partialTicks) {
		this.partialTicks = partialTicks;
		Minecraft mcClient = Minecraft.getInstance();
		Entity rve = mcClient.getCameraEntity();
		rveModifiedX = rve.xOld + ((rve.getX() - rve.xOld) * partialTicks);
		rveModifiedY = rve.yOld + ((rve.getY() - rve.yOld) * partialTicks);
		rveModifiedZ = rve.zOld + ((rve.getZ() - rve.zOld) * partialTicks);
	}

	public DefaultRenderingInfo(float partialTicks, HitResult result) {
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
		Player player = getClientSidePlayer();
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
	public HitResult getResult() {
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
	public static RenderingInfo getInstance(float partialTicks, HitResult result) {
		return new DefaultRenderingInfo(partialTicks, result);
	}

}
