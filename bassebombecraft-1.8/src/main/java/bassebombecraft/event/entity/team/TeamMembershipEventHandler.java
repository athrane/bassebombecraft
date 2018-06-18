package bassebombecraft.event.entity.team;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isEntityLiving;
import static bassebombecraft.player.PlayerUtils.*;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
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

		// remove living entity from team upon death
		if (isEntityLiving(event.getEntityLiving())) {
			EntityLiving entity = (EntityLiving) event.getEntityLiving();			
			repository.remove(entity);					
			return;
		}
		
		// disband team if dead entity is commander
		if (isEntityPlayer(event.getEntityLiving())) {
			EntityPlayer player = (EntityPlayer ) event.getEntityLiving();			
			repository.deleteTeam(player);
			return;
		}
		
	}
}
