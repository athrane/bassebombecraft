package bassebombecraft.entity.commander;

import static bassebombecraft.player.PlayerUtils.sendChatMessageToPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bassebombecraft.entity.commander.command.AttackCommandersTarget;
import bassebombecraft.entity.commander.command.NullCommand;
import bassebombecraft.entity.commander.command.Wait;
import net.minecraft.entity.player.EntityPlayer;

public class DefaultMobCommanderRepository implements MobCommanderRepository {

	/**
	 * Null mob command.
	 */
	public final static MobCommand NULL_COMMAND = new NullCommand();

	/**
	 * Mob commander state
	 */
	class MobCommanderState {

		/**
		 * Mob command.
		 */
		MobCommand command;

		/**
		 * MobCommanderState constructor.
		 * 
		 * @param command
		 *            initial command.
		 */
		public MobCommanderState(MobCommand command) {
			this.command = command;
		}

		public MobCommand getCommand() {
			return command;
		}

		public void setCommand(MobCommand command) {
			this.command = command;
		}

	}

	/**
	 * Commander container.
	 */
	Map<EntityPlayer, MobCommanderState> commanders;

	/**
	 * DefaultMobCommanderRepository constructor.
	 */
	public DefaultMobCommanderRepository() {
		super();
		this.commanders = Collections.synchronizedMap(new HashMap<EntityPlayer, MobCommanderState>());
	}

	@Override
	public boolean isRegistered(EntityPlayer player) {
		if (player == null)
			return false;
		return (commanders.containsKey(player));
	}

	@Override
	public void register(EntityPlayer player) {
		if (player == null)
			return;
		if (isRegistered(player)) {
			return;
		}

		// add new commander
		MobCommanderState state = new MobCommanderState(NULL_COMMAND);
		commanders.put(player, state);
	}

	@Override
	public void remove(EntityPlayer player) {
		if (player == null)
			return;
		commanders.remove(player);
	}

	@Override
	public void clear() {
		commanders.clear();
	}

	@Override
	public MobCommand getCommand(EntityPlayer player) {
		if (player == null)
			return NULL_COMMAND;

		if (!isRegistered(player))
			return NULL_COMMAND;

		// return current command.
		MobCommanderState state = commanders.get(player);
		return state.getCommand();
	}

	@Override
	public void cycle(EntityPlayer player) {
		register(player);

		// get command
		MobCommanderState state = commanders.get(player);
		MobCommand command = state.getCommand();

		internalCycleCommand(state, command);
		
		// update GUI
		command = state.getCommand();		
		sendChatMessageToPlayer(player, "Active mob command is: " + command.getTitle());
		
	}

	void internalCycleCommand(MobCommanderState state, MobCommand command) {
		switch (command.getType()) {

			case NULL: {
				state.setCommand(new Wait());
				return;
			}

			case WAIT: {
				state.setCommand(new AttackCommandersTarget());
				return;
			}
			
			case COMMANDERS_TARGET: {
				state.setCommand(NULL_COMMAND);
				return;
			}
			
			default: {
				state.setCommand(NULL_COMMAND);
				return;				
			}
		}
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static MobCommanderRepository getInstance() {
		return new DefaultMobCommanderRepository();
	}
}
