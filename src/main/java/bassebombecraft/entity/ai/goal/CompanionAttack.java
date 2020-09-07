package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_UPDATE_FREQUENCY;
import static bassebombecraft.entity.EntityUtils.getNullableTarget;
import static bassebombecraft.entity.EntityUtils.getTarget;
import static bassebombecraft.entity.EntityUtils.hasTarget;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static java.util.Optional.ofNullable;
import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LightningBoltMist;
import bassebombecraft.item.action.mist.entity.ToxicMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.DigMobHole2;
import bassebombecraft.operator.entity.raytraceresult.SpawnAnvil2;
import bassebombecraft.operator.entity.raytraceresult.SpawnCobweb2;
import bassebombecraft.operator.item.action.ExecuteOperatorAsAction2;
import bassebombecraft.operator.projectile.ShootArrowProjectile2;
import bassebombecraft.operator.projectile.ShootCircleProjectile2;
import bassebombecraft.operator.projectile.ShootFireballProjectile2;
import bassebombecraft.operator.projectile.ShootLargeFireballProjectile2;
import bassebombecraft.operator.projectile.ShootSkullCircleProjectile2;
import bassebombecraft.operator.projectile.ShootWitherSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.formation.TrifurcatedProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import bassebombecraft.projectile.action.EmitHorizontalForce;
import bassebombecraft.projectile.action.EmitVerticalForce;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnFlamingChicken;
import bassebombecraft.projectile.action.SpawnIceBlock;
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
 * 
 * This goal isn't a targeting goal, it is an attack goal (if a target is
 * defined).
 */
public class CompanionAttack extends Goal {

	static final ProjectileAction ICEBLOCK_PROJECTILE_ACTION = new SpawnIceBlock();
	static final ProjectileAction LAVABLOCK_PROJECTILE_ACTION = new SpawnLavaBlock();
	static final ProjectileAction LIGHTNING_PROJECTILE_ACTION = new SpawnLightningBolt();
	static final ProjectileAction EMIT_FORCE_PROJECTILE_ACTION = new EmitHorizontalForce();
	static final ProjectileAction EMIT_VERTICAL_FORCE_PROJECTILE_ACTION = new EmitVerticalForce();
	static final ProjectileAction SPAWN_SQUID_PROJECTILE_ACTION = new SpawnSquid();
	static final ProjectileAction FLAMING_CHICKEN_PROJECTILE_ACTION = new SpawnFlamingChicken();
	static final EntityMistActionStrategy SPAWN_VACUUM_MIST_PROJECTILE_ACTION = new VacuumMist();

	static final EntityMistActionStrategy TOXIC_MIST_STRATEGY = new ToxicMist();
	static final EntityMistActionStrategy LIGHTNING_MIST_STRATEGY = new LightningBoltMist();

	static final String CREEPER_CANNON_CONFIG_KEY = ShootCreeperCannon.class.getSimpleName();
	static final boolean ISNT_PRIMED = false;

