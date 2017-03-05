package bassebombecraft.predicate;

import com.google.common.base.Predicate;

import static bassebombecraft.player.PlayerUtils.isEntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Discard commander filter. Returns false if candidate is equal to commander.
 */
public class DiscardCommander implements Predicate<EntityLivingBase> {
	EntityPlayer commander;

	public void set(EntityPlayer commander) {
		this.commander = commander;
	}

	@Override
	public boolean apply(EntityLivingBase entity) {
		if (commander == null)
			return false;
		if (entity == null)
			return false;

		// exit if not a player
		if (!isEntityPlayer(entity))
			return false;

		// type cast
		EntityPlayer commander = (EntityPlayer) entity;
		
		return (!this.commander.equals(commander));
	}

}