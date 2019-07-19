package bassebombecraft.event.entity.target;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for handling targeted entities.
 */
@Mod.EventBusSubscriber
public class TargetedEntitiesEventHandler {

	@SubscribeEvent
	static public void handleEvent(LivingDeathEvent event) {

		// get repository
		TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();

		// remove entity from team upon death
		LivingEntity entity = event.getLivingEntity();
		repository.remove(entity);

	}
}
