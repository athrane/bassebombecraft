package bassebombecraft.event.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.BLOCKS_PER_TICK;
import static bassebombecraft.block.BlockUtils.createBlock;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQueryImpl;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for processing of {@linkplain BlockDirective}.
 * 
 * Directives are only processed at server side.
 * 
 * When a directive is processed a particle is registered for rendering using
 * the {@linkplain ParticleRenderingRepository}.
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
	public static void handlePlayerTickEvent(PlayerTickEvent event) throws Exception {

		// get repository
		BlockDirectivesRepository repository = getBassebombeCraft().getBlockDirectivesRepository();

		// exit if no directives are waiting to be processed.
		if (!repository.containsDirectives())
			return;

		// get world
		PlayerEntity player = event.player;
		World world = player.getEntityWorld();

		// exit if at client side
		if (isWorldAtClientSide(world))
			return;

		// create world query
		WorldQueryImpl worldQuery = new WorldQueryImpl(player, NULL_POSITION);

		// process directives
		for (int i = 0; i < BLOCKS_PER_TICK; i++) {
			processDirective(world, worldQuery);
		}
	}

	/**
	 * Process directive if an air directive is encountered then the directive is
	 * skipped and another one is processed.
	 * 
	 * @param world
	 * @param worldQuery
	 * 
	 * @throws Exception if processing fails.
	 */
	static void processDirective(World world, WorldQueryImpl worldQuery) throws Exception {

		// get repositories
		BlockDirectivesRepository directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
		ParticleRenderingRepository particleRepository = getBassebombeCraft().getParticleRenderingRepository();

		while (directivesRepository.containsDirectives()) {

			// get directive
			BlockDirective directive = directivesRepository.getNext();

			// skip if source and target states are both air
			BlockState currentState = world.getBlockState(directive.getBlockPosition());
			if (currentState.equals(directive.getState()))
				continue;

			// process directive
			createBlock(directive, worldQuery);

			// register directive for particle rendering
			BlockPos pos = directive.getBlockPosition();
			ParticleRendering particle = getInstance(pos, PARTICLE_INFO);
			particleRepository.add(particle);
		}
	}

}
