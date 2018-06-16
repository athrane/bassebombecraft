package bassebombecraft.predicate;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import com.google.common.base.Predicate;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.EntityLiving;

/**
 * Discard team members filter
 */
public class DiscardTeamMembers implements Predicate<EntityLiving> {
	
	/** 
	 * Team member.
	 */
	EntityLiving entity;

	public void set(EntityLiving entity) {
		this.entity = entity;
	}

	@Override
	public boolean apply(EntityLiving candidateMember) {
		if (this.entity == null)
			return false;
		if (candidateMember == null)
			return false;

		// verify if members of the same team
		TeamRepository repository = getBassebombeCraft().getTeamRepository();
		return (!repository.isTeamMembers(this.entity, candidateMember));
	}

}