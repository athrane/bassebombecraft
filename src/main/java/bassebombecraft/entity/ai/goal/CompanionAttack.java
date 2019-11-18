package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_UPDATE_FREQUENCY;
import static bassebombecraft.entity.EntityUtils.getAliveTarget;
import static bassebombecraft.entity.EntityUtils.hasAliveTarget;
import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.action.ShootLargeFireball;
import bassebombecraft.item.action.ShootMultipleArrows;
import bassebombecraft.item.action.ShootSmallFireball;
import bassebombecraft.item.action.ShootWitherSkull;
import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LightningBoltMist;
import bassebombecraft.item.action.mist.entity.ToxicMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;
import bassebombecraft.projectile.action.DigMobHole;
import bassebombecraft.projectile.action.EmitHorizontalForce;
import bassebombecraft.projectile.action.EmitVerticalForce;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnAnvil;
import bassebombecraft.projectile.action.SpawnCobweb;
import bassebombecraft.projectile.action.SpawnFlamingChicken;
import bassebombecraft.projectile.action.SpawnIceBlock;
import bassebombecraft.projectile.action.SpawnKittenArmy;
import bassebombecraft.projectile.action.SpawnLavaBlock;
import bassebombecraft.projectile.action.SpawnLightningBolt;
import bassebombecraft.projectile.action.SpawnSquid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;

/**
 * AI goal for companion, e.g. charmed mob or guardian.
 * 
 * The goal will attack the targeted mob using the abilities implemented in the
 * BassBombeCraft mod.
 */
public class CompanionAttack extends Goal {

	static final ProjectileAction COWEB_PROJECTILE_ACTION = new SpawnCobweb();
	static final ProjectileAction ICEBLOCK_PROJECTILE_ACTION = new SpawnIceBlock();
	static final ProjectileAction LAVABLOCK_PROJECTILE_ACTION = new SpawnLavaBlock();
	static final ProjectileAction LIGHTNING_PROJECTILE_ACTION = new SpawnLightningBolt();
	static final ProjectileAction EMIT_FORCE_PROJECTILE_ACTION = new EmitHorizontalForce();
	static final ProjectileAction EMIT_VERTICAL_FORCE_PROJECTILE_ACTION = new EmitVerticalForce();
	static final ProjectileAction SPAWN_SQUID_PROJECTILE_ACTION = new SpawnSquid();
	static final ProjectileAction FALLING_ANVIL_PROJECTILE_ACTION = new SpawnAnvil();
	static final ProjectileAction MOB_HOLE_PROJECTILE_ACTION = new DigMobHole();
	static final ProjectileAction KITTEN_ARMY_PROJECTILE_ACTION = new SpawnKittenArmy();
	static final ProjectileAction FLAMING_CHICKEN_PROJECTILE_ACTION = new SpawnFlamingChicken();

	static final EntityMistActionStrategy SPAWN_VACUUM_MIST_PROJECTILE_ACTION = new VacuumMist();
	static final EntityMistActionStrategy TOXIC_MIST_STRATEGY = new ToxicMist();
	static final EntityMistActionStrategy LIGHTNING_MIST_STRATEGY = new LightningBoltMist();

	final static String CREEPER_CANNON_CONFIG_KEY = ShootCreeperCannon.class.getSimpleName();
	final static boolean ISNT_PRIMED = false;

	/**
	 * Attack target.
	 */
	LivingEntity attackTarget;

	/**
	 * Entity movement speed.
	 */
	double entityMoveSpeed = 1.0D;

	/**
	 * Distance to target.
	 */
	double targetDistance;

	/**
	 * List of long range actions.
	 */
	static List<RightClickedItemAction> longRangeActions = initializeLongRangeActions();

	/**
	 * List of close range actions.
	 */
	static List<RightClickedItemAction> closeRangeActions = initializeCloseRangeActions();

	/**
	 * Mob commander.
	 */
	PlayerEntity commander;

	/**
	 * Goal owner.
	 */
	MobEntity entity;

	/**
	 * Target facts.
	 */
	TargetFacts targetFacts;

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity entity that the tasks is applied to.
	 */
	public CompanionAttack(MobEntity entity) {
		this.entity = entity;

		// "interactive" AI
		setMutexFlags(EnumSet.of(LOOK));
	}

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity    entity that the tasks is applied to.
	 * @param commander entity which commands entity.
	 */
	public CompanionAttack(MobEntity entity, PlayerEntity commander) {
		this.commander = commander;
		this.entity = entity;
	}

