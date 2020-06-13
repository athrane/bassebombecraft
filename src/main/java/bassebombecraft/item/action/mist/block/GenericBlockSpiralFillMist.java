package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.genericBlockSpiralFillMistSpiralSize;
import static bassebombecraft.geom.GeometryUtils.calculateSpiral;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;

import java.util.List;
import java.util.function.Function;

import bassebombecraft.event.job.Job;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.block.mist.ApplyEffectFromMistStrategy2;
import bassebombecraft.operator.block.mist.CalculateSpiralPosition2;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import bassebombecraft.operator.conditional.IsSpiralNotCompleted2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which creates an
 * expanding spiral (mist) with a custom effect when the mist passes a block.
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
		 * World object.
		 */
		World world;

		/**
		 * Operator ports.
		 */
		Ports ports;

		/**
		 * Operators for the job.
		 */
		Operator2[] ops;

		/**
		 * Constructor.
		 * 
		 * @param world world object.
		 * @param ops   stateful operators for the job.
		 */
		public GenericBlockSpiralFillMistJob(World world, Operator2[] ops) {
			this.world = world;
			this.ops = ops;
			this.ports = getInstance();
		}

		@Override
		public void update() {
			ports.setWorld(world);
			run(ports, ops);
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
		this.spiralSize = genericBlockSpiralFillMistSpiralSize.get();
		this.spiralCoordinates = calculateSpiral(spiralSize, spiralSize);
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {

		// get spiral centre
		BlockPos center = new BlockPos(entity);

		// create spiral operator
		Function<Ports, BlockPos> fnBlockPos = p -> p.getBlockPosition();
		Operator2[] ops = new Operator2[] { new IsSpiralNotCompleted2(spiralCoordinates),
				new CalculateSpiralPosition2(spiralCoordinates, center),
				new ApplyEffectFromMistStrategy2(strategy, fnBlockPos),
				new AddParticlesFromPosAtClient2(strategy.getRenderingInfos(), fnBlockPos) };

		// create and register job
		GenericBlockSpiralFillMistJob job = new GenericBlockSpiralFillMistJob(world, ops);
		String id = new StringBuilder().append(entity.getEntityString()).append(strategy.toString()).toString();
		getProxy().getServerJobRepository().add(id, strategy.getEffectDuration(), job);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		// NO-OP, update is done in the job
	}

}
