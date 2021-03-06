package bassebombecraft.client.event.duration;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for duration updates.
 * 
 * The handler executes events CLIENT side.
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class DurationEventHandler {

	@SubscribeEvent
	static public void handleClientTickEvent(ClientTickEvent event) {
		try {
			// exit if not in start phase
			if (event.phase != Phase.START)
				return;
			getProxy().getClientDurationRepository().update();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
