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
import static net.minecraft.world.entity.ai.goal.Goal.Flag.LOOK;

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
import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LightningBoltMist;
import bassebombecraft.item.action.mist.entity.ToxicMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.raytraceresult.DigMobHole2;
import bassebombecraft.operator.entity.raytraceresult.EmitHorizontalForce2;
import bassebombecraft.operator.entity.raytraceresult.EmitVerticalForce2;
import bassebombecraft.operator.entity.raytraceresult.SpawnAnvil2;
import bassebombecraft.operator.entity.raytraceresult.SpawnCobweb2;
import bassebombecraft.operator.entity.raytraceresult.SpawnFlamingChicken2;
import bassebombecraft.operator.entity.raytraceresult.SpawnIceBlock2;
import bassebombecraft.operator.entity.raytraceresult.SpawnLavaBlock2;
import bassebombecraft.operator.entity.raytraceresult.SpawnLightning2;
import bassebombecraft.operator.entity.raytraceresult.SpawnSquid2;
import bassebombecraft.operator.item.action.ExecuteOperatorAsAction2;
import bassebombecraft.operator.projectile.ShootArrowProjectile2;
import bassebombecraft.operator.projectile.ShootCircleProjectile2;
import bassebombecraft.operator.projectile.ShootFireballProjectile2;
import bassebombecraft.operator.projectile.ShootLargeFireballProjectile2;
import bassebombecraft.operator.projectile.ShootWitherSkullProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import bassebombecraft.operator.projectile.formation.TrifurcatedProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

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
	static Supplier<Operator2> splLargeFireballOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootLargeFireballProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splFireballOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootFireballProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splWitherSkullOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootWitherSkullProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splArrowsOp = () -> {
		Operator2 formationOp = new TrifurcatedProjectileFormation2();
		Operator2 projectileOp = new ShootArrowProjectile2();
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splDigMobHoleOp = () -> {
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
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnCobweb2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnIceBlockOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnIceBlock2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnLavaBlockOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnLavaBlock2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnAnvilOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnAnvil2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splEmitHorizontalForceOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(),
				p -> EmitHorizontalForce2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splEmitVerticalForceOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(),
				p -> EmitVerticalForce2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnLigthningOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnLightning2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnSquidOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> SpawnSquid2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splSpawnFlamingChickenOp = () -> {
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(),
				p -> SpawnFlamingChicken2.NAME);
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
	Player commander;

	/**
	 * Goal owner.
	 */
	Mob entity;

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
	public CompanionAttack(Mob entity) {
		this.entity = entity;

		// set "interactive" AI
		setFlags(EnumSet.of(LOOK));

		// create observation repository
		observationRepository = DefaultObservationRepository.getInstance(entity);

	}

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity    entity that the tasks is applied to.
	 * @param commander entity which commands entity.
	 */
	public CompanionAttack(Mob entity, Player commander) {
		this(entity);
		this.commander = commander;
	}

	@Override
	public boolean canUse() {

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
			PathNavigation navigator = entity.getNavigation();
			if (targetFacts.isTargetClose()) {
				navigator.stop();
			} else {
				entity.getNavigation().moveTo(optTarget.get(), entityMoveSpeed);
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
	public void stop() {
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
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splLargeFireballOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splFireballOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splWitherSkullOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splArrowsOp.get()));
		actions.add(new ShootBaconBazooka());
		actions.add(new ShootCreeperCannon(ISNT_PRIMED));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnSquidOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnAnvilOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnLigthningOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splDigMobHoleOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnFlamingChickenOp.get()));
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
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splEmitHorizontalForceOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splEmitVerticalForceOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnIceBlockOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnLavaBlockOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnSquidOp.get()));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnAnvilOp.get()));
		actions.add(new GenericEntityMist(SPAWN_VACUUM_MIST_PROJECTILE_ACTION));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splSpawnLigthningOp.get()));
		actions.add(new GenericEntityMist(LIGHTNING_MIST_STRATEGY));
		actions.add(ExecuteOperatorAsAction2.getInstance(getInstance(), splDigMobHoleOp.get()));
		return actions;
	}

	/**
	 * Look at target.
	 * 
	 * @param target target to look at.
	 */
	void lookAtTarget(LivingEntity target) {
		LookControl lookController = entity.getLookControl();
		lookController.setLookAt(target, 10.0F, (float) entity.getMaxHeadXRot());
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
		action.onRightClick(entity.getCommandSenderWorld(), entity);
		return action.getClass().getSimpleName();
	}

}
