package bassebombecraft.event.charm;

import java.util.List;

import net.minecraft.entity.EntityLiving;

/**
 * Charmed mob.
 */
public class CharmedMob {

	final List tasks; // captured AI tasks.
	final List targetTasks; // captured AI target tasks.
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

	public List getTasks() {
		return tasks;
	}

	public List getTargetTasks() {
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
