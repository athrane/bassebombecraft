package bassebombecraft.event.entity.target;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		EntityLivingBase entity = event.getEntityLiving();
		repository.remove(entity);

	}
}
