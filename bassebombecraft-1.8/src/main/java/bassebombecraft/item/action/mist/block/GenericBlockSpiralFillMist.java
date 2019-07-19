package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;

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
	 * Rendering frequency in ticks.
	 */
	static final int RENDERING_FREQUENCY = 5;

	/**
	 * Effect frequency when targeted mob are affect by most. Frequency is
	 * measured in ticks.
	 */
	static final int EFFECT_UPDATE_FREQUENCY = 5;

	/**
	 * Spawn distance of mist from invoker. Distance is measured in blocks.
	 */
	static final float INVOCATION_DIST = 4;

	/**
	 * Spiral size.
	 */
	static final int SPIRAL_SIZE = 20;

	/**
	 * Ticks counter.
	 */
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
	 * Particle repository
	 */
	ParticleRenderingRepository particleRepository;

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
	 * GenericBlockMist constructor.
	 * 
	 * @param strategy
	 *            mist strategy.
	 */
	public GenericBlockSpiralFillMist(BlockMistActionStrategy strategy) {
		this.strategy = strategy;
		particleRepository = getBassebombeCraft().getParticleRenderingRepository();

		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(SPIRAL_SIZE, SPIRAL_SIZE);
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

		// render mist
		if (ticksCounter % RENDERING_FREQUENCY == 0) {
			render(worldIn);
		}

		// update game effect
		if (ticksCounter % EFFECT_UPDATE_FREQUENCY == 0) {
			applyEffect(worldIn);
		}

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
	 * @param world
	 *            world object.
	 * @param entity
	 *            entity object
	 */
	void initializeMistPostition(World world, LivingEntity entity) {
		spiralCounter = strategy.getSpiralOffset();
		spiralCenter = new BlockPos(entity);
	}

	/**
	 * Apply effect to block.
	 * 
	 * @param world
	 *            world object
	 */
	void applyEffect(World world) {
		strategy.applyEffectToBlock(mistPosition, world);
	}

	/**
	 * Render mist in world.
	 * 
	 * @param world
	 *            world object.
	 */
	void render(World world) {
		updateMistPosition(world);

		// render mists
		// iterate over rendering info's
		for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {
			ParticleRendering particle = getInstance(mistPosition, info);
			particleRepository.add(particle);
		}
	}

	/**
	 * Update mist positions.
	 * 
	 * @param world
	 *            world object.
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
