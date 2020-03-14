package bassebombecraft.event.potion;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import bassebombecraft.potion.effect.DecreaseSizeEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the decrease size potion effect.
 * 
 * Server side logic for the {@linkplain DecreaseSizeEffect}.
 */
@Mod.EventBusSubscriber
public class DecreaseSizeEffectEventHandler {

	/**
	 * Handle {@linkplain PotionExpiryEvent} at server side.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void handlePotionExpiryEvent(PotionExpiryEvent event) {

		// sync removal of effect to client
		LivingEntity entity = event.getEntityLiving();
		EffectInstance effectInstance = event.getPotionEffect();
		getBassebombeCraft().getNetworkChannel().sendRemoveEffectPacket(entity, effectInstance);
	}

}
