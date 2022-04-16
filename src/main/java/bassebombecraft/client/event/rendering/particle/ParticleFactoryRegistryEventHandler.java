package bassebombecraft.client.event.rendering.particle;

import static bassebombecraft.client.particles.RegisteredParticles.*;
import static bassebombecraft.client.particles.RegisteredParticles.LIGHTNING_PARTICLE;
import static bassebombecraft.client.particles.RegisteredParticles.SPARK_PARTICLE;

import bassebombecraft.client.particles.BlockParticle;
import bassebombecraft.client.particles.ChickenParticle;
import bassebombecraft.client.particles.CircleParticle;
import bassebombecraft.client.particles.CurseParticle;
import bassebombecraft.client.particles.LightningParticle;
import bassebombecraft.client.particles.PlayerAggroParticle;
import bassebombecraft.client.particles.ReflectParticle;
import bassebombecraft.client.particles.SkullParticle;
import bassebombecraft.client.particles.SparkParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
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

		Minecraft mcClient = Minecraft.getInstance();		
		ParticleEngine particles = mcClient.particleEngine;

		/**
		 * Register factory for {@linkplain LightningParticle}.
		 */
		particles.register(LIGHTNING_PARTICLE.get(), sprite -> new LightningParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain SparkParticle}.
		 */
		particles.register(SPARK_PARTICLE.get(), sprite -> new SparkParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain ChickenParticle}.
		 */
		particles.register(CHICKEN_PARTICLE.get(), sprite -> new ChickenParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain CircleParticle}.
		 */
		particles.register(CIRCLE_PARTICLE.get(), sprite -> new CircleParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain SkullParticle}.
		 */
		particles.register(SKULL_PARTICLE.get(), sprite -> new SkullParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain BlockParticle}.
		 */
		particles.register(BLOCK_PARTICLE.get(), sprite -> new BlockParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain PlayerAggroParticle}.
		 */
		particles.register(PLAYER_AGGRO_PARTICLE.get(), sprite -> new PlayerAggroParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain CurseParticle}.
		 */
		particles.register(CURSE_PARTICLE.get(), sprite -> new CurseParticle.Factory(sprite));

		/**
		 * Register factory for {@linkplain ReflectParticle}.
		 */
		particles.register(REFLECT_PARTICLE.get(), sprite -> new ReflectParticle.Factory(sprite));
		
	}

}
