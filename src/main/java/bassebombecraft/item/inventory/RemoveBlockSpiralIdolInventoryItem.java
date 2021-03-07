package bassebombecraft.item.inventory;

import static bassebombecraft.config.ConfigUtils.*;
import static bassebombecraft.config.ModConfiguration.removeBlockSpiralIdolInventoryItem;
import static bassebombecraft.config.ModConfiguration.removeBlockSpiralIdolInventoryItemParticleInfo;
import static bassebombecraft.config.ModConfiguration.removeBlockSpiralIdolInventoryItemSpiralSize;
import static bassebombecraft.operator.DefaultPorts.getBcSetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.getBcSetBlockPosition2;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition2;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.item.action.inventory.ExecuteOperatorOnInvoker2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.block.CalculateSpiralPosition2;
import bassebombecraft.operator.block.RemoveBlock2;
import bassebombecraft.operator.block.ResetSpiralOnMovement2;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import bassebombecraft.operator.counter.SingleLoopIncreasingCounter2;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Remove block spiral idol implementation.
 */
public class RemoveBlockSpiralIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = RemoveBlockSpiralIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {

		// Read configuration values
		int spiralSize = removeBlockSpiralIdolInventoryItemSpiralSize.get();
		ParticleRenderingInfo info = createInfoFromConfig(removeBlockSpiralIdolInventoryItemParticleInfo);
		int numberSpiralBlocks = (spiralSize * spiralSize);

		/**
		 * create functions for accessing the ports:
		 * 
		 * 1) a function is defined for reading the invoker entity position, read from
		 * the ports.livingentity#1
		 * 
		 * 2) ports.blockpos#1 is used for the dynamic spiral centre.
		 * 
		 * 3) ports.blockpos#2 is used for the calculated spiral block.
		 */
		Function<Ports, BlockPos> fnGetInvokerPos = p -> p.getLivingEntity1().getPosition();
		Function<Ports, BlockPos> fnGetCenter = getFnGetBlockPosition1();
		BiConsumer<Ports, BlockPos> bcSetCenter = getBcSetBlockPosition1();
		Function<Ports, BlockPos> fnGetSpiralPos = getFnGetBlockPosition2();
		BiConsumer<Ports, BlockPos> bcSetSpiralPos = getBcSetBlockPosition2();
		Function<Ports, World> fnGetWorld = getFnWorld1();

		return new Sequence2(new ResetSpiralOnMovement2(1, fnGetInvokerPos, fnGetCenter, bcSetCenter),
				new SingleLoopIncreasingCounter2(numberSpiralBlocks - 1),
				new CalculateSpiralPosition2(spiralSize, fnGetCenter, bcSetSpiralPos, fnGetWorld),
				new RemoveBlock2(fnGetSpiralPos, fnGetWorld), new AddParticlesFromPosAtClient2(info, fnGetSpiralPos));
	};

	public RemoveBlockSpiralIdolInventoryItem() {
		super(removeBlockSpiralIdolInventoryItem, new ExecuteOperatorOnInvoker2(getInstance(), splOp.get()));
	}
}
