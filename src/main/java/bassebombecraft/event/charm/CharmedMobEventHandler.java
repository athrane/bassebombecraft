package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.CHARM_PARTICLE_RENDERING_FREQUENCY;
import static bassebombecraft.entity.EntityUtils.isTypeMobEntity;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.network.NetworkChannelHelper;
import bassebombecraft.network.packet.AddParticleRendering;
import net.minecraft.entity.MobEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for managing charmed mobs, i.e. removal on death and update for
 * the charm to time out.
 * 
 * The handler only executes events SERVER side.
 * 
 * When a charmed mob is updated, a particle is registered for rendering using
 * {@linkplain NetworkChannelHelper} to send a {@linkplain AddParticleRendering}
 * packet to the client.
 */
@Mod.EventBusSubscriber
public class CharmedMobEventHandler {

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 1;
	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.HEART;
	static final int PARTICLE_DURATION = 20; // Measured in world ticks
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo PARTICLE_INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R,
			G, B, PARTICLE_SPEED);

	@SubscribeEvent
	static public void handleLivingUpdateEvent(LivingUpdateEvent event) {
		try {

			BassebombeCraft.getBassebombeCraft().getLogger().debug("handleLivingUpdateEvent: CP1 entity="+event.getEntityLiving());			
			
			// exit if handler is executed at client side
			if (isLogicalClient(event.getEntityLiving().getEntityWorld()))
				return;

			BassebombeCraft.getBassebombeCraft().getLogger().debug("handleLivingUpdateEvent: CP2, at logical server");			

			BassebombeCraft.getBassebombeCraft().getLogger().debug("handleLivingUpdateEvent: CP3 "+getProxy().getFrequencyRepository().isActive(CHARM_PARTICLE_RENDERING_FREQUENCY));			
			
			// exit if frequency isn't active
			if (!getProxy().getFrequencyRepository().isActive(CHARM_PARTICLE_RENDERING_FREQUENCY))
				return;

			BassebombeCraft.getBassebombeCraft().getLogger().debug("handleLivingUpdateEvent: CP3, charm rendering freq active");			
			
			// exit if entity isn't a mob (since only they can be charmed)
			if (!isTypeMobEntity(event.getEntityLiving()))
				return;

			BassebombeCraft.getBassebombeCraft().getLogger().debug("handleLivingUpdateEvent: CP4, is living entity");			
			
			// type cast
			MobEntity entity = (MobEntity) event.getEntityLiving();

			// exit if entity isn't charmed
			CharmedMobsRepository repository = getProxy().getCharmedMobsRepository(entity.getEntityWorld());
			if (!repository.contains(entity))
				return;

			BassebombeCraft.getBassebombeCraft().getLogger().debug("handleLivingUpdateEvent: CP5 is charmed");			
			
			// send particle rendering info to client
			BlockPos pos = entity.getPosition();
			ParticleRendering particle = getInstance(pos, PARTICLE_INFO);
			getProxy().getNetworkChannel(entity.getEntityWorld()).sendAddParticleRenderingPacket(particle);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@SubscribeEvent
	public static void handleLivingDeathEvent(LivingDeathEvent event) {
		try {

			// exit if handler is executed at client side
			if (isLogicalClient(event.getEntityLiving().getEntityWorld()))
				return;

			// type cast
			if (!isTypeMobEntity(event.getEntityLiving()))
				return;

			// get an type cast entity
			MobEntity entity = (MobEntity) event.getEntityLiving();

			// remove
			getProxy().getCharmedMobsRepository(entity.getEntityWorld()).remove(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
