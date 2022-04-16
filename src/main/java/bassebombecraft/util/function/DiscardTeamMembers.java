package bassebombecraft.util.function;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import com.google.common.base.Predicate;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.world.entity.LivingEntity;

/**
 * Discard team members filter
 */
public class DiscardTeamMembers implements Predicate<LivingEntity> {

	/**
	 * Team member.
	 */
	LivingEntity entity;

	public void set(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public boolean apply(LivingEntity candidateMember) {
		try {
			if (this.entity == null)
				return false;
			if (candidateMember == null)
				return false;

			// verify if members of the same team
			TeamRepository repository = getProxy().getServerTeamRepository();
			return (!repository.isTeamMembers(this.entity, candidateMember));

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);

			// return not team members, since we had an error
			return false;
		}
	}

}