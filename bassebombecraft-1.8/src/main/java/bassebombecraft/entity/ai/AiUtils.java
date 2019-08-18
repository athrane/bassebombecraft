package bassebombecraft.entity.ai;

import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Stream;

import static org.apache.commons.lang3.reflect.FieldUtils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.entity.ai.goal.CommanderControlledTargeting;
import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.entity.ai.goal.FollowEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.ai.goal.FleeSunGoal;
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
	 * Force access to private fields when using reflection.
	 */
	static final boolean FORCE_ACCESS = true;

	/**
	 * Initial goal priority.
	 */
	static final int INITIAL_GOAL_PRIORITY = 0;

	static final double MOVEMENT_SPEED = 1.5D; // movement speed towards player
	static final float MINIMUM_DIST = 6.0F; // Entity minimum distance to player
	static final float MAXIMUM_DIST = 50.0F; // Entity maximum distance to player

	/**
	 * Logger.
	 */
	static Logger logger = LogManager.getLogger(BassebombeCraft.class);

	/**
	 * Capture goals from {@linkplain GoalSelector}.
	 * 
	 * Goals are accessed using reflection to access private goals field in the goal
	 * selector.
	 * 
	 * @param selector goal selector to get goals from.
	 * 
	 * @return set of goals from goals selector.
	 */
	public static Set<PrioritizedGoal> captureGoals(GoalSelector selector) {
		try {
			Field field = selector.getClass().getDeclaredField("goals");
			return (Set<PrioritizedGoal>) readField(field, selector, FORCE_ACCESS);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			logger.error("Failed to capture goals due to the error: " + e.getMessage());
			// TODO: add to centralized exception handling.

			// return null set
			return Sets.newLinkedHashSet();
		}
	}

	/**
	 * Clear passive and fighting AI goals.
	 * 
	 * @param entity whose goals are cleared.
	 */
	public static void clearAllAiGoals(MobEntity entity) {
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
	 * @param entity entity to assign goals to.
	 * @param goals  set of goals.
	 */
	public static void assignAiGoals(MobEntity entity, Set<PrioritizedGoal> goals) {
		try {
			GoalSelector selector = entity.goalSelector;
			Field field = selector.getClass().getDeclaredField("goals");
			writeField(field, selector, goals, FORCE_ACCESS);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			logger.error("Failed to assign goals due to the error: " + e.getMessage());
			// TODO: add to centralized exception handling.
			// NO-OP
		}
	}

	/**
	 * Assign target AI goals.
	 * 
	 * @param entity entity to assign goals to.
	 * @param goals  set of goals.
	 */
	public static void assignAiTargetGoals(MobEntity entity, Set<PrioritizedGoal> goals) {
		try {
			GoalSelector selector = entity.targetSelector;
			Field field = selector.getClass().getDeclaredField("goals");
			writeField(field, selector, goals, FORCE_ACCESS);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			logger.error("Failed to assign goals due to the error: " + e.getMessage());
			// TODO: add to centralized exception handling.
			// NO-OP
		}
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity    entity which will configured with charmed AI.
	 * @param commander entity which charmed mob.
	 */
	public static void buildCharmedMobAi(MobEntity entity, LivingEntity commander) {

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
	 * Setup targeting.
	 * 
	 * Mob commanded targeting is only set if commander is a player entity.
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
