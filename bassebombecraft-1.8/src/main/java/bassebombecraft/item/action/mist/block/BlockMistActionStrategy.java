package bassebombecraft.item.action.mist.block;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Strategy for block mist action.
 */
public interface BlockMistActionStrategy {

	/**
	 * Apply effect to target block.
	 * 
	 * @param target
	 *            target entity.
	 * @param world
	 *            world object.
	 */
	void applyEffectToBlock(BlockPos target, World world);

	/**
	 * Return effect duration.
	 * 
	 * @return effect duration.
	 */
	int getEffectDuration();

	/**
	 * Return the number of mists to spawn.
	 * 
	 * @return number of mists to spawn.
	 */
	int getNumberMists();

	/**
	 * Return the angle between mists if multiple mists are spawned.
	 * 
	 * @return angle between mists if multiple mists are spawned.
	 */
	double getMistAngle();

	/**
	 * Return true if mist is disabled when effect has been applied once.
	 * 
	 * @return true if mist is disabled when effect has been applied once.
	 */
	boolean isOneShootEffect();

	/**
	 * Get array of rendering info's used to render mist.
	 * 
	 * @return array of rendering info's used to render mist.
	 */
	ParticleRenderingInfo[] getRenderingInfos();

	/**
	 * Get spiral offset.
	 * 
	 * This property only makes sense of the strategy used by the
	 * {@linkplain GenericBlockSpiralFillMist}.
	 * 
	 * @return the spiral offset
	 */
	int getSpiralOffset();

}
