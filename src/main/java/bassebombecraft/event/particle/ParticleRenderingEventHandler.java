package bassebombecraft.event.particle;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getMincraft;
import static bassebombecraft.ModConstants.PARTICLE_RENDERING_FREQUENCY;
import static bassebombecraft.world.WorldUtils.isWorldAtServerSide;

import java.util.Random;

import bassebombecraft.event.frequency.FrequencyRepository;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for rendering particles.
 * 
 * Particles are only rendered at client side.
 */
@Mod.EventBusSubscriber
public class ParticleRenderingEventHandler {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void handlePlayerTickEvent(PlayerTickEvent event) {

		// get repository
		ParticleRenderingRepository repository = getBassebombeCraft().getParticleRenderingRepository();
		FrequencyRepository frequencyRepository = getBassebombeCraft().getFrequencyRepository();

		// update particle duration
		repository.updateParticleDuration();

		// get world
		PlayerEntity player = event.player;
		World world = player.getEntityWorld();

		// exit if world is undefined
		if (world == null)
			return;

		// exit if at server side
		// exit if invoked at server side
		if (isWorldAtServerSide(world))
			return;

		// exit if particles should be rendered in this tick
		// exit if frequency isn't active
		if (!frequencyRepository.isActive(PARTICLE_RENDERING_FREQUENCY))
			return;

		// render particles
		render(world, repository);
	}

	/**
	 * Render particles.
	 * 
	 * @param world      world object.
	 * @param repository particle rendering repository.
	 */
	static void render(World world, ParticleRenderingRepository repository) {
		try {
			// get and render particles
			ParticleRendering[] particles = repository.getParticles();
			for (ParticleRendering particle : particles) {

				// render multiple instances of particles if specified
				int numberToRender = particle.getNumber();
				for (int i = 0; i < numberToRender; i++) {
					if (renderWithCustomColor(particle)) {
						renderParticleWithCustomColor(world, particle);
						continue;
					}
					renderParticle(world, particle);
				}
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Returns true if particle should be rendered with custom color.
	 * 
	 * @param particle particle to inspect.
	 * 
	 * @return true if particle should be rendered with custom color.
	 */
	static boolean renderWithCustomColor(ParticleRendering particle) {
		BasicParticleType type = particle.getParticleType();
		return (type == ParticleTypes.EFFECT);
	}

	/**
	 * Render single particle.
	 * 
	 * @param world    world object
	 * @param particle particle to render.
	 */
	static void renderParticle(World world, ParticleRendering particle) {
		double speed = particle.getSpeed();
		double d0 = calculateRandomSpeed(speed);
		double d1 = calculateRandomSpeed(speed);
		double d2 = calculateRandomSpeed(speed);

		double x = particle.getPosition().getX() + 0.5D;
		double y = particle.getPosition().getY() + 1;
		double z = particle.getPosition().getZ() + 0.5D;

		world.addParticle(particle.getParticleType(), x, y, z, d0, d1, d2);
	}

	/**
	 * Render single particle with custom color.
	 * 
	 * Assumes that the particle is of a type which supports custom colors, e.g.
	 * {@linkplain ParticleTypes.EFFECT}.
	 * 
	 * The particle is rendered without any acceleration
	 * 
	 * @param world    world object
	 * @param particle particle to render.
	 */
	static void renderParticleWithCustomColor(World world, ParticleRendering particle) {
		Random random = getBassebombeCraft().getRandom();

		double speed = particle.getSpeed();
		double d0 = calculateRandomSpeed(speed);
		double d1 = calculateRandomSpeed(speed);
		double d2 = calculateRandomSpeed(speed);

		float r = particle.getRedColorComponent(random);
		float g = particle.getGreenColorComponent(random);
		float b = particle.getBlueColorComponent(random);

		double x = particle.getPosition().getX() + 0.5D;
		double y = particle.getPosition().getY() + 1;
		double z = particle.getPosition().getZ() + 0.5D;

		ParticleManager manager = getMincraft().particles;
		Particle spellParticle = manager.addParticle(ParticleTypes.EFFECT, x, y, z, d0, d1, d2);
		spellParticle.setColor(r, g, b);
	}

	/**
	 * Calculate random speed.
	 * 
	 * @param speed particle speed.
	 * 
	 * @return double between -PARTICLE_SPEED..PARTICLE_SPEED.
	 */
	static double calculateRandomSpeed(double speed) {
		Random random = getBassebombeCraft().getRandom();
		return (random.nextDouble() * 2.0D - 1.0D) * speed;
	}

}
