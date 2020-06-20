package bassebombecraft.event.frequency;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for frequency updates.
 * 
 * The handler executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class FrequencyEventHandler {

	@SubscribeEvent
	static public void handleServerTickEvent(ServerTickEvent event) {
		try {
			// exit if not in start phase
			if (event.phase != Phase.START)
				return;
			getProxy().getServerFrequencyRepository().update();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
