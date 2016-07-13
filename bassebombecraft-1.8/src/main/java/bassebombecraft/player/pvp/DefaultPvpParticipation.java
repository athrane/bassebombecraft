package bassebombecraft.player.pvp;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Default implementation of {@linkplain PvpParticipation}
 */
public class DefaultPvpParticipation implements PvpParticipation {

	private EntityPlayer player;
	private int duration;

	/**
	 * DefaultPvpParticipation constructor.
	 * 
	 * @param player
	 *            player.
	 */
	private DefaultPvpParticipation(EntityPlayer player) {
		this.player = player;
		this.duration = 10;
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void updateDuration() {
		if (duration > 0)
			duration--;
		System.out.println(duration);
	}
	
	@Override
	public void extendParticipation() {
		duration = 100;
	}

	@Override
	public boolean isExpired() {
		return (duration == 0);
	}

	/**
	 * Factory method for creation of a PVP participation info object.
	 * 
	 * @param player
	 *            player.
	 * 
	 * @return particle rendering object.
	 */
	public static PvpParticipation getInstance(EntityPlayer player) {
		return new DefaultPvpParticipation(player);
	}
}
