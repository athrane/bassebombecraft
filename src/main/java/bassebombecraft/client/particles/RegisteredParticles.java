package bassebombecraft.client.particles;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.ForgeRegistries.PARTICLE_TYPES;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered particle types.
 */
public class RegisteredParticles {

	/**
	 * Deferred registry for registration of particles.
	 */
	public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTRY = new DeferredRegister<>(PARTICLE_TYPES,
			MODID);

	/**
	 * Registry object for {@linkplain LightningParticle}.
	 */
	public static final RegistryObject<BasicParticleType> LIGHTNING_PARTICLE = PARTICLE_REGISTRY
			.register(LightningParticle.NAME.toLowerCase(), () -> new BasicParticleType(true));

	/**
	 * Registry object for {@linkplain SparkParticle}.
	 */
	public static final RegistryObject<BasicParticleType> SPARK_PARTICLE = PARTICLE_REGISTRY
			.register(SparkParticle.NAME.toLowerCase(), () -> new BasicParticleType(true));

	/**
	 * Registry object for {@linkplain ChickenParticle}.
	 */
	public static final RegistryObject<BasicParticleType> CHICKEN_PARTICLE = PARTICLE_REGISTRY
			.register(ChickenParticle.NAME.toLowerCase(), () -> new BasicParticleType(true));

	/**
	 * Registry object for {@linkplain CircleParticle}.
	 */
	public static final RegistryObject<BasicParticleType> CIRCLE_PARTICLE = PARTICLE_REGISTRY
			.register(CircleParticle.NAME.toLowerCase(), () -> new BasicParticleType(true));

	/**
	 * Registry object for {@linkplain SkullParticle}.
	 */
	public static final RegistryObject<BasicParticleType> SKULL_PARTICLE = PARTICLE_REGISTRY
			.register(SkullParticle.NAME.toLowerCase(), () -> new BasicParticleType(true));

	/**
	 * Registry object for {@linkplain BlockParticle}.
	 */
	public static final RegistryObject<BasicParticleType> BLOCK_PARTICLE = PARTICLE_REGISTRY
			.register(BlockParticle.NAME.toLowerCase(), () -> new BasicParticleType(true));

}
