package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.block.BlockUtils.selectPinkColoredWool;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;
import java.util.Random;

import com.typesafe.config.Config;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.GeometryUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class pinkynizes the areas around the
 * invoker.
 */
public class Pinkynize implements InventoryItemActionStrategy {

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Spiral size.
	 */
	final int spiralSize;

	/**
	 * Random generator
	 */
	Random random = new Random();

	/**
	 * Block directives repository
	 */
	BlockDirectivesRepository directivesRepository;

	/**
	 * Current color counter.
	 */
	int colorCounter = 0;

	/**
	 * Spiral counter.
	 */
	int spiralCounter;

	/**
	 * Global centre of the spiral.
	 */
	BlockPos spiralCenter;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

	/**
	 * Pinkynize constructor
	 * 
	 * @param key
	 *            configuration key to initialize particle rendering info from.
	 */
	public Pinkynize(String key) {
		infos = createFromConfig(key);
		Config configuration = getBassebombeCraft().getConfiguration();
		spiralSize = configuration.getInt(key+".SpiralSize");

		directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();

		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(spiralSize, spiralSize);
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		return targetIsInvoker;
	}

	@Override
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
		colorCounter++;

		// calculate position
		BlockPos targetPosition = calculatePostion(target);

		// locate ground block
		BlockPos groundPosition = locateGroundBlockPos(targetPosition, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		// create wool block
		BlockState woolBlock = selectPinkColoredWool(colorCounter);		
		BlockDirective directive = new BlockDirective(groundPosition, woolBlock.getBlock(), DONT_HARVEST);
		directive.setState(selectPinkColoredWool(colorCounter));

		// create block
		BlockDirectivesRepository directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
		directivesRepository.add(directive);
	}

	@Override
	public int getEffectRange() {
		return 1; // Not a AOE effect
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

	/**
	 * Calculate target position in spiral.
	 * 
	 * @param target
	 *            target to calculate position for.
	 * 
	 * @return target position in spiral
	 */
	BlockPos calculatePostion(Entity target) {

		// initialize if no last position or a new position
		if (spiralCenter == null)
			initializeSpiral(target);
		if (!spiralCenter.equals(target.getPosition()))
			initializeSpiral(target);

		// exit if entire spiral is processed
		if (spiralCounter >= spiralCoordinates.size())
			return target.getPosition();

		// get next spiral coordinate
		BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);

		// calculate ground coordinates
		int x = spiralCenter.getX() + spiralCoord.getX();
		int y = spiralCenter.getY();
		int z = spiralCenter.getZ() + spiralCoord.getZ();
		BlockPos groundCandidate = new BlockPos(x, y, z);

		// update spiral
		spiralCounter++;

		return groundCandidate;
	}

	/**
	 * Initialize spiral
	 * 
	 * @param target
	 *            target to initialize spiral from from.
	 */
	void initializeSpiral(Entity target) {
		spiralCounter = 0;
		spiralCenter = new BlockPos(target);
	}

}
