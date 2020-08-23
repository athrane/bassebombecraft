package bassebombecraft.client.particles;

import static bassebombecraft.client.particles.RegisteredParticles.LIGHTNING_PARTICLE;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for registration of particle factories.
 * 
 * {@linkplain LightningParticle}
 * 
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
	}

}