	/**
	 * Entity movement speed.
	 */
	double entityMoveSpeed = 1.0D;

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splLargeFireballOps = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootLargeFireballProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splFireballOps = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootFireballProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splWitherSkullOps = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootWitherSkullProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splArrowsOps = () -> {
		Operator2 formationOp = new TrifurcatedProjectileFormation2();
		Operator2 projectileOp = new ShootArrowProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splDigMobHoleOps = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> DigMobHole2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnCobwebOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnCobweb2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnAnvilOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootSkullCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnAnvil2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

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
	 * Facts about a combat situation.
	 */
	SituationalFacts targetFacts = DefaultFacts.getInstance();

	/**
	 * Observation repository for observing combat.
	 */
	ObservationRepository observationRepository;

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity entity that the tasks is applied to.
	 */
	public CompanionAttack(MobEntity entity) {
		this.entity = entity;

		// set "interactive" AI
		setMutexFlags(EnumSet.of(LOOK));

		// create observation repository
		observationRepository = DefaultObservationRepository.getInstance(entity);

	}

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity    entity that the tasks is applied to.
	 * @param commander entity which commands entity.
	 */
	public CompanionAttack(MobEntity entity, PlayerEntity commander) {
		this(entity);
		this.commander = commander;
	}

	@Override
	public boolean shouldExecute() {

		// stop goal execution if no target is defined
		if (!hasTarget(entity)) {
			return false;
		}

		// get target
		Optional<LivingEntity> optTarget = getNullableTarget(entity);

		// exit if target isn't defined (anymore)
		if (!optTarget.isPresent())
			return false;

		// target is defined, observe situation
		observeAndupdateFacts(optTarget.get());

		// continue goal execution if target is alive
		return (optTarget.get().isAlive());
	}

	@Override
	public void tick() {
		try {

			// exit if frequency isn't active
			FrequencyRepository repository = getProxy().getServerFrequencyRepository();
			if (!repository.isActive(AI_COMPANION_ATTACK_UPDATE_FREQUENCY))
				return;

			// get target
			Optional<LivingEntity> optTarget = ofNullable(getTarget(entity));

			// exit if target isn't defined (anymore)
			if (!optTarget.isPresent())
				return;

			// look at target
			lookAtTarget(optTarget.get());

			// navigate to target if entity isn't at minimum range
			PathNavigator navigator = entity.getNavigator();
			if (targetFacts.isTargetClose()) {
				navigator.clearPath();
			} else {
				entity.getNavigator().tryMoveToEntityLiving(optTarget.get(), entityMoveSpeed);
			}

			// select behaviour type based on distance
			if (targetFacts.isTargetClose())
				selectAction(closeRangeActions);
			else
				selectAction(longRangeActions);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void resetTask() {
		observationRepository.clear();
	}

	/**
	 * Process the situation.
	 * 
	 * @param livingEntity target to observe.
	 */
	void observeAndupdateFacts(LivingEntity target) {

		// observe
		Observation observation = observationRepository.observe(target);
		// getBassebombeCraft().getLogger().debug(observation.getObservationAsString());

		// exit if not enough observations are registered
		if (observationRepository.isTooFewObservationsRegistered())
			return;

		// get observations
		Stream<Observation> observations = observationRepository.get();

		// create facts
		targetFacts.update(observations);

		// log
		// getBassebombeCraft().getLogger().debug(targetFacts.getFactsAsString());
		// getProxy().postAiObservation("Attack", observation);
	}

	/**
	 * Initialise list of long range actions.
	 * 
	 * @return list of long range actions.
	 */
	static List<RightClickedItemAction> initializeLongRangeActions() {
		List<RightClickedItemAction> actions = new ArrayList<RightClickedItemAction>();
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splLargeFireballOps.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splFireballOps.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splWitherSkullOps.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splArrowsOps.get()));
		actions.add(new ShootBaconBazooka());
		actions.add(new ShootCreeperCannon(ISNT_PRIMED));
		actions.add(new ShootGenericEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnAnvilOp.get()));
		actions.add(new ShootGenericEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splDigMobHoleOps.get()));
		actions.add(new ShootGenericEggProjectile(FLAMING_CHICKEN_PROJECTILE_ACTION));
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
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnCobwebOp.get()));
		actions.add(new ShootGenericEggProjectile(EMIT_FORCE_PROJECTILE_ACTION));
		actions.add(new ShootGenericEggProjectile(EMIT_VERTICAL_FORCE_PROJECTILE_ACTION));
		actions.add(new ShootGenericEggProjectile(ICEBLOCK_PROJECTILE_ACTION));
		actions.add(new ShootGenericEggProjectile(LAVABLOCK_PROJECTILE_ACTION));
		actions.add(new ShootGenericEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnAnvilOp.get()));
		actions.add(new GenericEntityMist(SPAWN_VACUUM_MIST_PROJECTILE_ACTION));
		actions.add(new ShootGenericEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		actions.add(new GenericEntityMist(LIGHTNING_MIST_STRATEGY));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splDigMobHoleOps.get()));
		return actions;
	}

	/**
	 * Look at target.
	 * 
	 * @param target target to look at.
	 */
	void lookAtTarget(LivingEntity target) {
		LookController lookController = entity.getLookController();
		lookController.setLookPositionWithEntity(target, 10.0F, (float) entity.getVerticalFaceSpeed());
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
