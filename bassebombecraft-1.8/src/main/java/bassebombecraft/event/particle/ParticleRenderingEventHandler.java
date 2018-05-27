package bassebombecraft.event.particle;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.world.WorldUtils.isWorldAtServerSide;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Event handler for rendering particles.
 * 
 * Particles are only rendered at client side.
 */
@Mod.EventBusSubscriber
public class ParticleRenderingEventHandler {

	/**
	 * Rendering frequency.
	 */
	static final int RENDERING_FREQUENCY = 10; // Measured in world ticks

	/**
	 * Game ticks counter.
	 */
	static int ticksCounter = 0;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onPlayerTick(PlayerTickEvent event) {
		ticksCounter++;

		// get repository
		ParticleRenderingRepository repository = getBassebombeCraft().getParticleRenderingRepository();

		// update particle duration
		repository.updateParticleDuration();

		// get world
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();

		// exit if world is undefined
		if (world == null)
			return;

		// exit if at server side
		// exit if invoked at server side
		if (isWorldAtServerSide(world))
			return;

		// exit if particles should be rendered in this tick
		if (ticksCounter % RENDERING_FREQUENCY != 0)
			return;

		// render particles
		render(world, repository);
	}

	/**
	 * Render particles.
	 * 
	 * @param world
	 *            world object.
	 * @param repository
	 *            particle rendering repository.
	 */
	static void render(World world, ParticleRenderingRepository repository) {

		// get and render particles
		ParticleRendering[] particles = repository.getParticles();
		for (ParticleRendering particle : particles) {

			// render multiple instances of particles if specified
			for (int i = 0; i < particle.getNumber(); i++) {
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
	 * @param particle
	 *            particle to inspect.
	 * 
	 * @return true if particle should be rendered with custom color.
	 */
	static boolean renderWithCustomColor(ParticleRendering particle) {
		EnumParticleTypes type = particle.getParticleType();
		return (type == EnumParticleTypes.SPELL_MOB);
	}

	/**
	 * Render single particle.
	 * 
	 * @param world
	 *            world object
	 * @param particle
	 *            particle to render.
	 */
	static void renderParticle(World world, ParticleRendering particle) {
		double speed = particle.getSpeed();
		double d0 = calculateRandomSpeed(speed);
		double d1 = calculateRandomSpeed(speed);
		double d2 = calculateRandomSpeed(speed);

		double x = particle.getPosition().getX() + 0.5D;
		double y = particle.getPosition().getY() + 1;
		double z = particle.getPosition().getZ() + 0.5D;
		world.spawnParticle(particle.getParticleType(), x, y, z, d0, d1, d2);
	}

	/**
	 * Render single particle with custom color.
	 * 
	 * Assumes that the particle is of a type which supports custom colors, e.g.
	 * {@linkplain EnumParticleTypes.SPELL_MOB}.
	 * 
	 * The particle is rendered without any acceleration
	 * 
	 * @param world
	 *            world object
	 * @param particle
	 *            particle to render.
	 */
	static void renderParticleWithCustomColor(World world, ParticleRendering particle) {
		Random random = getBassebombeCraft().getRandom();
		float r = particle.getRedColorComponent(random);
		float g = particle.getGreenColorComponent(random);
		float b = particle.getBlueColorComponent(random);

		double x = particle.getPosition().getX() + 0.5D;
		double y = particle.getPosition().getY() + 1;
		double z = particle.getPosition().getZ() + 0.5D;
		world.spawnParticle(particle.getParticleType(), x, y, z, r, g, b);
	}

	/**
	 * Calculate random speed.
	 * 
	 * @param speed
	 *            particle speed.
	 * 
	 * @return double between -PARTICLE_SPEED..PARTICLE_SPEED.
	 */
	static double calculateRandomSpeed(double speed) {
		Random random = getBassebombeCraft().getRandom();		
		return (random.nextDouble() * 2.0D - 1.0D) * speed;
	}

}
