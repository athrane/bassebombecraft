package bassebombecraft.event.duration;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for duration updates.
 * 
 * The handler executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class DurationEventHandler {

	@SubscribeEvent
	static public void handleServerTickEvent(ServerTickEvent event) {
		try {
			// exit if not in start phase
			if (event.phase != Phase.START)
				return;
			getProxy().getServerDurationRepository().update();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
