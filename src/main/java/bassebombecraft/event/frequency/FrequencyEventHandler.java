package bassebombecraft.event.frequency;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for frequency updates.
 * 
 * The handler executes events CLIENT and SERVER side.
 * 
 * It is used SERVER side on other event handlers. It is used CLIENT side for
 * rendering.
 */
@Mod.EventBusSubscriber
public class FrequencyEventHandler {

	@SubscribeEvent
	static public void handlePlayerTickEvent(PlayerTickEvent event) {
		try {

			// get repository
			FrequencyRepository repository = getProxy().getFrequencyRepository();

			// update
			repository.update();

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
