package bassebombecraft.event.charm;

import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;

/**
 * Charmed mob.
 */
public class CharmedMob {

	final Set<EntityAITaskEntry> tasks; // captured AI tasks.
	final Set<EntityAITaskEntry> targetTasks; // captured AI target tasks.
	final EntityLiving entity; // charmed mob.
	private int duration; // Measured in ticks.

	/**
	 * CharmedMob constructor.
	 * 
	 * @param entity
	 *            charmed mob.
	 * @param duration
	 *            duration of charm in measured in ticks.
	 * 
	 */
	CharmedMob(EntityLiving entity, int duration) {
		this.entity = entity;
		tasks = entity.tasks.taskEntries;
		targetTasks = entity.targetTasks.taskEntries;
		this.duration = duration;
	}

	public Set<EntityAITaskEntry> getTasks() {
		return tasks;
	}

	public Set<EntityAITaskEntry> getTargetTasks() {
		return targetTasks;
	}

	public EntityLiving getEntity() {
		return entity;
	}

	public void update() {
		if (duration == 0)
			return;
		duration = duration - 1;
	}

	public boolean isCharmExpired() {
		return (duration == 0);
	}

}
