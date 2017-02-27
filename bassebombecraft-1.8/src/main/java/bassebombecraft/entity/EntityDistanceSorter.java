package bassebombecraft.entity;

import java.util.Comparator;

import net.minecraft.entity.Entity;

/**
 * Entity distance sorter.
 */
public class EntityDistanceSorter implements Comparator<Entity> {
	Entity entity;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public int compare(Entity e1, Entity e2) {
		double d0 = this.entity.getDistanceSqToEntity(e1);
		double d1 = this.entity.getDistanceSqToEntity(e2);
		return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
	}
}