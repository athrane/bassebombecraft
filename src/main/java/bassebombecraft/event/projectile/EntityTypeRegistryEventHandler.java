package bassebombecraft.event.projectile;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraft.entity.EntityClassification.MISC;

import bassebombecraft.entity.projectile.CompositeProjectileEntity;
import bassebombecraft.entity.projectile.LlamaProjectileEntity;
import bassebombecraft.entity.projectile.GenericEggProjectile;
import bassebombecraft.entity.projectile.LightningProjectileEntity;
import bassebombecraft.operator.projectile.egg.OperatorEggProjectile2;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Event handler for registration of entity types, projectiles.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypeRegistryEventHandler {

	/**
	 * Handle {@linkplain RegistryEvent.Register<EntityType<?>>} event to register
	 * entity types with forge.
	 * 
	 * @param event register entity event.
	 */
	@SubscribeEvent
	public static void handleRegisterEntityType(RegistryEvent.Register<EntityType<?>> event) {
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();

		String name = GenericEggProjectile.NAME.toLowerCase();
		registry.register(EntityType.Builder.<GenericEggProjectile>create(GenericEggProjectile::new, MISC).build(name)
				.setRegistryName(MODID, name));

		name = OperatorEggProjectile2.NAME.toLowerCase();
		registry.register(EntityType.Builder.<OperatorEggProjectile2>create(OperatorEggProjectile2::new, MISC)
				.build(name).setRegistryName(MODID, name));

		name = CompositeProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<CompositeProjectileEntity>create(CompositeProjectileEntity::new, MISC)
				.build(name).setRegistryName(MODID, name));

		name = LlamaProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<LlamaProjectileEntity>create(LlamaProjectileEntity::new, MISC)
				.build(name).setRegistryName(MODID, name));

		name = LightningProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<LightningProjectileEntity>create(LightningProjectileEntity::new, MISC)
				.build(name).setRegistryName(MODID, name));
		
	}

}
