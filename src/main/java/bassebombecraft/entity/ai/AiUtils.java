package bassebombecraft.entity.ai;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.event.charm.CharmedMob.IS_EXPIRED;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static net.minecraft.world.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE;
import static net.minecraft.world.entity.ai.goal.Goal.Flag.TARGET;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.ai.goal.AttackInRangeGoal;
import bassebombecraft.entity.ai.goal.ChargeTowardsGoal;
import bassebombecraft.entity.ai.goal.CommanderControlledTargeting;
import bassebombecraft.entity.ai.goal.CommandersTargetGoal;
import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.entity.ai.goal.FollowEntityGoal;
import bassebombecraft.entity.ai.goal.SelfdestructWhenTargetDiesGoal;
import bassebombecraft.event.duration.DurationRepository;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.ai.goal.AttackInRangeGoal;
import bassebombecraft.entity.ai.goal.ChargeTowardsGoal;
import bassebombecraft.entity.ai.goal.CommanderControlledTargeting;
import bassebombecraft.entity.ai.goal.CommandersTargetGoal;
import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.entity.ai.goal.FollowEntityGoal;
import bassebombecraft.entity.ai.goal.SelfdestructWhenTargetDiesGoal;
import bassebombecraft.event.duration.DurationRepository;
import net.minecraft.world.entity.CreatureEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtGoal;
import net.minecraft.world.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.PrioritizedGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.SitGoal;
import net.minecraft.world.entity.ai.goal.SwimGoal;
import net.minecraft.world.entity.monster.CreeperEntity;
import net.minecraft.world.entity.monster.SkeletonEntity;
import net.minecraft.world.entity.passive.CatEntity;
import net.minecraft.world.entity.player.PlayerEntity;

/**
 * AI utility class.
 */
public class AiUtils {

	/**
	 * Initial goal priority.
	 */
	static final int INITIAL_GOAL_PRIORITY = 0;

	/**
	 * movement speed towards player.
	 */
	static final double AI_MOVE_SPEED = 1.5D;

	/**
	 * Entity minimum distance to player.
	 */
	static final float AI_MIN_DIST = 6.0F;

	/**
	 * Entity maximum distance to player.
	 */
	static final float AI_MAX_DIST = 50.0F;

	/**
	 * Minimum charge distance for parrots.
	 */
	static final int AI_MIN_CHARGE_DISTANCE = 1;

	/**
	 * Logger.
	 */
	static Logger logger = LogManager.getLogger(BassebombeCraft.class);

	/**
	 * Capture goals from {@linkplain GoalSelector}.
	 * 
	 * Goals are accessed using the private "goals" field in the goal selector. The
	 * visibility of the field is modified using the Forge accesstransformer.cfg.
	 * 
	 * @param selector goal selector to get goals from.
	 * 
	 * @return set of goals from goals selector.
	 */
	public static Set<WrappedGoal> captureGoals(GoalSelector selector) {
		try {
			return selector.availableGoals;
		} catch (Exception e) {
			logger.error("Failed to capture goals due to the error: " + e.getMessage());
			getBassebombeCraft().reportException(e);

			// return null set
			return Sets.newLinkedHashSet();
		}
	}

	/**
	 * Clear passive and fighting AI goals.
	 * 
	 * @param entity whose goals are cleared.
	 */
	public static void clearAllAiGoals(Mob entity) {
		removeGoals(entity.goalSelector);
		removeGoals(entity.targetSelector);
	}

	/**
	 * Remove goals in a concurrently safe way.
	 * 
	 * Goals are accessed using the private "goals" field in the goal selector. The
	 * visibility of the field is modified using the Forge accesstransformer.cfg.
	 * 
	 * @param selector AI goal selector.
	 */
	static void removeGoals(GoalSelector selector) {
		try {
			Set<WrappedGoal> goals = selector.availableGoals;
			goals.forEach(g -> selector.removeGoal(g));
		} catch (Exception e) {
			logger.error("Failed to remove goals due to the error: " + e.getMessage());
			getBassebombeCraft().reportException(e);
		}
	}

	/**
	 * Assign passive AI goals.
	 * 
	 * Goals are accessed using the private "goals" field in the goal selector. The
	 * visibility of the field is modified using the Forge accesstransformer.cfg.
	 * 
	 * @param entity entity to assign goals to.
	 * @param goals  set of goals.
	 */
	public static void assignAiGoals(Mob entity, Set<WrappedGoal> goals) {
		try {
			GoalSelector selector = entity.goalSelector;
			selector.availableGoals.addAll(goals);
		} catch (Exception e) {
			logger.error("Failed to assign goals due to the error: " + e.getMessage());
			getBassebombeCraft().reportException(e);
			// NO-OP
		}
	}

