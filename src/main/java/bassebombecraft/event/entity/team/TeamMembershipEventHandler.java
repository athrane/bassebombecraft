package bassebombecraft.event.entity.team;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for team membership.
 * 
 * The handler only executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class TeamMembershipEventHandler {

	@SubscribeEvent
	static public void handleLivingDeathEvent(LivingDeathEvent event) {
		try {
			// exit if handler is executed at client side
			if (isLogicalClient(event.getEntity()))
				return;

			// get repository
			TeamRepository repository = getProxy().getTeamRepository(event.getEntity().getEntityWorld());

			// remove living entity from team upon death
			if (isTypeLivingEntity(event.getEntityLiving())) {
				LivingEntity entity = event.getEntityLiving();
				repository.remove(entity);
				return;
			}

			// disband team if dead entity is commander
			if (isTypePlayerEntity(event.getEntityLiving())) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();

				// exit if player isn't a commander
				if (!repository.isCommander(player))
					return;

				// disband team
				repository.deleteTeam(player);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@SubscribeEvent
	static public void handlePlayerLoggedInEvent(PlayerLoggedInEvent event) {
		try {
			// exit if handler is executed at client side
			if (isLogicalClient(event.getEntity()))
				return;

			// register team
			TeamRepository repository = getProxy().getTeamRepository(event.getEntity().getEntityWorld());
			repository.createTeam(event.getPlayer());

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@SubscribeEvent
	static public void handlePlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
		try {
			// exit if handler is executed at client side
			if (isLogicalClient(event.getEntity()))
				return;

			// disband team
			TeamRepository repository = getProxy().getTeamRepository(event.getEntity().getEntityWorld());
			repository.deleteTeam(event.getPlayer());

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
