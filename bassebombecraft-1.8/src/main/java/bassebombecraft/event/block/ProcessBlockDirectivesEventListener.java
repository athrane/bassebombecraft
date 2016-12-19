package bassebombecraft.event.block;

import static bassebombecraft.block.BlockUtils.createBlock;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import java.util.Random;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQueryImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * Event listener for processing of {@linkplain BlockDirective}.
 * 
 * Directives are only processed at server side.
 * 
 * When a directive is processed a particle is registered for rendering using
 * the {@linkplain ParticleRenderingRepository}.
 */
public class ProcessBlockDirectivesEventListener {

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 5;
	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.SPELL_INSTANT;
	static final int PARTICLE_DURATION = 20;
	static final double PARTICLE_SPEED = 3.0D; // Particle speed	
	static final ParticleRenderingInfo PARTICLE_INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R,
			G, B, PARTICLE_SPEED);

	static final int BLOCKS_PER_TICK = 3; // Measured in world ticks
	static final BlockPos NULL_POSITION = null; // NULL block position.

	/**
	 * Block directives repository.
	 */
	BlockDirectivesRepository directivesRepository;

	/**
	 * Particle rendering repository.
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * Random generator.
	 */
	Random random = new Random();

	/**
	 * ProcessBlockDirectiveEventListener constructor.
	 * 
	 * @param directivesRepository
	 *            block directives repository.
	 * @param particleRepository
	 *            particle rendering repository.
	 * 
	 */
	public ProcessBlockDirectivesEventListener(BlockDirectivesRepository directivesRepository,
			ParticleRenderingRepository particleRepository) {
		super();
		this.directivesRepository = directivesRepository;
		this.particleRepository = particleRepository;
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) throws Exception {

		// exit if no directives are waiting to be processed.
		if (!directivesRepository.containsDirectives())
			return;

		// get world
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();

		// exit if at client side
		if (world.isRemote)
			return;

		// create world query
		WorldQueryImpl worldQuery = new WorldQueryImpl(player, NULL_POSITION);

		// process directives
		for (int i = 0; i < BLOCKS_PER_TICK; i++) {
			processDirective(world, worldQuery);
		}
	}

	void processDirective(World world, WorldQueryImpl worldQuery) throws Exception {
		// exit if no directives are waiting to be processed.
		if (!directivesRepository.containsDirectives())
			return;

		// get directive
		BlockDirective directive = directivesRepository.getNext();

		// skip if source and target states are both air
		IBlockState currentState = world.getBlockState(directive.getBlockPosition());
		if (currentState.equals(directive.getState())) {

			// skip to next directive
			processDirective(world, worldQuery);
			return;
		}

		// process directive
		createBlock(directive, worldQuery);

		// register directive for rendering
		BlockPos pos = directive.getBlockPosition();
		ParticleRendering particle = getInstance(pos, PARTICLE_INFO);
		particleRepository.add(particle);
	}

}
