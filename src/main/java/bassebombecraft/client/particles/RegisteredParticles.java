package bassebombecraft.client.particles;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.ForgeRegistries.PARTICLE_TYPES;

import bassebombecraft.client.event.rendering.particle.ParticleFactoryRegistryEventHandler;
import net.minecraft.client.particle.SpriteTexturedParticle;
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
	public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTRY = new DeferredRegister<>(PARTICLE_TYPES,
			MODID);

	/**
	 * Registry object for {@linkplain LightningParticle}.
	 */
	public static final RegistryObject<BasicParticleType> LIGHTNING_PARTICLE = register(LightningParticle.class);

	/**
	 * Registry object for {@linkplain SparkParticle}.
	 */
	public static final RegistryObject<BasicParticleType> SPARK_PARTICLE = register(SparkParticle.class);

	/**
	 * Registry object for {@linkplain ChickenParticle}.
	 */
	public static final RegistryObject<BasicParticleType> CHICKEN_PARTICLE = register(ChickenParticle.class);

	/**
	 * Registry object for {@linkplain CircleParticle}.
	 */
	public static final RegistryObject<BasicParticleType> CIRCLE_PARTICLE = register(CircleParticle.class);

	/**
	 * Registry object for {@linkplain SkullParticle}.
	 */
	public static final RegistryObject<BasicParticleType> SKULL_PARTICLE = register(SkullParticle.class);

	/**
	 * Registry object for {@linkplain BlockParticle}.
	 */
	public static final RegistryObject<BasicParticleType> BLOCK_PARTICLE = register(BlockParticle.class);

	/**
	 * Registry object for {@linkplain PlayerAggroParticle}.
	 */
	public static final RegistryObject<BasicParticleType> PLAYER_AGGRO_PARTICLE = register(PlayerAggroParticle.class);

	/**
	 * Registry object for {@linkplain CurseParticle}.
	 */
	public static final RegistryObject<BasicParticleType> CURSE_PARTICLE = register(CurseParticle.class);

	/**
	 * Registry object for {@linkplain CurseParticle}.
	 */
	public static final RegistryObject<BasicParticleType> REFLECT_PARTICLE = register(ReflectParticle.class);

	/**
	 * Register particle as {@linkplain BasicParticleType}.
	 * 
	 * @param class1 class for particle to register.
	 * 
	 * @return registry object.
	 */
	static RegistryObject<BasicParticleType> register(Class<? extends SpriteTexturedParticle> class1) {
		String key = class1.getSimpleName().toLowerCase();
		return PARTICLE_REGISTRY.register(key, () -> new BasicParticleType(true));
	}

}
