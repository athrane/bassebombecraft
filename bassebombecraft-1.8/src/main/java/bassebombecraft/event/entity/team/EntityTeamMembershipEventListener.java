package bassebombecraft.event.entity.team;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Entity team membership handler event listener.
 */
public class EntityTeamMembershipEventListener {

	/**
	 * Team repository.
	 */
	TeamRepository repository;

	/**
	 * EntityTeamMembershipEventListener constructor.
	 * 
	 * @param repository
	 *            team repository.
	 */
	public EntityTeamMembershipEventListener(TeamRepository repository) {
		this.repository = repository;
	}

	@SubscribeEvent
	public void onPlayerTick(LivingDeathEvent event) {

		EntityLivingBase entity = event.getEntityLiving();
		repository.remove(entity);

	}
}
