package bassebombecraft.event.entity.team;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
		if (isTypeLivingEntity(event.getEntityLiving())) {
			LivingEntity entity = event.getEntityLiving();
			repository.remove(entity);					
			return;
		}
		
		// disband team if dead entity is commander
		if (isTypePlayerEntity(event.getEntityLiving())) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();			
			repository.deleteTeam(player);
			return;
		}
		
	}
}