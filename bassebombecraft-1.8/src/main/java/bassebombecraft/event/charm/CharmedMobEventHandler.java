package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

/**
 * Event handler for managing charmed mobs, i.e. removal on death and update for the charm to time out.
 */
@Mod.EventBusSubscriber
public class CharmedMobEventHandler {

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 1;
	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.HEART;
	static final int PARTICLE_DURATION = 20; // Measured in world ticks
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo PARTICLE_INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R,
			G, B, PARTICLE_SPEED);

	static final int SPAWN_PARTICLES_FREQUENCY = 40; // Measured in world ticks

	/**
	 * Game ticks counter.
	 */	
	static int ticksCounter = 0;

	@SubscribeEvent
	static public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.getEntityLiving() == null)
			return;
		if (!(event.getEntityLiving() instanceof EntityLiving))
			return;

		// cast
		EntityLiving entityLiving = EntityLiving.class.cast(event.getEntityLiving());

		// get repository
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		
		// exit if entity isn't charmed
		if (!repository.contains(entityLiving))
			return;

		// update charm
		repository.update(entityLiving);

		// spawn particles
		if (ticksCounter % SPAWN_PARTICLES_FREQUENCY == 0) {

			// get repository
			ParticleRenderingRepository particleRepository = getBassebombeCraft().getParticleRenderingRepository();
						
			// register directive for rendering
			BlockPos pos = entityLiving.getPosition();
			ParticleRendering particle = getInstance(pos, PARTICLE_INFO);
			particleRepository.add(particle);
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() == null)
			return;
		if (!(event.getEntityLiving() instanceof EntityLiving))
			return;

		// cast
		EntityLiving entityLiving = EntityLiving.class.cast(event.getEntityLiving());

		// get repository
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		
		// remove
		repository.remove(entityLiving);
	}

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		ticksCounter++;
	}

}
