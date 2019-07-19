package bassebombecraft.predicate;

import static bassebombecraft.player.PlayerUtils.isPlayerEntity;

import com.google.common.base.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Discard commander filter. Returns false if candidate is equal to commander.
 */
public class DiscardCommander implements Predicate<LivingEntity> {
	PlayerEntity commander;

	public void set(PlayerEntity commander) {
		this.commander = commander;
	}

	@Override
	public boolean apply(LivingEntity entity) {
		if (commander == null)
			return false;
		if (entity == null)
			return false;

		// exit if not a player
		if (!isPlayerEntity(entity))
			return false;

		// type cast
		PlayerEntity commander = (PlayerEntity) entity;
		
		return (!this.commander.equals(commander));
	}

}