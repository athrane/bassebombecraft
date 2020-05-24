package bassebombecraft.event.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.BLOCKS_PER_TICK;
import static bassebombecraft.block.BlockUtils.createBlock;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.network.NetworkChannelHelper;
import bassebombecraft.network.packet.AddParticleRendering;
import net.minecraft.block.BlockState;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
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

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 5;
	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.EFFECT;
	static final int PARTICLE_DURATION = 20;
	static final double PARTICLE_SPEED = 3.0D; // Particle speed
	static final ParticleRenderingInfo PARTICLE_INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R,
			G, B, PARTICLE_SPEED);

	static final BlockPos NULL_POSITION = null; // NULL block position.

	@SubscribeEvent
	public static void handleServerTickEvent(ServerTickEvent event) {

		// get repository
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();

		// exit if no directives are waiting to be processed.
		if (repository.isEmpty())
			return;

		// process directives
		for (int i = 0; i < BLOCKS_PER_TICK; i++) {
			processDirective();
		}
	}

	/**
	 * Process directive if an air directive is encountered then the directive is
	 * skipped and another one is processed.
	 */
	static void processDirective() {
		try {
			// get repositories
			BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();

			while (!repository.isEmpty()) {

				// get directive
				BlockDirective directive = repository.getNext();

				// get world
				World world = directive.getWorld();

				// skip if source and target states are both air
				BlockState currentState = world.getBlockState(directive.getBlockPosition());
				if (currentState.equals(directive.getState()))
					continue;

				// process directive
				createBlock(directive);

				// send particle for rendering to client
				BlockPos pos = directive.getBlockPosition();
				ParticleRendering particle = getInstance(pos, PARTICLE_INFO);
				getProxy().getNetworkChannel(world).sendAddParticleRenderingPacket(particle);
			}

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}

	}

}
