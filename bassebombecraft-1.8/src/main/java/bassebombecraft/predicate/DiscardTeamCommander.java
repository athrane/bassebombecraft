package bassebombecraft.predicate;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import com.google.common.base.Predicate;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.EntityLivingBase;

/**
 * Discard team commander filter
 */
public class DiscardTeamCommander implements Predicate<EntityLivingBase> {
	EntityLivingBase entity;

	public void set(EntityLivingBase entity) {
		this.entity = entity;
	}

	@Override
	public boolean apply(EntityLivingBase input) {
		if (this.entity == null)
			return false;
		if (input == null)
			return false;

		// verify if members of the same team
		TeamRepository repository = getBassebombeCraft().getTeamRepository();
		return (!repository.isTeamMembers(this.entity, input));
	}

}