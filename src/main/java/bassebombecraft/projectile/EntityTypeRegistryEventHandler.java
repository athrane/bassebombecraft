package bassebombecraft.projectile;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.projectile.GenericEggProjectile.PROJECTILE_NAME;
import static net.minecraft.entity.EntityClassification.MISC;

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

		event.getRegistry().register(EntityType.Builder.<GenericEggProjectile>create(GenericEggProjectile::new, MISC)
				.build(PROJECTILE_NAME).setRegistryName(MODID, PROJECTILE_NAME));
	}

}
