package bassebombecraft.client.event.rendering.particle;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;

import java.util.Random;
import java.util.stream.Stream;

import bassebombecraft.event.particle.ParticleRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Client side renderer for rendering particles.
 */
public class ParticleRenderer {

	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
		try {

			// exit if player isn't defined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			Player player = getClientSidePlayer();

			// get world
			Level world = player.getCommandSenderWorld();

			// render particles
			render(world);

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
	static void render(Level world) throws Exception {
		ParticleRenderingRepository repository = getProxy().getClientParticleRenderingRepository();

		// get and render particles
		Stream<ParticleRendering> particles = repository.get();
		particles.forEach(p -> {
			// render multiple instances of particles if specified
			int numberToRender = p.getNumber();
			for (int i = 0; i < numberToRender; i++) {
				if (renderWithCustomColor(p)) {
					renderParticleWithCustomColor(world, p);
					continue;
				}
				renderParticle(world, p);
			}
		});
	}

	/**
	 * Returns true if particle should be rendered with custom color.
	 * 
	 * @param particle particle to inspect.
	 * 
	 * @return true if particle should be rendered with custom color.
	 */
	static boolean renderWithCustomColor(ParticleRendering particle) {
		SimpleParticleType type = particle.getParticleType();
		return (type == ParticleTypes.EFFECT);
	}

	/**
	 * Render single particle.
	 * 
	 * @param world    world object
	 * @param particle particle to render.
	 */
	static void renderParticle(Level world, ParticleRendering particle) {
		double speed = particle.getInfo().getSpeed();
		double d0 = calculateRandomSpeed(speed);
		double d1 = calculateRandomSpeed(speed);
		double d2 = calculateRandomSpeed(speed);

		double x = particle.getPosition().getX() + 0.5D;
		double y = particle.getPosition().getY() + 1.0D;
		double z = particle.getPosition().getZ() + 0.5D;

		// add particle
		Minecraft mcClient = Minecraft.getInstance();
		ParticleEngine manager = mcClient.particleEngine;
		Particle spellParticle = manager.createParticle(particle.getParticleType(), x, y, z, d0, d1, d2);

		// set age
		int duration = Math.abs(particle.getInfo().getDuration());
		spellParticle.setLifetime(duration);
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
	 * 
	 * @param particle particle to render.
	 */
	static void renderParticleWithCustomColor(Level world, ParticleRendering particle) {
		Random random = getBassebombeCraft().getRandom();

		double speed = particle.getInfo().getSpeed();
		double d0 = calculateRandomSpeed(speed);
		double d1 = calculateRandomSpeed(speed);
		double d2 = calculateRandomSpeed(speed);

		float r = particle.getRedColorComponent(random);
		float g = particle.getGreenColorComponent(random);
		float b = particle.getBlueColorComponent(random);

		double x = particle.getPosition().getX() + 0.5D;
		double y = particle.getPosition().getY() + 1.0D;
		double z = particle.getPosition().getZ() + 0.5D;

		// add particle
		Minecraft mcClient = Minecraft.getInstance();
		ParticleEngine manager = mcClient.particleEngine;
		Particle spellParticle = manager.createParticle(ParticleTypes.EFFECT, x, y, z, d0, d1, d2);

		// set color
		spellParticle.setColor(r, g, b);

		// set age
		int duration = Math.abs(particle.getInfo().getDuration());
		spellParticle.setLifetime(duration);
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
