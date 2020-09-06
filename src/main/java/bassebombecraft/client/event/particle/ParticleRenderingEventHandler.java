package bassebombecraft.client.event.particle;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.PARTICLE_SPAWN_FREQUENCY;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayer;
import static bassebombecraft.client.player.ClientPlayerUtils.isClientSidePlayerDefined;

import java.util.Random;
import java.util.stream.Stream;

import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for rendering particles.
 * 
 * The handler only executes events CLIENT side.
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ParticleRenderingEventHandler {

	@SubscribeEvent
	public static void handleRenderWorldLastEvent(RenderWorldLastEvent event) {
		try {

			// exit if player isn't defined
			if (!isClientSidePlayerDefined())
				return;

			// get player
			PlayerEntity player = getClientSidePlayer();

			// get world
			World world = player.getEntityWorld();

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
	static void render(World world) throws Exception {
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

		// add particle		
		Minecraft mcClient = Minecraft.getInstance();
		ParticleManager manager = mcClient.particles;
		Particle spellParticle = manager.addParticle(particle.getParticleType(), x, y, z, d0, d1, d2);
		
		// set age		
		spellParticle.setMaxAge(particle.getInfo().getDuration());		
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

		// add particle
		Minecraft mcClient = Minecraft.getInstance();
		ParticleManager manager = mcClient.particles;
		Particle spellParticle = manager.addParticle(ParticleTypes.EFFECT, x, y, z, d0, d1, d2);
		
		// set color and age
		spellParticle.setColor(r, g, b);
		spellParticle.setMaxAge(particle.getInfo().getDuration());				
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
