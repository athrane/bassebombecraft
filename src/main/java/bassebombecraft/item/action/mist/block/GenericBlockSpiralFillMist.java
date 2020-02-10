package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.*;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which creates one
 * or more mists with a custom effect when a mist passes a block.
 * 
 * The effect is implemented by the configured strategy
 * {@linkplain BlockMistActionStrategy}.
 */
public class GenericBlockSpiralFillMist implements RightClickedItemAction {

	/**
	 * Action identifier.
	 */
	public final static String NAME = GenericBlockSpiralFillMist.class.getSimpleName();

	/**
	 * Ticks counter.
	 */
	@Deprecated
	int ticksCounter = 0;

	/**
	 * Invoking entity.
	 */
	LivingEntity entity;

	/**
	 * Invoking entity look unit vector.
	 */
	Vec3d entityLook;

	/**
	 * Defines whether behaviour is active.
	 */
	boolean isActive = false;

	/**
	 * Mist strategy.
	 */
	BlockMistActionStrategy strategy;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

	/**
	 * Spiral counter.
	 */
	int spiralCounter;

	/**
	 * Global centre of the spiral.
	 */
	BlockPos spiralCenter;

	/**
	 * Current position in the mist.
	 */
	BlockPos mistPosition;

	/**
	 * Spiral size.
	 */
	int spiralSize;

	/**
	 * GenericBlockMist constructor.
	 * 
	 * @param strategy mist strategy.
	 */
	public GenericBlockSpiralFillMist(BlockMistActionStrategy strategy) {
		this.strategy = strategy;
		spiralSize = ModConfiguration.genericBlockSpiralFillMistSpiralSize.get();

		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(spiralSize, spiralSize);
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		this.entity = entity;
		isActive = true;
		ticksCounter = 0;
		initializeMistPostition(world, entity);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		// exit if mist isn't active
		if (!isActive())
			return;

		// render mist if frequency is active
		FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();
		if (repository.isActive(PARTICLE_RENDERING_FREQUENCY))
			render(worldIn);

		// update effect if frequency is active
		if (repository.isActive(BLOCK_EFFECT_FREQUENCY))
			applyEffect(worldIn);

		// disable if duration is completed
		if (ticksCounter > strategy.getEffectDuration()) {
			isActive = false;
			entity = null;
			return;
		}

		ticksCounter++;
	}

	/**
	 * Returns true if behaviour is active.
	 * 
	 * @return true if behaviour is active.
	 */
	boolean isActive() {
		return isActive;
	}

	/**
	 * Initialize mist position.
	 * 
	 * Mist is calculated as a spiral.
	 * 
	 * @param world  world object.
	 * @param entity entity object
	 */
	void initializeMistPostition(World world, LivingEntity entity) {
		spiralCounter = strategy.getSpiralOffset();
		spiralCenter = new BlockPos(entity);
	}

	/**
	 * Apply effect to block.
	 * 
	 * @param world world object
	 */
	void applyEffect(World world) {
		strategy.applyEffectToBlock(mistPosition, world);
	}

	/**
	 * Render mist in world.
	 * 
	 * @param world world object.
	 */
	void render(World world) {
		updateMistPosition(world);

		// render mists
		ParticleRenderingRepository repository = getBassebombeCraft().getParticleRenderingRepository();

		// iterate over rendering info's
		for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {
			ParticleRendering particle = getInstance(mistPosition, info);
			repository.add(particle);
		}
	}

	/**
	 * Update mist positions.
	 * 
	 * @param world world object.
	 */
	void updateMistPosition(World world) {

		// exit if entire spiral is processed
		if (spiralCounter >= spiralCoordinates.size())
			return;

		// get next spiral coordinate
		BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);

		// calculate ground coordinates
		int x = spiralCenter.getX() + spiralCoord.getX();
		int y = spiralCenter.getY();
		int z = spiralCenter.getZ() + spiralCoord.getZ();
		BlockPos groundCandidate = new BlockPos(x, y, z);

		// locate ground block
		mistPosition = locateGroundBlockPos(groundCandidate, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		spiralCounter++;
	}

	@Override
	public String toString() {
		return super.toString() + ", strategy=" + strategy;
	}

}
