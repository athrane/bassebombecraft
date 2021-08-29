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
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE;

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
		
		addAttackDamageAttributeToPigEntity(event);
		addAttackDamageAttributeToParrotEntity(event);
	}

	/**
	 * Add the {@linkplain Attributes.ATTACK_DAMAGE} attribute to the
	 * {@linkplain PigEntity} to support the attack value modification in the
	 * {@linkplain SpawnWarPig2} operator.
	 * 
	 * @param event attribute modification event.
	 */
	static void addAttackDamageAttributeToPigEntity(EntityAttributeModificationEvent event) {
		event.add(EntityType.PIG, ATTACK_DAMAGE);
	}

	/**
	 * Add the {@linkplain Attributes.ATTACK_DAMAGE} attribute to the
	 * {@linkplain ParrotEntity} to support the attack value modification in the
	 * {@linkplain SpawnAngryParrots} operator.
	 * 
	 * @param event attribute modification event.
	 */
	static void addAttackDamageAttributeToParrotEntity(EntityAttributeModificationEvent event) {
		event.add(EntityType.PARROT, ATTACK_DAMAGE);
	}
	
}
