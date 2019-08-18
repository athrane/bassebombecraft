package bassebombecraft.entity.ai;

import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.stream.Stream;

import bassebombecraft.entity.ai.goal.CommanderControlledTargeting;
import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.entity.ai.goal.FollowEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.ai.goal.FleeSunGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * AI utility class.
 */
public class AiUtils {

	/**
	 * Initial goal priority.
	 */
	static final int INITIAL_GOAL_PRIORITY = 0;

	static final double MOVEMENT_SPEED = 1.5D; // movement speed towards player
	static final float MINIMUM_DIST = 6.0F; // Entity minimum distance to player
	static final float MAXIMUM_DIST = 50.0F; // Entity maximum distance to player

	/**
	 * Clear passive and fighting AI goals.
	 * 
	 * @param entity whose goals are cleared.
	 */
	public static void clearAllAiGoals(CreatureEntity entity) {
		removeGoals(entity.goalSelector);
		removeGoals(entity.targetSelector);
	}

	/**
	 * Remove goals in a concurrently safe way
	 * 
	 * @param selector AI goal selector.
	 */
	static void removeGoals(GoalSelector selector) {
		Stream<PrioritizedGoal> goals = selector.getRunningGoals();
		goals.forEach(g -> selector.removeGoal(g));
	}

	/**
	 * Assign passive AI goals.
	 * 
	 * The first goal in the stream is added with highest priority, i.e. 1. The
	 * priority is increased for each subsequent goal.
	 * 
	 * @param entity entity to assign goals to.
	 * @param goals  stream of goals.
	 */
	public static void assignAiGoals(CreatureEntity entity, Stream<Goal> goals) {
		// use integer array to hold running counter for task priority
		int[] priority = { INITIAL_GOAL_PRIORITY };

		// get goals and add them to the entity
		GoalSelector selector = entity.goalSelector;
		goals.forEachOrdered(g -> selector.addGoal(priority[0]++, g));
	}

	/**
	 * Assign target AI goals.
	 * 
	 * The first goal in the stream is added with highest priority, i.e. 1. The
	 * priority is increased for each subsequent goal.
	 * 
	 * @param entity entity to assign goals to.
	 * @param goals  stream of goals.
	 */
	public static void assignAiTargetTasks(CreatureEntity entity, Stream<Goal> goals) {
		// use integer array to hold running counter for task priority
		int[] priority = { INITIAL_GOAL_PRIORITY };

		// get goals and add them to the entity
		GoalSelector selector = entity.targetSelector;
		goals.forEachOrdered(g -> selector.addGoal(priority[0]++, g));
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity    entity which will configured with charmed AI.
	 * @param commander entity which charmed mob.
	 */
	public static void buildCharmedMobAi(CreatureEntity entity, LivingEntity commander) {

		// set goals
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(0, new SwimGoal(entity));

		// setup attacking if commander is player
		if (isTypePlayerEntity(commander)) {

			// type cast
			PlayerEntity player = (PlayerEntity) commander;
			selector.addGoal(2, new CompanionAttack(entity, player));
		} else
			selector.addGoal(2, new CompanionAttack(entity));

		// setup remaining tasks
		selector.addGoal(3, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		selector.addGoal(5, new LookRandomlyGoal(entity));

		// exit setting target tasks if entity isn't a creature
		// including commander controlled targeting
		if (!isTypeCreatureEntity(entity))
			return;

		// set targeting goals
		CreatureEntity entityCreature = CreatureEntity.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * s Build AI for kitten army.
	 * 
	 * @param entity    entity which will configured with kitten army AI.
	 * @param commander entity which commands skeleton.
	 */
	public static void buildKittenArmyAi(OcelotEntity entity, LivingEntity commander) {

		// set goals
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(1, new SwimGoal(entity));
		selector.addGoal(2, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		selector.addGoal(3, new LeapAtTargetGoal(entity, 0.3F));
		selector.addGoal(4, new OcelotAttackGoal(entity));
		selector.addGoal(5, new LookRandomlyGoal(entity));

		// set targeting goals
		CreatureEntity entityCreature = CreatureEntity.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Build AI for skeleton army.
	 * 
	 * @param entity    entity which will configured with kitten army AI.
	 * @param commander entity which commands skeleton.
	 * 
	 */
	public static void buildSkeletonArmyAi(SkeletonEntity entity, LivingEntity commander) {

		// set goals
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(1, new SwimGoal(entity));
		selector.addGoal(2, new FleeSunGoal(entity, 1.0D));
		selector.addGoal(2, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		selector.addGoal(3, new LookAtGoal(entity, PlayerEntity.class, 8.0F));
		selector.addGoal(3, new LookRandomlyGoal(entity));

		// set targeting goals
		CreatureEntity entityCreature = CreatureEntity.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Build AI for Creeper army.
	 * 
	 * @param entity    entity which will configured with kitten army AI.
	 * @param commander entity which commands skeleton.
	 * 
	 */
	public static void buildCreeperArmyAi(CreeperEntity entity, LivingEntity commander) {

		// set goals
		GoalSelector selector = entity.goalSelector;
		selector.addGoal(1, new SwimGoal(entity));
		selector.addGoal(2, new CreeperSwellGoal(entity));
		selector.addGoal(3, new MeleeAttackGoal(entity, 1.0D, false));
		selector.addGoal(2, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		selector.addGoal(3, new LookAtGoal(entity, PlayerEntity.class, 8.0F));
		selector.addGoal(3, new LookRandomlyGoal(entity));

		// type cast
		CreatureEntity entityCreature = CreatureEntity.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Setup targeting. Mob commanded targeting is only set if commander is a player
	 * entity.
	 * 
	 * @param entity    entity to set targeting for.
	 * @param commander commander (if defined).
	 */
	static void setupTargetingTasks(CreatureEntity entity, LivingEntity commander) {

		// setup targeting if commander is player entity
		if (isTypePlayerEntity(commander)) {

			// type cast
			PlayerEntity player = (PlayerEntity) commander;

			// set commander targeting
			GoalSelector selector = entity.targetSelector;
			selector.addGoal(0, new CommanderControlledTargeting(entity, player));
			return;
		}

		// set AI commander targeting if commander is a living entity
		GoalSelector selector = entity.targetSelector;
		selector.addGoal(0, new CommanderControlledTargeting(entity, commander));
		selector.addGoal(1, new HurtByTargetGoal(entity));
		return;
	}

}
