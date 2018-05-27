package bassebombecraft.event.entity.team;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Event handler for team membership.
 */
@Mod.EventBusSubscriber
public class TeamMembershipEventHandler {

	@SubscribeEvent
	static public void onPlayerTick(LivingDeathEvent event) {

		// get repository
		TeamRepository repository = getBassebombeCraft().getTeamRepository();

		// remove entity from team upon death
		EntityLivingBase entity = event.getEntityLiving();
		repository.remove(entity);

	}
}
