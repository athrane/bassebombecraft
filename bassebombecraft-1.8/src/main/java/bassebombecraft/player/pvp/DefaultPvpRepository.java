package bassebombecraft.player.pvp;

import static bassebombecraft.player.PlayerUtils.sendChatMessageToPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Default implementation of the {@linkplain PvpRepository}.
 */
public class DefaultPvpRepository implements PvpRepository {

	/**
	 * Participant container.
	 */
	Map<EntityPlayer, PvpParticipation> participants;

	/**
	 * DefaultPvpRepository constructor.
	 */
	public DefaultPvpRepository() {
		super();
		this.participants = Collections.synchronizedMap(new HashMap<EntityPlayer, PvpParticipation>());
	}

	@Override
	public boolean isRegisteredForPvp(EntityPlayer player) {
		return (participants.containsKey(player));
	}

	@Override
	public void participate(EntityPlayer player) {
		if (isRegisteredForPvp(player)) {

			// get PVP participant
			PvpParticipation participant = participants.get(player);
			participant.extendParticipation();
			return;
		}

		// add new participant
		PvpParticipation participant = DefaultPvpParticipation.getInstance(player);
		add(participant);
	}

	/**
	 * Add participant to PVP.
	 * 
	 * @param participant
	 *            to add to PVP.
	 */
	void add(PvpParticipation participant) {
		if (participant == null)
			return;

		EntityPlayer player = participant.getPlayer();
		if (isRegisteredForPvp(player))
			return;

		// register
		participants.put(player, participant);
		sendChatMessageToPlayer(player, "Player entered PVP: " + player.getName());
	}

	@Override
	public void remove(PvpParticipation participant) {
		if (participant == null)
			return;

		EntityPlayer player = participant.getPlayer();
		participants.remove(player);
	}

	@Override
	public boolean isPvpActive() {
		return (!participants.isEmpty());
	}

	@Override
	public void clear() {
		participants.clear();
	}

	@Override
	public void updatePlayerParticipation() {
		synchronized (participants) {

			for (Iterator<EntityPlayer> it = participants.keySet().iterator(); it.hasNext();) {
				EntityPlayer player = it.next();
				PvpParticipation participant = participants.get(player);

				participant.updateDuration();

				// remove if expired
				if (participant.isExpired()) {
					it.remove();
					sendChatMessageToPlayer(player, "Player left PVP: " + player.getName());
				}

			}
		}
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static PvpRepository getInstance() {
		return new DefaultPvpRepository();
	}
}