	/**
	 * Assign target AI goals.
	 * 
	 * Goals are accessed using the private "goals" field in the goal selector. The
	 * visibility of the field is modified using the Forge accesstransformer.cfg.
	 * 
	 * @param entity entity to assign goals to.
	 * @param goals  set of goals.
	 */
	public static void assignAiTargetGoals(Mob entity, Set<WrappedGoal> goals) {
		try {
			GoalSelector selector = entity.targetSelector;
			selector.availableGoals.addAll(goals);
		} catch (Exception e) {
			logger.error("Failed to assign goals due to the error: " + e.getMessage());
			getBassebombeCraft().reportException(e);
			// NO-OP
		}
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity    entity which will configured with charmed AI.
	 * @param commander entity which charmed mob.
	 */
	public static void buildCharmedMobAi(Mob entity, LivingEntity commander) {

		// set goals
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(0, new FloatGoal(entity));

		// setup attacking if commander is player
		if (isTypePlayerEntity(commander)) {

			// type cast
			Player player = (Player) commander;
			selector.addGoal(2, new CompanionAttack(entity, player));
		} else
			selector.addGoal(2, new CompanionAttack(entity));

		// setup remaining tasks
		selector.addGoal(3, new FollowEntityGoal(entity, commander, AI_MOVE_SPEED, AI_MIN_DIST, AI_MAX_DIST));
		selector.addGoal(5, new RandomLookAroundGoal(entity));

		// exit setting target tasks if entity isn't a creature
		// including commander controlled targeting
		if (!isTypeCreatureEntity(entity))
			return;

		// set targeting goals
		PathfinderMob entityCreature = PathfinderMob.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Build AI for kitten army.
	 * 
	 * @param entity    entity which will configured with kitten army AI.
	 * @param commander entity which commands skeleton.
	 */
	public static void buildKittenArmyAi(Cat entity, LivingEntity commander) {

		// set goals, priority is: attack then follow leader
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(1, new FloatGoal(entity));
		selector.addGoal(2, new LeapAtTargetGoal(entity, 0.3F));
		selector.addGoal(3, new OcelotAttackGoal(entity));
		selector.addGoal(4, new FollowEntityGoal(entity, commander, AI_MOVE_SPEED, AI_MIN_DIST, AI_MAX_DIST));
		selector.addGoal(5, new SitWhenOrderedToGoal(entity));
		selector.addGoal(6, new RandomLookAroundGoal(entity));

		// set targeting goals
		PathfinderMob entityCreature = PathfinderMob.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Build AI for skeleton army.
	 * 
	 * @param entity    entity which will configured with skeleton army AI.
	 * @param commander entity which commands skeleton.
	 * 
	 */
	public static void buildSkeletonArmyAi(Skeleton entity, LivingEntity commander) {

		// set goals, priority is: attack then follow leader
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(1, new FloatGoal(entity));
		selector.addGoal(2, new FleeSunGoal(entity, 1.0D));
		selector.addGoal(3, new RangedBowAttackGoal<>(entity, 1.0D, 20, 15.0F));
		selector.addGoal(4, new FollowEntityGoal(entity, commander, AI_MOVE_SPEED, AI_MIN_DIST, AI_MAX_DIST));
		selector.addGoal(5, new LookAtPlayerGoal(entity, Player.class, 8.0F));
		selector.addGoal(6, new RandomLookAroundGoal(entity));

		// set targeting goals
		PathfinderMob entityCreature = PathfinderMob.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Build AI for charging entites, i.e. parrots, bees and war pigs.
	 * 
	 * @param entity entity which will configured with flying AI.
	 * @param target entity that entity will attack.
	 * @param entity damage.
	 */
	public static void buildChargingAi(Mob entity, LivingEntity target, float damage) {

		// set goals
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(1, new ChargeTowardsGoal(entity, target, AI_MIN_CHARGE_DISTANCE));
		selector.addGoal(2, new AttackInRangeGoal(entity, target, AI_MIN_CHARGE_DISTANCE, damage));

		// set targeting goals
		GoalSelector targetSelector = entity.targetSelector;
		targetSelector.addGoal(1, new SelfdestructWhenTargetDiesGoal(entity, target));
	}

	/**
	 * Build AI for Creeper army.
	 * 
	 * @param entity    entity which will configured with kitten army AI.
	 * @param commander entity which commands skeleton.
	 * 
	 */
	public static void buildCreeperArmyAi(Creeper entity, LivingEntity commander) {

		// set goals, priority is: attack then follow leader
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(1, new FloatGoal(entity));
		selector.addGoal(2, new SwellGoal(entity));
		selector.addGoal(3, new MeleeAttackGoal(entity, 1.0D, false));
		selector.addGoal(4, new FollowEntityGoal(entity, commander, AI_MOVE_SPEED, AI_MIN_DIST, AI_MAX_DIST));
		selector.addGoal(5, new LookAtPlayerGoal(entity, Player.class, 8.0F));
		selector.addGoal(6, new RandomLookAroundGoal(entity));

		// type cast
		PathfinderMob entityCreature = PathfinderMob.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Setup targeting.
	 * 
	 * Mob commanded targeting is only set if commander is a player entity.
	 * 
	 * @param entity    entity to set targeting for.
	 * @param commander commander (if defined).
	 */
	static void setupTargetingTasks(PathfinderMob entity, LivingEntity commander) {

		// setup targeting if commander is player entity
		if (isTypePlayerEntity(commander)) {

			// type cast
			Player player = (Player) commander;

			// set commander targeting
			GoalSelector selector = entity.targetSelector;
			selector.addGoal(0, new CommanderControlledTargeting(entity, player));
			return;
		}

		// set AI commander targeting if commander is a living entity
		GoalSelector selector = entity.targetSelector;
		selector.addGoal(0, new CommanderControlledTargeting(entity, commander));
		selector.addGoal(1, new CommandersTargetGoal(entity, commander));
		selector.addGoal(2, new HurtByTargetGoal(entity));
		return;
	}

	/**
	 * Get name of first running AI target goal.
	 * 
	 * @param entity to get target goal name from.
	 * 
	 * @return name of first target goal if entity is a {@linkplain MobEntity}.
	 *         Otherwise "N/A" is returned as name.
	 */
	public static String getFirstRunningAiTargetGoalName(LivingEntity entity) {

		// exit if entity isn't a mob entity
		if (!EntityUtils.isTypeMobEntity(entity))
			return "AI Target Goal: N/A";

		// type cast
		Mob mobEntity = (Mob) entity;

		// get first target goal
		Stream<WrappedGoal> targetGoals = mobEntity.targetSelector.getRunningGoals();
		Optional<WrappedGoal> optFirstGoal = targetGoals.findFirst();

		// return name if goal is defined
		if (optFirstGoal.isPresent()) {
			WrappedGoal goal = optFirstGoal.get();

			// get embedded goal
			Goal embbedGoal = goal.getGoal();

			return "AI Target Goal: " + embbedGoal.getClass().getSimpleName();
		}

		return "AI Target Goal: N/A";
	}

	/**
	 * Get name of running AI goal.
	 * 
	 * @param entity to get goal name from.
	 * 
	 * @return name of first goal if entity is a {@linkplain MobEntity}. Otherwise
	 *         "N/A" is returned as name.
	 */
	public static String getFirstRunningAiGoalName(LivingEntity entity) {

		// exit if entity isn't a mob entity
		if (!EntityUtils.isTypeMobEntity(entity))
			return "AI Goal: N/A";

		// type cast
		Mob mobEntity = (Mob) entity;

		// get first target goal
		Stream<WrappedGoal> targetGoals = mobEntity.goalSelector.getRunningGoals();
		Optional<WrappedGoal> optFirstGoal = targetGoals.findFirst();

		// return name if goal is defined
		if (optFirstGoal.isPresent()) {
			WrappedGoal goal = optFirstGoal.get();

			// get embedded goal
			Goal embbedGoal = goal.getGoal();

			return "AI Goal: " + embbedGoal.getClass().getSimpleName();
		}

		return "AI Goal: N/A";
	}

	/**
	 * Set mutex flags for targeting goal.
	 * 
	 * @param goal AI goal to set flags for.
	 */
	public static void setMutexFlagsforTargetingGoal(Goal goal) {
		goal.setFlags(EnumSet.of(TARGET));
	}

	/**
	 * Set mutex flags for attack goal.
	 * 
	 * @param goal AI goal to set flags for.
	 */
	public static void setMutexFlagsforAttackGoal(Goal goal) {
		goal.setFlags(EnumSet.of(MOVE, LOOK));
	}

	/**
	 * Set mutex flags for movement goal.
	 * 
	 * @param goal AI goal to set flags for.
	 */
	public static void setMutexFlagsforMovementGoal(Goal goal) {
		goal.setFlags(EnumSet.of(MOVE, LOOK));
	}

	/**
	 * Get charm duration of charm mob.
	 * 
	 * @param id         ID of charmed mob. ID is Entity.getEntityId() as a string.
	 * @param repository duration repository to perform query in.
	 * 
	 * @return charm duration of charm mob.
	 */
	public static int getCharmDuration(String id, DurationRepository repository) {
		try {
			// return zero if expired
			if (repository.isExpired(id))
				return IS_EXPIRED;

			return repository.get(id);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);

			// return zero as expired
			return IS_EXPIRED;
		}
	}

}
