package bassebombecraft.entity;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraft.entity.EntityClassification.MISC;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.ENTITIES;

import java.util.function.Supplier;

import bassebombecraft.entity.projectile.EggProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered entities.
 */
public class RegisteredEntities {

	/**
	 * Deferred registry for registration of entities.
	 */
	public static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = create(ENTITIES, MODID);

	/**
	 * Registered entities.
	 */
	public static final RegistryObject<EntityType<EggProjectileEntity>> EGG_PROJECTILE = registerProjectile(
			EggProjectileEntity.NAME.toLowerCase(), EggProjectileEntity::new);
	
	/**
	 * Register object for entities which are used as projectiles.
	 * 
	 * @param key       registry object name.
	 * @param factoryIn factory used to create entity instance.
	 * 
	 * @return registry object.
	 */
	static <T extends Entity> RegistryObject<EntityType<T>> registerProjectile(String key, IFactory<T> factoryIn) {
		Supplier<EntityType<T>> splEntity = () -> EntityType.Builder.<T>create(factoryIn, MISC)
				.build(key.toLowerCase());
		return ENTITY_REGISTRY.register(key.toLowerCase(), splEntity);
	}

}
