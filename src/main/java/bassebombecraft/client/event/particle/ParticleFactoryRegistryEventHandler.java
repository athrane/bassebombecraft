package bassebombecraft.client.event.particle;

import static bassebombecraft.client.particles.RegisteredParticles.CHICKEN_PARTICLE;
import static bassebombecraft.client.particles.RegisteredParticles.LIGHTNING_PARTICLE;
import static bassebombecraft.client.particles.RegisteredParticles.SPARK_PARTICLE;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.client.particles.ChickenParticle;
import bassebombecraft.client.particles.LightningParticle;
import bassebombecraft.client.particles.SparkParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for registration of particle factories.
 * 
 * The handler executes events CLIENT side.
 * 
 * This {@linkplain ParticleFactoryRegisterEvent} event is handled by the mod
 * event bus, so the mod argument must defined in the annotation.
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleFactoryRegistryEventHandler {

	/**
	 * Handle {@linkplain ParticleFactoryRegisterEvent} event to register particle
	 * factories.with forge.
	 * 
	 * @param event register entity event.
	 */
	@SubscribeEvent
	public static void handleParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {

		ParticleManager particles = Minecraft.getInstance().particles;

		/**
		 * Register factory for {@linkplain LightningParticle}.
		 */
		particles.registerFactory(LIGHTNING_PARTICLE.get(), sprite -> new LightningParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain SparkParticle}.
		 */
		particles.registerFactory(SPARK_PARTICLE.get(), sprite -> new SparkParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain ChickenParticle}.
		 */
		particles.registerFactory(CHICKEN_PARTICLE.get(), sprite -> new ChickenParticle.Factory(sprite));

	}

}
