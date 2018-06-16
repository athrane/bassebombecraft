package bassebombecraft.event.entity.team;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isEntityLiving;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/**
 * Event handler for team membership.
 */
@Mod.EventBusSubscriber
public class TeamMembershipEventHandler {

	@SubscribeEvent
	static public void handleEvent(LivingDeathEvent event) {

		// get repository
		TeamRepository repository = getBassebombeCraft().getTeamRepository();

		// exit if not a living entity
		if (!isEntityLiving(event.getEntityLiving())) return;
		
		// remove entity from team upon death
		EntityLiving entity = (EntityLiving ) event.getEntityLiving();			
		repository.remove(entity);
	}
}
