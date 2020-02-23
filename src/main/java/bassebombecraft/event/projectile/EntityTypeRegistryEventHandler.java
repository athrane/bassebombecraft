package bassebombecraft.event.projectile;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraft.entity.EntityClassification.MISC;

import bassebombecraft.projectile.GenericEggProjectile;
import bassebombecraft.projectile.OperatorEggProjectile;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Class for initializing projectiles.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypeRegistryEventHandler {

	/**
	 * Initialize projectile.
	 * 
	 * @param event register entity event.
	 */
	@SubscribeEvent
	public void handleEvent(RegistryEvent.Register<EntityType<?>> event) {

		event.getRegistry()
				.register(EntityType.Builder.<GenericEggProjectile>create(GenericEggProjectile::new, MISC)
						.build(GenericEggProjectile.PROJECTILE_NAME)
						.setRegistryName(MODID, GenericEggProjectile.PROJECTILE_NAME));

		event.getRegistry().register(
				EntityType.Builder.<OperatorEggProjectile>create(OperatorEggProjectile::new, MISC)
						.build(OperatorEggProjectile.PROJECTILE_NAME)
						.setRegistryName(MODID, OperatorEggProjectile.PROJECTILE_NAME));

	}

}
