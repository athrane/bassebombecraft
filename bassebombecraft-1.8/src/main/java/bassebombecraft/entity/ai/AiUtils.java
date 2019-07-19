package bassebombecraft.entity.ai;

import static bassebombecraft.entity.EntityUtils.isCreatureEntity;
import static bassebombecraft.player.PlayerUtils.isPlayerEntity;

import java.util.Set;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.entity.ai.task.AiCommandersTargeting;
import bassebombecraft.entity.ai.task.CommanderControlledTargeting;
import bassebombecraft.entity.ai.task.CompanionAttack;
import bassebombecraft.entity.ai.task.FollowEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.PlayerEntity;

/**
 * AI utility class.
 */
public class AiUtils {

	static final boolean DONT_CALL_FOR_HELP = false;
	static final boolean NEARBY_ONLY = true;
	static final boolean SHOULD_CHECK_SIGHT = true;
	static final double MOVEMENT_SPEED = 1.5D; // movement speed towards player
	static final float MINIMUM_DIST = 6.0F; // Entity minimum distance to player
	static final float MAXIMUM_DIST = 50.0F; // Entity maximum distance to
												// player
	static final float WATCH_DIST = 10.0F;

	/**
	 * Clear passive and fighting AI tasks.
	 * 
	 * @param entity to clear AI tasks for.
	 */
	public static void clearAiTasks(LivingEntity entity) {
		removeTasks(entity.tasks);
		removeTasks(entity.targetTasks);
	}

	/**
	 * Remove tasks in a concurrently safe way
	 * 
	 * @param tasks AI tasks.
	 */
	static void removeTasks(EntityAITasks tasks) {
		Set<EntityAITaskEntry> entries = tasks.taskEntries;
		EntityAITaskEntry[] entriesArray = entries.toArray(new EntityAITaskEntry[entries.size()]);

		for (EntityAITaskEntry entry : entriesArray) {
			// get task
			EntityAIBase task = entry.action;

			// set to true to force removal
			entry.using = true;

			// remove
			tasks.removeTask(task);
		}
	}

	/**
	 * Assign passive AI tasks.
	 * 
	 * @param entity  to clear AI tasks for.
	 * @param entries tasks entries.
	 */
	public static void assignAiTasks(LivingEntity entity, Set<EntityAITaskEntry> entries) {
		for (EntityAITaskEntry entry : entries) {
			int priority = entry.priority;
			EntityAIBase task = entry.action;
			entity.tasks.addTask(priority, task);
		}
	}

	/**
	 * Assign target AI tasks.
	 * 
	 * @param entity  to clear AI tasks for.
	 * @param entries tasks entries.
	 */
	public static void assignAiTargetTasks(LivingEntity entity, Set<EntityAITaskEntry> entries) {
		for (EntityAITaskEntry entry : entries) {
			int priority = entry.priority;
			EntityAIBase task = entry.action;
			entity.targetTasks.addTask(priority, task);
		}
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity    entity which will configured with charmed AI.
	 * @param commander entity which charmed mob.
	 */
	public static void buildCharmedMobAi(LivingEntity entity, LivingEntity commander) {

		// set tasks
		entity.tasks.addTask(1, new EntityAISwimming(entity));

		// setup attacking if commander is player
		if (isPlayerEntity(commander)) {
			// type cast
			PlayerEntity player = (PlayerEntity) commander;
			entity.tasks.addTask(2, new CompanionAttack(entity, player));
		} else {
			entity.tasks.addTask(2, new CompanionAttack(entity));
		}

		// setup remainig tasks
		entity.tasks.addTask(3, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(5, new EntityAILookIdle(entity));

		// exit setting target tasks if entity isn't a creature
		// including commander controlled targeting
		if (!isCreatureEntity(entity))
			return;

		// type cast
		CreatureEntity entityCreature = CreatureEntity.class.cast(entity);
		setupTargetingTasks(entityCreature, commander);
	}

	/**
	 * Build AI for kitten army.
	 * 
	 * @param entity    entity which will configured with kitten army AI.
	 * @param commander entity which commands skeleton.
	 */
	public static void buildKittenArmyAi(EntityOcelot entity, LivingEntity commander) {

		entity.tasks.addTask(1, new EntityAISwimming(entity));
		entity.tasks.addTask(2, entity.getAISit());
		entity.tasks.addTask(3, new EntityAIFollowOwner(entity, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(4, new EntityAIOcelotSit(entity, 0.8D));
		entity.tasks.addTask(5, new EntityAILeapAtTarget(entity, 0.3F));
		entity.tasks.addTask(6, new EntityAIOcelotAttack(entity));
		entity.tasks.addTask(7, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(9, new EntityAILookIdle(entity));

		// type cast
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

		entity.tasks.addTask(1, new EntityAISwimming(entity));
		entity.tasks.addTask(2, new EntityAIFleeSun(entity, 1.0F));
		entity.tasks.addTask(3, new EntityAIPanic(entity, 1.4D));
		entity.tasks.addTask(4, new EntityAIAttackRangedBow(entity, 1.0D, 20, 15.0F));
		entity.tasks.addTask(5, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(7, new EntityAILookIdle(entity));

		// type cast
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

		entity.tasks.addTask(1, new EntityAISwimming(entity));
		entity.tasks.addTask(2, new EntityAICreeperSwell(entity));
		entity.tasks.addTask(4, new EntityAIAttackMelee(entity, 1.0D, false));
		entity.tasks.addTask(5, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(7, new EntityAILookIdle(entity));

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

		// setup targeting if commander is player
		if (isPlayerEntity(commander)) {

			// type cast
			PlayerEntity player = (PlayerEntity) commander;

			// set commander targeting
			entity.targetTasks.addTask(1, new CommanderControlledTargeting(entity, player));
			return;
		}

		// set AI commander targeting if commander is a living entity
		if (EntityUtils.isLivingEntity(commander)) {

			// type cast
			LivingEntity commander2 = (LivingEntity) commander;

			entity.targetTasks.addTask(1, new AiCommandersTargeting(entity, commander2));
			entity.targetTasks.addTask(2, new EntityAIHurtByTarget(entity, DONT_CALL_FOR_HELP, new Class[0]));
			return;
		}

		// set AI commander targeting if commander is a living entity base
		entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entity, DONT_CALL_FOR_HELP, new Class[0]));
	}

}
