package bassebombecraft.event.charm;

import static bassebombecraft.ModConstants.CHARM_PARTICLE_RENDERING_FREQUENCY;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.config.ModConfiguration.charmedMobParticles;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.network.NetworkChannelHelper;
import bassebombecraft.network.packet.AddParticleRendering;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.client.rendering.AddParticlesFromEntityAtClient2;
import bassebombecraft.operator.conditional.IsEntity1OfType2;
import bassebombecraft.operator.conditional.IsEntityIsCharmed2;
import bassebombecraft.operator.conditional.IsFrequencyIsActive2;
import bassebombecraft.operator.conditional.IsWorldAtServerSide2;
import bassebombecraft.operator.entity.RemoveCharm2;
import net.minecraft.entity.MobEntity;
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

	/**
	 * Operator for spawning particles for charmed mob.
	 */
	static Operator2[] particlesOps = { new IsWorldAtServerSide2(),
			new IsFrequencyIsActive2(CHARM_PARTICLE_RENDERING_FREQUENCY), new IsEntity1OfType2(MobEntity.class),
			new IsEntityIsCharmed2(), new AddParticlesFromEntityAtClient2(createFromConfig(charmedMobParticles)) };

	/**
	 * Operators for uncharm.
	 */
	static Operator2[] uncharmOps = { new IsWorldAtServerSide2(), new IsEntity1OfType2(MobEntity.class),
			new RemoveCharm2() };

	@SubscribeEvent
	static public void handleLivingUpdateEvent(LivingUpdateEvent event) {
		Ports ports = getInstance().setLivingEntity1(event.getEntityLiving());
		run(ports, particlesOps);
	}

	@SubscribeEvent
	public static void handleLivingDeathEvent(LivingDeathEvent event) {
		Ports ports = getInstance().setLivingEntity1(event.getEntityLiving());
		run(ports, uncharmOps);
	}

}
