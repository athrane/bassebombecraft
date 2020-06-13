package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.genericBlockSpiralFillMistSpiralSize;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.calculateSpiral;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;

import bassebombecraft.event.job.Job;
import bassebombecraft.event.job.JobRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
	 * Implementation of {@linkplain Job}.
	 */
	class GenericBlockSpiralFillMistJob implements Job {

		/**
		 * Spiral counter. Exclude the centre of the spiral
		 */
		int spiralCounter = 1;

		/**
		 * Global centre of the spiral.
		 */
		BlockPos spiralCenter;

		/**
		 * Current position in the mist.
		 */
		BlockPos mistPosition;

		/**
		 * World object.
		 */
		World world;

		/**
		 * Constructor.
		 * 
		 * @param spiralCenter global centre of the spiral.
		 * @param world        world object.
		 */
		public GenericBlockSpiralFillMistJob(BlockPos spiralCenter, World world) {
			this.spiralCenter = spiralCenter;
			this.world = world;
		}

		@Override
		public void update() {
			updateMistPosition();
			render();
			strategy.applyEffectToBlock(mistPosition, world);
		}

		@Override
		public void terminate() {
			// NO-OP
		}

		/**
		 * Update current position in the spiral.
		 */
		void updateMistPosition() {

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

		/**
		 * Render mist in world.
		 */
		void render() {
			// iterate over rendering info's
			for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {

				// send particle rendering info to client
				ParticleRendering particle = getInstance(mistPosition, info);
				getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);
			}
		}
	}

	/**
	 * Action identifier.
	 */
	public static final String NAME = GenericBlockSpiralFillMist.class.getSimpleName();

	/**
	 * Mist strategy.
	 */
	BlockMistActionStrategy strategy;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

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
		spiralSize = genericBlockSpiralFillMistSpiralSize.get();

		// calculate spiral
		spiralCoordinates = calculateSpiral(spiralSize, spiralSize);
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {

		// get ID for invocation
		String id = new StringBuilder().append(entity.getEntityString()).append(strategy.toString()).toString();

		// create job
		BlockPos spiralCenter = new BlockPos(entity);
		GenericBlockSpiralFillMistJob job = new GenericBlockSpiralFillMistJob(spiralCenter, world);

		// register job
		JobRepository jobRepository = getProxy().getServerJobRepository();
		jobRepository.add(id, strategy.getEffectDuration(), job);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		// NO-OP, update is done in the job
	}

}
