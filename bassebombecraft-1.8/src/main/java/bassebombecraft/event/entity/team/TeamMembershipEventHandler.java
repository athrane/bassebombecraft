package bassebombecraft.event.entity.team;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isLivingEntity;
import static bassebombecraft.player.PlayerUtils.isEntityPlayer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
		if (isLivingEntity(event.getLivingEntity())) {
			LivingEntity entity = (LivingEntity) event.getLivingEntity();			
			repository.remove(entity);					
			return;
		}
		
		// disband team if dead entity is commander
		if (isEntityPlayer(event.getLivingEntity())) {
			EntityPlayer player = (EntityPlayer ) event.getLivingEntity();			
			repository.deleteTeam(player);
			return;
		}
		
	}
}