	@Override
	public boolean shouldExecute() {

		// exit if no living target is defined
		if (!hasAliveTarget(entity)) {
			return false;
		}

		// get candidate
		LivingEntity targetCandidate = getAliveTarget(entity);

		// set target
		attackTarget = targetCandidate;

		return true;
	}

	@Override
	public void startExecuting() {

		// create target facts object
		targetFacts = new TargetFacts(entity, attackTarget);
	}

	@Override
	public void tick() {
		
		// declare AI action 
		String action = "";

		try {

			// exit if frequency isn't active
			FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();
			if (!repository.isActive(AI_COMPANION_ATTACK_UPDATE_FREQUENCY))
				return;

			lookAtTarget();

			// observe target
			targetFacts.observe();

			// navigate to target if entity isn't at minimum range
			PathNavigator navigator = entity.getNavigator();
			if (targetFacts.isTargetClose()) {
				navigator.clearPath();
			} else {
				entity.getNavigator().tryMoveToEntityLiving(attackTarget, entityMoveSpeed);
			}

			doAiObservation("Before-attack");

			// select behaviour type based on distance
			if (targetFacts.isTargetClose())
				action = selectAction(closeRangeActions);
			else
				action = selectAction(longRangeActions);

			doAiObservation("After-attack, ac="+action);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute() || !entity.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		attackTarget = null;
		targetFacts = null;
	}

	/**
	 * 
	 * @param observation
	 */
	void doAiObservation(String observation) {
		String id = targetFacts.getCurrentObservationAsString(observation);
		getBassebombeCraft().getLogger().debug(id);
		// getProxy().postAiObservation("Attack", observation);
	}

	/**
	 * Initialise list of long range actions.
	 * 
	 * @return list of long range actions.
	 */
	static List<RightClickedItemAction> initializeLongRangeActions() {
		List<RightClickedItemAction> actions = new ArrayList<RightClickedItemAction>();
		actions.add(new ShootSmallFireball());
		actions.add(new ShootLargeFireball());
		actions.add(new ShootWitherSkull());
		actions.add(new ShootMultipleArrows());
		actions.add(new ShootBaconBazooka());
		actions.add(new ShootCreeperCannon(ISNT_PRIMED));
		actions.add(new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(FALLING_ANVIL_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(MOB_HOLE_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(KITTEN_ARMY_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(FLAMING_CHICKEN_PROJECTILE_ACTION));
		return actions;
	}

	/**
	 * Initialise list of close range actions.
	 * 
	 * @return list of close range actions.
	 */
	static List<RightClickedItemAction> initializeCloseRangeActions() {
		List<RightClickedItemAction> actions = new ArrayList<RightClickedItemAction>();
		actions.add(new GenericEntityMist(TOXIC_MIST_STRATEGY));
		actions.add(new GenericShootEggProjectile(COWEB_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(EMIT_FORCE_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(EMIT_VERTICAL_FORCE_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(ICEBLOCK_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(LAVABLOCK_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(FALLING_ANVIL_PROJECTILE_ACTION));
		actions.add(new GenericEntityMist(SPAWN_VACUUM_MIST_PROJECTILE_ACTION));
		actions.add(new GenericShootEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		actions.add(new GenericEntityMist(LIGHTNING_MIST_STRATEGY));
		actions.add(new GenericShootEggProjectile(MOB_HOLE_PROJECTILE_ACTION));
		return actions;
	}

	/**
	 * Look at target.
	 */
	void lookAtTarget() {
		// look at target
		LookController lookController = entity.getLookController();
		lookController.setLookPositionWithEntity(attackTarget, 10.0F, (float) entity.getVerticalFaceSpeed());
	}

	/**
	 * Select and perform attack action toward target mob.
	 * 
	 * Mists are not used due to update problems.
	 * 
	 * @param actions list of candidate actions
	 * 
	 * @return chosen action.
	 */
	String selectAction(List<RightClickedItemAction> actions) {
		Random random = getBassebombeCraft().getRandom();
		int numberActions = actions.size();
		int choice = random.nextInt(numberActions);
		RightClickedItemAction action = actions.get(choice);
		action.onRightClick(entity.getEntityWorld(), entity);
		return action.getClass().getSimpleName();
	}

}
