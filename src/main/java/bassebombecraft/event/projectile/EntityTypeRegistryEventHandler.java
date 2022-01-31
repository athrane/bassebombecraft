package bassebombecraft.event.projectile;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraft.entity.EntityClassification.MISC;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import bassebombecraft.entity.projectile.CircleProjectileEntity;
import bassebombecraft.entity.projectile.LightningProjectileEntity;
import bassebombecraft.entity.projectile.SkullProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Event handler for registration of entity types (projectiles).
 */
@EventBusSubscriber(bus = MOD)
@Deprecated
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

		String name = CircleProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<CircleProjectileEntity>create(CircleProjectileEntity::new, MISC)
				.build(name).setRegistryName(MODID, name));

		name = SkullProjectileEntity.NAME.toLowerCase();
		registry.register(EntityType.Builder.<SkullProjectileEntity>create(SkullProjectileEntity::new, MISC).build(name)
				.setRegistryName(MODID, name));

	}

}
