package bassebombecraft.operator.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.geom.BlockDirective.getInstance;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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
	 * Function to get world from ports.
	 */
	Function<Ports, Level> fnGetWorld;

	/**
	 * Constructor.
	 * 
	 * @param fnBlockPos function to get block position where block should be
	 *                   removed.
	 * @param fnGetWorld function to get world.
	 */
	public RemoveBlock2(Function<Ports, BlockPos> fnGetBlockPos, Function<Ports, Level> fnGetWorld) {
		this.fnGetBlockPos = fnGetBlockPos;
		this.fnGetWorld = fnGetWorld;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with block position #1 (as position where block should
	 * be removed) from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 */
	public RemoveBlock2() {
		this(getFnGetBlockPosition1(), getFnWorld1());
	}

	@Override
	public void run(Ports ports) {
		BlockPos groundPosition = applyV(fnGetBlockPos, ports);
		Level world = applyV(fnGetWorld, ports);

		// remove block
		BlockDirective directive = getInstance(groundPosition, Blocks.AIR, DONT_HARVEST, world);
		getProxy().getServerBlockDirectivesRepository().add(directive);
	}

}
