package bassebombecraft.predicate;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import com.google.common.base.Predicate;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.LivingEntity;

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
		if (this.entity == null)
			return false;
		if (candidateMember == null)
			return false;

		// verify if members of the same team
		TeamRepository repository = getBassebombeCraft().getTeamRepository();
		return (!repository.isTeamMembers(this.entity, candidateMember));
	}

}