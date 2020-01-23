package bassebombecraft.event.duration;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for duration updates.
 */
@Mod.EventBusSubscriber
public class DurationEventHandler {

	@SubscribeEvent
	static public void handlePlayerTickEvent(WorldTickEvent event) {

		// get repository
		DurationRepository repository = getBassebombeCraft().getDurationRepository();

		// update
		repository.update();
	}
}
