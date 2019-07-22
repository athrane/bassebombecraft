package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.SPAWN_PARTICLES_FREQUENCY;
import static bassebombecraft.entity.EntityUtils.isLivingEntity;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

	@SubscribeEvent
	static public void handleEvent(LivingUpdateEvent event) {
		if (!isLivingEntity(event.getEntityLiving())) return;
		LivingEntity entity = event.getEntityLiving();

		// get repository
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		FrequencyRepository frequencyRepository = getBassebombeCraft().getFrequencyRepository(); 
		
		// exit if entity isn't charmed
		if (!repository.contains(entity))
			return;

		// update charm
		repository.update(entity);

		// exit if frequency isn't active
		if(!frequencyRepository.isActive(SPAWN_PARTICLES_FREQUENCY)) return;
				
		// get repository
		ParticleRenderingRepository particleRepository = getBassebombeCraft().getParticleRenderingRepository();
					
		// register directive for rendering
		BlockPos pos = entity.getPosition();
		ParticleRendering particle = getInstance(pos, PARTICLE_INFO);
		particleRepository.add(particle);
	}

	@SubscribeEvent
	public static void handleEvent(LivingDeathEvent event) {
		if (!isLivingEntity(event.getEntityLiving())) return;
		LivingEntity entity = event.getEntityLiving();

		// get repository
		CharmedMobsRepository repository = getBassebombeCraft().getCharmedMobsRepository();
		
		// remove
		repository.remove(entity);
	}
	
}
