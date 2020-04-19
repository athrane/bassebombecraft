package bassebombecraft.event.frequency;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for frequency updates.
 * 
 * The handler executes events CLIENT and SERVER side.
 */
@Mod.EventBusSubscriber
public class FrequencyEventHandler {

	@SubscribeEvent
	static public void handleWorldTickEvent(WorldTickEvent event) {
		try {
			getProxy().getFrequencyRepository().update();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
