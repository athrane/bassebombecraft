package bassebombecraft.event.frequency;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for frequency updates.
 * 
 * The handler executes events CLIENT and SERVER side.
 * 
 * It is used SERVER side on other event handlers. It is used CLIENT for
 * rendering.
 */
@Mod.EventBusSubscriber
public class FrequencyEventHandler {

	@SubscribeEvent
	static public void handlePlayerTickEvent(PlayerTickEvent event) {

		// get repository
		FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();

		// update
		repository.update();
	}
}
