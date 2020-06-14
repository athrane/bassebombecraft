package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.genericBlockSpiralFillMistSpiralSize;
import static bassebombecraft.geom.GeometryUtils.calculateSpiral;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.event.job.Job;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.block.ApplyEffectFromMistStrategy2;
import bassebombecraft.operator.block.CalculateSpiralPosition2;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import bassebombecraft.operator.counter.SingleLoopIncreasingCounter2;
import static bassebombecraft.operator.job.ExecuteOperatorAsJob.getInstance;
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

		// create ports
		Ports ports = getInstance();
		ports.setWorld(world);

		// set counter to exclude centre of spiral
		ports.setCounter(0);

		// set spiral centre
		ports.setBlockPosition1(new BlockPos(entity));
		
		// create functions for accessing the ports:
		// 1) ports.blockpos #1 is used for the static spiral center.
		// 2) ports.blockpos #2 is used for the calculated spiral block.
		Function<Ports, BlockPos> fnGetCenter = ports.getFnGetBlockPosition1();		
		BiConsumer<Ports, BlockPos> bcSetSpiralPos = ports.getBcSetBlockPosition2();		
		Function<Ports, BlockPos> fnGetSpiralPos = ports.getFnGetBlockPosition2();

		// create spiral operator
		Operator2[] ops = new Operator2[] { new SingleLoopIncreasingCounter2(spiralCoordinates.size()-1),
				new CalculateSpiralPosition2(spiralCoordinates, fnGetCenter, bcSetSpiralPos),
				new ApplyEffectFromMistStrategy2(strategy, fnGetSpiralPos),
				new AddParticlesFromPosAtClient2(strategy.getRenderingInfos(), fnGetSpiralPos) };

		// create and register job
		Job job = getInstance(ports, ops);
		String id = new StringBuilder().append(entity.getEntityString()).append(strategy.toString()).toString();
		getProxy().getServerJobRepository().add(id, strategy.getEffectDuration(), job);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		// NO-OP, update is done in the job
	}

}
