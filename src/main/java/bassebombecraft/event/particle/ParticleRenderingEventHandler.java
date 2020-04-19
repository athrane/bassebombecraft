package bassebombecraft.event.particle;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.PARTICLE_RENDERING_FREQUENCY;
import static bassebombecraft.world.WorldUtils.isWorldAtServerSide;

import java.util.Random;

import bassebombecraft.event.frequency.FrequencyRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for rendering particles.
 * 
 * The handler only executes events CLIENT side.
 */
@Mod.EventBusSubscriber
public class ParticleRenderingEventHandler {

	@SubscribeEvent
	public static void handleWorldTickEvent(WorldTickEvent event) {
		try {

			// exit if handler is executed at server side
			if (isWorldAtServerSide(event.world))
				return;

			// exit if particles shouldn't be rendered in this tick
			// exit if frequency isn't active
			FrequencyRepository frequencyRepository = getProxy().getFrequencyRepository();
			if (!frequencyRepository.isActive(PARTICLE_RENDERING_FREQUENCY))
				return;

			// render particles
			render(event.world);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Render particles.
	 * 
	 * @param world world object.
	 * 
	 * @throws Exception if rendering fails.
	 */
	static void render(World world) throws Exception {
		ParticleRenderingRepository repository = getProxy().getParticleRenderingRepository();
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
		double speed = particle.getInfo().getSpeed();
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

		double speed = particle.getInfo().getSpeed();
		double d0 = calculateRandomSpeed(speed);
		double d1 = calculateRandomSpeed(speed);
		double d2 = calculateRandomSpeed(speed);

		float r = particle.getRedColorComponent(random);
		float g = particle.getGreenColorComponent(random);
		float b = particle.getBlueColorComponent(random);

		double x = particle.getPosition().getX() + 0.5D;
		double y = particle.getPosition().getY() + 1;
		double z = particle.getPosition().getZ() + 0.5D;

		Minecraft mcClient = Minecraft.getInstance();
		ParticleManager manager = mcClient.particles;
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
