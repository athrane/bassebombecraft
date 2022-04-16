package bassebombecraft.client.particles;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.PARTICLE_TYPES;

import bassebombecraft.client.event.rendering.particle.ParticleFactoryRegistryEventHandler;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered particle types.
 * 
 * Please notice: particle should be registered in
 * {@linkplain ParticleFactoryRegistryEventHandler} as well.
 */
public class RegisteredParticles {

	/**
	 * Deferred registry for registration of particles.
	 */
	public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTRY = create(PARTICLE_TYPES, MODID);

	/**
	 * Registered particles.
	 * 
	 * The particle name are matching the corresponding particle class in the package bassebombecraft.client.particles.
	 */
	public static final RegistryObject<SimpleParticleType> LIGHTNING_PARTICLE = register("lightningparticle");
	public static final RegistryObject<SimpleParticleType> SPARK_PARTICLE = register("sparkparticle");
	public static final RegistryObject<SimpleParticleType> CHICKEN_PARTICLE = register("chickenparticle");
	public static final RegistryObject<SimpleParticleType> CIRCLE_PARTICLE = register("circleparticle");
	public static final RegistryObject<SimpleParticleType> SKULL_PARTICLE = register("skullparticle");
	public static final RegistryObject<SimpleParticleType> BLOCK_PARTICLE = register("blockparticle");
	public static final RegistryObject<SimpleParticleType> PLAYER_AGGRO_PARTICLE = register("playeraggroparticle");
	public static final RegistryObject<SimpleParticleType> CURSE_PARTICLE = register("curseparticle");
	public static final RegistryObject<SimpleParticleType> REFLECT_PARTICLE = register("reflectparticle");

	/**
	 * Register particle as {@linkplain BasicParticleType}.
	 * 
	 * @param key key particle to register.
	 * 
	 * @return registry object.
	 */
	static RegistryObject<SimpleParticleType> register(String key) {
		return PARTICLE_REGISTRY.register(key, () -> new SimpleParticleType(true));
	}

}
