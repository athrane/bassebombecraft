package bassebombecraft.event.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.BLOCKS_PER_TICK;
import static bassebombecraft.block.BlockUtils.createBlock;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.config.ModConfiguration.spawnedBlockParticles;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.network.NetworkChannelHelper;
import bassebombecraft.network.packet.AddParticleRendering;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.client.rendering.AddParticlesFromPosAtClient2;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for processing of {@linkplain BlockDirective}.
 * 
 * The handler only executes events SERVER side.
 * 
 * When a directive is processed, a particle is registered for rendering using
 * {@linkplain NetworkChannelHelper} to send a {@linkplain AddParticleRendering}
 * packet to the client.
 */
@Mod.EventBusSubscriber
public class ProcessBlockDirectivesEventHandler {

	/**
	 * Operator for spawning particles when processing a block directive .
	 */
	static Operator2 particlesOp = new AddParticlesFromPosAtClient2(createFromConfig(spawnedBlockParticles));

	@SubscribeEvent
	public static void handleServerTickEvent(ServerTickEvent event) {

		try {

			// get repository
			BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();

			// exit if no directives are waiting to be processed.
			if (repository.isEmpty())
				return;

			// process directives
			for (int i = 0; i < BLOCKS_PER_TICK; i++) {
				processDirective();
			}

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}

	/**
	 * Process directive if an air directive is encountered then the directive is
	 * skipped and another one is processed.
	 * 
	 * @throws exception if directive processing fails.
	 */
	static void processDirective() throws Exception {

		// get repositories
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();

		while (!repository.isEmpty()) {

			// get directive
			BlockDirective directive = repository.getNext();

			// get world
			World world = directive.getWorld();

			// skip if source and target states are equal
			BlockState currentState = world.getBlockState(directive.getBlockPosition());
			if (currentState.equals(directive.getState()))
				continue;

			// process directive
			createBlock(directive);

			// send particle for rendering to client
			BlockPos pos = directive.getBlockPosition();
			Ports ports = getInstance().setBlockPosition1(pos);
			run(ports, particlesOp);
		}

	}

}
