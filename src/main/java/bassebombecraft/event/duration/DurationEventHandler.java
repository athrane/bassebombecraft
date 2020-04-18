package bassebombecraft.event.duration;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for duration updates.
 * 
 * The handler only executes events SERVER side. 
 */
@Mod.EventBusSubscriber
public class DurationEventHandler {

	@SubscribeEvent
	static public void handlePlayerTickEvent(WorldTickEvent event) {

		// exit if handler is executed at client side
		if(isWorldAtClientSide(event.world)) return;
		
		// get repository
		DurationRepository repository = getBassebombeCraft().getDurationRepository();

		// update
		repository.update();
	}
}
