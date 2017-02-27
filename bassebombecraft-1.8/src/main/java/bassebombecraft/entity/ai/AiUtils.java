package bassebombecraft.entity.ai;

import static bassebombecraft.player.PlayerUtils.isEntityPlayer;

import java.util.Set;

import bassebombecraft.entity.ai.task.CompanionAttack;
import bassebombecraft.entity.ai.task.FollowEntity;
import bassebombecraft.entity.ai.task.MobCommandedTargeting;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;

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

	static EntityAiBuilder charmedMobAiBuilder = new CharmedMobAiBuilder();

	/**
	 * Clear passive and fighting AI tasks.
	 * 
	 * @param entity
	 *            to clear AI tasks for.
	 */
	public static void clearAiTasks(EntityLiving entity) {
		removeTasks(entity.tasks);
		removeTasks(entity.targetTasks);
	}

	/**
	 * Remove tasks in a concurrently safe way
	 * 
	 * @param tasks
	 *            AI tasks.
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
	 * @param entity
	 *            to clear AI tasks for.
	 * @param entries
	 *            tasks entries.
	 */
	public static void assignAiTasks(EntityLiving entity, Set<EntityAITaskEntry> entries) {
		for (EntityAITaskEntry entry : entries) {
			int priority = entry.priority;
			EntityAIBase task = entry.action;
			entity.tasks.addTask(priority, task);
		}
	}

	/**
	 * Assign target AI tasks.
	 * 
	 * @param entity
	 *            to clear AI tasks for.
	 * @param entries
	 *            tasks entries.
	 */
	public static void assignAiTargetTasks(EntityLiving entity, Set<EntityAITaskEntry> entries) {
		for (EntityAITaskEntry entry : entries) {
			int priority = entry.priority;
			EntityAIBase task = entry.action;
			entity.targetTasks.addTask(priority, task);
		}
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity
	 *            entity which will configured with charmed AI.
	 */
	public static void buildCharmedMobAi(EntityLiving entity) {
		charmedMobAiBuilder.build(entity);
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity
	 *            entity which will configured with charmed AI.
	 * @param owner
	 *            entity which charmed mob.
	 */
	public static void buildCharmedMobAi(EntityLiving entity, EntityLivingBase commander) {

		// set tasks
		entity.tasks.addTask(1, new EntityAISwimming(entity));
		entity.tasks.addTask(2, new CompanionAttack(entity));
		entity.tasks.addTask(3, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(4, new EntityAIWatchClosest(entity, EntityMob.class, WATCH_DIST));
		entity.tasks.addTask(5, new EntityAILookIdle(entity));

		// type cast
		EntityCreature entityCreature = EntityCreature.class.cast(entity);

		// setup targeting if commander is player
		if (isEntityPlayer(commander)) {

			// type cast
			EntityPlayer player = (EntityPlayer) commander;

			entity.targetTasks.addTask(1, new MobCommandedTargeting(entityCreature, player));
			return;
		}

		// setup targeting if commander is other entity
		entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entityCreature, DONT_CALL_FOR_HELP, new Class[0]));
		entity.targetTasks.addTask(2,
				new EntityAINearestAttackableTarget(entityCreature, EntityMob.class, SHOULD_CHECK_SIGHT, NEARBY_ONLY));
	}

	/**
	 * Build AI for kitten army.
	 * 
	 * @param entity
	 *            entity which will configured with kitten army AI.
	 * @param commander
	 *            entity which commands skeleton.
	 */
	public static void buildKittenArmyAi(EntityOcelot entity, EntityLivingBase commander) {

		entity.tasks.addTask(1, new EntityAISwimming(entity));
		entity.tasks.addTask(2, entity.getAISit());
		entity.tasks.addTask(3, new EntityAIFollowOwner(entity, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(4, new EntityAIOcelotSit(entity, 0.8D));
		entity.tasks.addTask(5, new EntityAILeapAtTarget(entity, 0.3F));
		entity.tasks.addTask(6, new EntityAIOcelotAttack(entity));
		entity.tasks.addTask(7, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(8, new EntityAIWatchClosest(entity, EntityMob.class, WATCH_DIST));
		entity.tasks.addTask(9, new EntityAILookIdle(entity));

		// type cast
		EntityCreature entityCreature = EntityCreature.class.cast(entity);

		// setup targeting if commander is player
		if (isEntityPlayer(commander)) {

			// type cast
			EntityPlayer player = (EntityPlayer) commander;

			entity.targetTasks.addTask(1, new MobCommandedTargeting(entityCreature, player));
			return;
		}

		// setup targeting if commander is other entity
		entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entityCreature, DONT_CALL_FOR_HELP, new Class[0]));
		entity.targetTasks.addTask(2,
				new EntityAINearestAttackableTarget(entityCreature, EntityMob.class, SHOULD_CHECK_SIGHT, NEARBY_ONLY));
	}

	/**
	 * Build AI for Skeleton army.
	 * 
	 * @param entity
	 *            entity which will configured with kitten army AI.
	 * @param commander
	 *            entity which commands skeleton.
	 * 
	 */
	public static void buildSkeletonArmyAi(EntitySkeleton entity, EntityLivingBase commander) {

		entity.tasks.addTask(1, new EntityAISwimming(entity));
		entity.tasks.addTask(2, new EntityAIFleeSun(entity, 1.0F));
		entity.tasks.addTask(3, new EntityAIPanic(entity, 1.4D));
		entity.tasks.addTask(4, new EntityAIAttackRangedBow(entity, 1.0D, 20, 15.0F));
		entity.tasks.addTask(5, new FollowEntity(entity, commander, MOVEMENT_SPEED, MINIMUM_DIST, MAXIMUM_DIST));
		entity.tasks.addTask(6, new EntityAIWatchClosest(entity, EntityMob.class, WATCH_DIST));
		entity.tasks.addTask(7, new EntityAILookIdle(entity));

		// type cast
		EntityCreature entityCreature = EntityCreature.class.cast(entity);

		// setup targeting if commander is player
		if (isEntityPlayer(commander)) {

			// type cast
			EntityPlayer player = (EntityPlayer) commander;

			entity.targetTasks.addTask(1, new MobCommandedTargeting(entityCreature, player));
			return;
		}

		// setup targeting if commander is other entity
		entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entityCreature, DONT_CALL_FOR_HELP, new Class[0]));
		entity.targetTasks.addTask(2,
				new EntityAINearestAttackableTarget(entityCreature, EntityMob.class, SHOULD_CHECK_SIGHT, NEARBY_ONLY));
	}

}
