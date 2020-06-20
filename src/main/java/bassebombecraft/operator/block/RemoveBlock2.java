package bassebombecraft.operator.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.geom.BlockDirective.getInstance;

import java.util.function.Function;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which removes block.
 */
public class RemoveBlock2 implements Operator2 {

	/**
	 * Function to get block position from ports.
	 * 
	 * Contains the centre of the spiral.
	 */
	Function<Ports, BlockPos> fnGetBlockPos;

	/**
	 * Constructor.
	 * 
	 * @param fnBlockPos function to get block position where block should be
	 *                   placed.
	 */
	public RemoveBlock2(Function<Ports, BlockPos> fnGetBlockPos) {
		this.fnGetBlockPos = fnGetBlockPos;
	}

	@Override
	public Ports run(Ports ports) {
		// get position
		BlockPos groundPosition = fnGetBlockPos.apply(ports);

		// get world
		World world = ports.getWorld();

		// remove block
		BlockDirective directive = getInstance(groundPosition, Blocks.AIR, DONT_HARVEST, world);
		getProxy().getServerBlockDirectivesRepository().add(directive);
		return ports;
	}

}
