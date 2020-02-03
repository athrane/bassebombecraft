package bassebombecraft.entity.event;

import static bassebombecraft.ModConstants.MODID;

import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Class for initializing entities.
 */
@ObjectHolder(MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistryEventHandler {

	// @ObjectHolder(KAMIKAZE_BAT_NAME)
	// public static EntityType<KamikazeBatEntity> kamikazeBat = null;

	/**
	 * Initialize entities.
	 * 
	 * @param event register blocks event.
	 */
	@SubscribeEvent
	public static void handleRegisterEntityType(RegistryEvent.Register<EntityType<?>> event) {

		/**
		 * kamikazeBat = EntityType.Builder.create(KamikazeBatEntity::new,
		 * CREATURE).size(0.5F, 0.9F) .build(KAMIKAZE_BAT_NAME);
		 * kamikazeBat.setRegistryName(MODID, KAMIKAZE_BAT_NAME);
		 * event.getRegistry().register(kamikazeBat);
		 **/
	}

}
