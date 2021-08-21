package bassebombecraft.event.entity.attribute;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import java.util.List;

import static bassebombecraft.entity.attribute.RegisteredAttributes.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for adding custom entity attributes to existing vanilla mobs.
 * 
 * The handler only executes events SERVER side.
 * 
 * This {@linkplain EntityAttributeModificationEvent} event is handled by the
 * mod event bus, so the mod argument must defined in the annotation.
 */
@Mod.EventBusSubscriber(modid = MODID, bus = MOD)
public class EntityAttributeEventHandler {

	@SubscribeEvent
	static public void handleEntityAttributeModificationEvent(EntityAttributeModificationEvent event) {
		List<EntityType<? extends LivingEntity>> types = event.getTypes();
		types.stream().forEach(t -> {
			event.add(t, IS_DECOY_ATTRIBUTE.get());
			event.add(t, RESPAWN_ATTRIBUTE.get());			
			event.add(t, IS_RESPAWNED_ATTRIBUTE.get());						
		});
	}

}
