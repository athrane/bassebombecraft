package bassebombecraft.event.entity.target;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class DefaultTargetedEntitiesRepository implements TargetedEntitiesRepository {

	/**
	 * Targeted entities.
	 */
	Set<LivingEntity> targets = new HashSet<LivingEntity>();

	@Override
	public void add(LivingEntity entity) {
		targets.add(entity);
	}

	@Override
	public void remove(LivingEntity entity) {
		targets.remove(entity);
	}

	@Override
	public Stream<LivingEntity> get(PlayerEntity commander) {
		return targets.stream();
	}

	
	@Override
	public int size(PlayerEntity commander) {
		return targets.size();
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static TargetedEntitiesRepository getInstance() {
		return new DefaultTargetedEntitiesRepository();
	}

}
