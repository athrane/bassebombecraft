package bassebombecraft.client.particles;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.ForgeRegistries.PARTICLE_TYPES;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Object holders for registered particle types.
 * 
 * Entity types are registered in {@linkplain ParticleTypeRegistryEventHandler}.
 */
@OnlyIn(Dist.CLIENT)
public class RegisteredParticles {

	/**
	 * Defered registry for registration of particles.
	 */
	public static final DeferredRegister<ParticleType<?>> PARTICLES = new DeferredRegister<>(PARTICLE_TYPES, MODID);

	/**
	 * Registry object for {@linkplain LightningParticle}.
	 */
	public static final RegistryObject<BasicParticleType> LIGHTNING_PARTICLE = PARTICLES.register(LightningParticle.NAME.toLowerCase(), ()-> new BasicParticleType(true));
	
}
