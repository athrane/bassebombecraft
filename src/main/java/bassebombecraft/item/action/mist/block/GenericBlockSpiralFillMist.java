package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.genericBlockSpiralFillMistSpiralSize;
import static bassebombecraft.operator.DefaultPorts.getBcSetBlockPosition2;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition2;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.event.job.Job;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.block.ApplyEffectFromMistStrategy2;
import bassebombecraft.operator.block.CalculateSpiralPosition2;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import bassebombecraft.operator.counter.SingleLoopIncreasingCounter2;
import bassebombecraft.operator.job.ExecuteOperatorAsJob2;
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
 * 
 * The operator is executed as a job.
 * 
 * A ports is created for each new job. The ports is configured the world, a
 * counter for the spiral offset and the block position of the invoker entity.
 */
public class GenericBlockSpiralFillMist implements RightClickedItemAction {

	/**
	 * Action identifier.
	 */
	public static final String NAME = GenericBlockSpiralFillMist.class.getSimpleName();

	/**
	 * Mist strategy.
	 */
	BlockMistActionStrategy strategy;

	/**
	 * Spiral size.
	 */
	int spiralSize;

	/**
	 * Operator for creation of spiral.
	 */
	Operator2 spiralOp;

	/**
	 * GenericBlockMist constructor.
	 * 
	 * @param strategy mist strategy.
	 */
	public GenericBlockSpiralFillMist(BlockMistActionStrategy strategy) {
		this.strategy = strategy;
		this.spiralSize = genericBlockSpiralFillMistSpiralSize.get();
		int numberSpiralBlocks = (spiralSize * spiralSize);

		/**
		 * create functions for accessing the ports:
		 * 
		 * 1) ports.blockpos#1 is used for the static spiral center
		 * 
		 * 2) ports.blockpos#2 is used for the calculated spiral block.
		 */
		Function<Ports, BlockPos> fnGetCenter = getFnGetBlockPosition1();
		BiConsumer<Ports, BlockPos> bcSetSpiralPos = getBcSetBlockPosition2();
		Function<Ports, BlockPos> fnGetSpiralPos = getFnGetBlockPosition2();
		Function<Ports, World> fnGetWorld = getFnWorld1();

		// create operators
		spiralOp = new Sequence2(new SingleLoopIncreasingCounter2(numberSpiralBlocks - 1),
				new CalculateSpiralPosition2(spiralSize, fnGetCenter, bcSetSpiralPos, fnGetWorld),
				new ApplyEffectFromMistStrategy2(strategy, fnGetSpiralPos, fnGetWorld),
				new AddParticlesFromPosAtClient2(strategy.getRenderingInfo(), fnGetSpiralPos));
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {

		// create ports
		Ports ports = getInstance();
		ports.setWorld(world);

		// set counter to exclude centre of spiral
		ports.setCounter(strategy.getSpiralOffset());

		// set spiral centre
		BlockPos center = new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ());		
		ports.setBlockPosition1(center);

		// create and register job
		Job job = new ExecuteOperatorAsJob2(ports, spiralOp);
		String id = new StringBuilder().append(entity.getEntityString()).append(strategy.toString()).toString();
		getProxy().getServerJobRepository().add(id, strategy.getEffectDuration(), job);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		// NO-OP, update is done in the job
	}

}
