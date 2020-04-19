package bassebombecraft.event.duration;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for duration updates.
 * 
 * The handler executes events CLIENT and SERVER side.
 */
@Mod.EventBusSubscriber
public class DurationEventHandler {

	@SubscribeEvent
	static public void handleWorldTickEvent(WorldTickEvent event) {
		try {
			// get repository
			DurationRepository repository = getProxy().getDurationRepository();

			// update
			repository.update();

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
