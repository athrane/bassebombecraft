package bassebombecraft.player.pvp;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * Event listener for updating PVP participation.
 */
public class PvpEventListener {

	/**
	 * PVP repository.
	 */
	PvpRepository repository;

	/**
	 * PvpEventListener constructor.
	 * 
	 * @param repository
	 *            PVP repository.
	 */
	public PvpEventListener(PvpRepository repository) {
		super();
		this.repository = repository;
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		repository.updatePlayerParticipation();
	}

}
