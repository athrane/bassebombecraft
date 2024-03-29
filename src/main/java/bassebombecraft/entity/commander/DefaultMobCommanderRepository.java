package bassebombecraft.entity.commander;

import static bassebombecraft.player.PlayerUtils.sendChatMessageToPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bassebombecraft.entity.commander.command.AttackCommandersTargetCommand;
import bassebombecraft.entity.commander.command.AttackNearestMobCommand;
import bassebombecraft.entity.commander.command.AttackNearestPlayerCommand;
import bassebombecraft.entity.commander.command.DanceCommand;
import bassebombecraft.entity.commander.command.NullCommand;
import static bassebombecraft.player.PlayerUtils.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DefaultMobCommanderRepository implements MobCommanderRepository {

	/**
	 * Attack nearest mob command.
	 */
	static final AttackNearestMobCommand ATTACK_NEAREST_MOB_COMMAND = new AttackNearestMobCommand();

	/**
	 * Attack nearest player command.
	 */
	static final AttackNearestPlayerCommand ATTACK_NEAREST_PLAYER_COMMAND = new AttackNearestPlayerCommand();

	/**
	 * Stop command.
	 */
	static final DanceCommand STOP_COMMAND = new DanceCommand();

	/**
	 * Attack commanders target command.
	 */
	static final AttackCommandersTargetCommand ATTACK_COMMANDERS_TARGET_COMMAND = new AttackCommandersTargetCommand();

	/**
	 * Null mob command.
	 */
	public static final MobCommand NULL_COMMAND = new NullCommand();

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
		 * @param command initial command.
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
	Map<LivingEntity, MobCommanderState> commanders;

	/**
	 * DefaultMobCommanderRepository constructor.
	 */
	public DefaultMobCommanderRepository() {
		super();
		this.commanders = Collections.synchronizedMap(new HashMap<LivingEntity, MobCommanderState>());
	}

	@Override
	public boolean isRegistered(LivingEntity entity) {
		if (entity == null)
			return false;
		return (commanders.containsKey(entity));
	}

	@Override
	public void register(LivingEntity entity) {
		if (entity == null)
			return;
		if (isRegistered(entity)) {
			return;
		}

		// add new commander
		MobCommanderState state = new MobCommanderState(NULL_COMMAND);
		commanders.put(entity, state);
	}

	@Override
	public void remove(LivingEntity entity) {
		if (entity == null)
			return;
		commanders.remove(entity);
	}

	@Override
	public void clear() {
		commanders.clear();
	}

	@Override
	public MobCommand getCommand(LivingEntity entity) {
		if (entity == null)
			return NULL_COMMAND;

		if (!isRegistered(entity))
			return NULL_COMMAND;

		// return current command.
		MobCommanderState state = commanders.get(entity);
		return state.getCommand();
	}

	@Override
	public void cycle(LivingEntity entity) {
		register(entity);

		// get command
		MobCommanderState state = commanders.get(entity);
		MobCommand command = state.getCommand();

		internalCycleCommand(state, command);

		// update GUI
		command = state.getCommand();

		// if commander is player entity then send chat message
		if (isTypePlayerEntity(entity)) {

			// type cast
			Player player = (Player) entity;

			// send message
			sendChatMessageToPlayer(player, "Krenko commands: " + command.getTitle());
		}

	}

	void internalCycleCommand(MobCommanderState state, MobCommand command) {
		switch (command.getType()) {

		case NULL: {
			state.setCommand(STOP_COMMAND);
			return;
		}

		case STOP: {
			state.setCommand(ATTACK_COMMANDERS_TARGET_COMMAND);
			return;
		}

		case COMMANDERS_TARGET: {
			state.setCommand(ATTACK_NEAREST_MOB_COMMAND);
			return;
		}

		case NEAREST_MOB: {
			state.setCommand(ATTACK_NEAREST_PLAYER_COMMAND);
			return;
		}

		case NEAREST_PLAYER: {
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
