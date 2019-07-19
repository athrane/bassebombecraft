package bassebombecraft.event.frequency;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * Event handler for frequency updates.
 */
@Mod.EventBusSubscriber
public class FrequencyEventHandler {

	@SubscribeEvent
	static public void handleEvent(PlayerTickEvent event) {

		// get repository
		FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();

		// update
		repository.update();
	}
}
