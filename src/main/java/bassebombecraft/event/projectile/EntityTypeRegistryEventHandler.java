package bassebombecraft.event.projectile;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraft.entity.EntityClassification.MISC;

import bassebombecraft.entity.projectile.CircleProjectileEntity;
import bassebombecraft.entity.projectile.EggProjectileEntity;
import bassebombecraft.entity.projectile.LightningProjectileEntity;
import bassebombecraft.entity.projectile.LlamaProjectileEntity;
import bassebombecraft.entity.projectile.SkullProjectileEntity;
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

		String name = EggProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<EggProjectileEntity>create(EggProjectileEntity::new, MISC).build(name)
				.setRegistryName(MODID, name));

		name = LlamaProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<LlamaProjectileEntity>create(LlamaProjectileEntity::new, MISC).build(name)
				.setRegistryName(MODID, name));

		name = LightningProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<LightningProjectileEntity>create(LightningProjectileEntity::new, MISC)
				.build(name).setRegistryName(MODID, name));

		name = CircleProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<CircleProjectileEntity>create(CircleProjectileEntity::new, MISC)
				.build(name).setRegistryName(MODID, name));

		name = SkullProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<SkullProjectileEntity>create(SkullProjectileEntity::new, MISC)
				.build(name).setRegistryName(MODID, name));
		
	}

}
