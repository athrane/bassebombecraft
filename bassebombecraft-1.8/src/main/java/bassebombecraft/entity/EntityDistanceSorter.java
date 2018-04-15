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
		double d0 = entity.getDistanceSq(e1);
		double d1 = entity.getDistanceSq(e2);
		return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
	}
}