package bassebombecraft.client.particles;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.PARTICLE_TYPES;

import bassebombecraft.client.event.rendering.particle.ParticleFactoryRegistryEventHandler;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
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
	public static final RegistryObject<BasicParticleType> LIGHTNING_PARTICLE = register("lightningparticle");
	public static final RegistryObject<BasicParticleType> SPARK_PARTICLE = register("sparkparticle");
	public static final RegistryObject<BasicParticleType> CHICKEN_PARTICLE = register("chickenparticle");
	public static final RegistryObject<BasicParticleType> CIRCLE_PARTICLE = register("circleparticle");
	public static final RegistryObject<BasicParticleType> SKULL_PARTICLE = register("skullparticle");
	public static final RegistryObject<BasicParticleType> BLOCK_PARTICLE = register("blockparticle");
	public static final RegistryObject<BasicParticleType> PLAYER_AGGRO_PARTICLE = register("playeraggroparticle");
	public static final RegistryObject<BasicParticleType> CURSE_PARTICLE = register("curseparticle");
	public static final RegistryObject<BasicParticleType> REFLECT_PARTICLE = register("reflectparticle");

	/**
	 * Register particle as {@linkplain BasicParticleType}.
	 * 
	 * @param key key particle to register.
	 * 
	 * @return registry object.
	 */
	static RegistryObject<BasicParticleType> register(String key) {
		return PARTICLE_REGISTRY.register(key, () -> new BasicParticleType(true));
	}

}
