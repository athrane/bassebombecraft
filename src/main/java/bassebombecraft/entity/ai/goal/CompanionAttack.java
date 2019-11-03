package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_MINIMUM_RANGE;
import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_UPDATE_FREQUENCY;
import static bassebombecraft.entity.EntityUtils.getAliveTarget;
import static bassebombecraft.entity.EntityUtils.hasAliveTarget;
import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
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
import bassebombecraft.proxy.Proxy;
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
	 * Observation counter..
	 */
	int observationCounter = 0;

	/**
	 * List of long range actions.
	 */
	static ArrayList<RightClickedItemAction> longRangeActions;

	/**
	 * List of close range actions.
	 */
	static ArrayList<RightClickedItemAction> closeRangeActions;

	/**
	 * Mob commander.
	 */
	PlayerEntity commander;

	/**
	 * Goal owner.
	 */
	MobEntity entity;

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity entity that the tasks is applied to.
	 */
	public CompanionAttack(MobEntity entity) {
		this.entity = entity;

		// "interactive" AI
		setMutexFlags(EnumSet.of(LOOK));

		getCloseRangeActions();
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
		if (!hasAliveTarget(entity))
			return false;

		// get candidate
		LivingEntity targetCandidate = getAliveTarget(entity);

		// set target
		attackTarget = targetCandidate;
		return true;
	}

	@Override
	public void startExecuting() {
		// NO-OP
	}

	@Override
	public void tick() {
		String action = "";

		try {

			// get repository
			FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();

			// exit if frequency isn't active
			if (!repository.isActive(AI_COMPANION_ATTACK_UPDATE_FREQUENCY))
				return;

			// look at target
			LookController lookController = entity.getLookController();
			lookController.setLookPositionWithEntity(attackTarget, 10.0F, (float) entity.getVerticalFaceSpeed());

			// calculate distance to target
			boolean isTargetClose = isTargetClose();

			// navigate to target if entity isn't at minimum range
			PathNavigator navigator = entity.getNavigator();
			if (isTargetClose) {
				navigator.clearPath();
			} else {
				entity.getNavigator().tryMoveToEntityLiving(attackTarget, entityMoveSpeed);
			}

			// add AI observation event
			// type, position, health
			// doAiObservation("Before-attack");

			// select behaviour type based on distance
			if (isTargetClose)
				action = selectAction(getCloseRangeActions());
			else
				action = selectAction(getLongRangeActions());

			// increase observation counter
			observationCounter++;
			// doAiObservation("After-attack");

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Return whether target is close, i.e. within the minimum attack range.
	 * 
	 * @return true if target is close.
	 */
	boolean isTargetClose() {
		double distance = entity.getDistanceSq(attackTarget.posX, attackTarget.getBoundingBox().minY,
				attackTarget.posZ);
		return (distance < AI_COMPANION_ATTACK_MINIMUM_RANGE);
	}

	/**
	 * Select and perform attack action toward target mob.
	 * 
	 * Mists are not used due to update problems.
	 * 
	 * @param actions list af candidate actions
	 * 
	 * @return chosen action.
	 */
	String selectAction(ArrayList<RightClickedItemAction> actions) {
		Random random = getBassebombeCraft().getRandom();
		int numberActions = actions.size();
		int choice = random.nextInt(numberActions);
		RightClickedItemAction action = actions.get(choice);
		action.onRightClick(entity.getEntityWorld(), entity);
		return action.getClass().getSimpleName();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute() || !entity.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		attackTarget = null;
	}

	void doAiObservation(String observation) {
		String id = new StringBuilder().append(this.hashCode()).append("-").append(observationCounter).append("-")
				.append(observation).toString();

		System.out.println("observation-id=" + id);
		System.out.println("source.name=" + entity.getName());
		System.out.println("source.health=" + entity.getHealth());
		System.out.println("source.position=" + entity.getPosition().toLong());
		System.out.println("target.name=" + attackTarget.getName());
		System.out.println("target.health=" + attackTarget.getHealth());
		System.out.println("target.position=" + attackTarget.getPosition().toLong());

		Proxy proxy = getBassebombeCraft().getProxy();
		proxy.postAiObservation("Attack", observation);
	}

	/**
	 * Get instance of list of long range actions.
	 * 
	 * @return list of long range actions.
	 */
	static ArrayList<RightClickedItemAction> getLongRangeActions() {
		Optional<ArrayList<RightClickedItemAction>> optActions = Optional.ofNullable(longRangeActions);
		if (optActions.isPresent())
			return longRangeActions;

		longRangeActions = new ArrayList<RightClickedItemAction>();
		longRangeActions.add(new ShootSmallFireball());
		longRangeActions.add(new ShootLargeFireball());
		longRangeActions.add(new ShootWitherSkull());
		longRangeActions.add(new ShootMultipleArrows());
		longRangeActions.add(new ShootBaconBazooka());
		longRangeActions.add(new ShootCreeperCannon(ISNT_PRIMED));
		longRangeActions.add(new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(FALLING_ANVIL_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(MOB_HOLE_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(KITTEN_ARMY_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(FLAMING_CHICKEN_PROJECTILE_ACTION));
		return longRangeActions;
	}

	/**
	 * Get instance of list of close range actions.
	 * 
	 * @return list of close range actions.
	 */
	static ArrayList<RightClickedItemAction> getCloseRangeActions() {
		Optional<ArrayList<RightClickedItemAction>> optActions = Optional.ofNullable(closeRangeActions);
		if (optActions.isPresent())
			return closeRangeActions;

		closeRangeActions = new ArrayList<RightClickedItemAction>();
		closeRangeActions.add(new GenericEntityMist(TOXIC_MIST_STRATEGY));
		closeRangeActions.add(new GenericShootEggProjectile(COWEB_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(EMIT_FORCE_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(EMIT_VERTICAL_FORCE_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(ICEBLOCK_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(LAVABLOCK_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(FALLING_ANVIL_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericEntityMist(SPAWN_VACUUM_MIST_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericEntityMist(LIGHTNING_MIST_STRATEGY));
		closeRangeActions.add(new GenericShootEggProjectile(MOB_HOLE_PROJECTILE_ACTION));
		return closeRangeActions;
	}

}
