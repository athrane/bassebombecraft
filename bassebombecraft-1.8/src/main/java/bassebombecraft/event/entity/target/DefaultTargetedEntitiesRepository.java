package bassebombecraft.event.entity.target;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class DefaultTargetedEntitiesRepository implements TargetedEntitiesRepository {
	

	/**
	 * Targeted entities.
	 */
	Set<EntityLivingBase> targets = new HashSet<EntityLivingBase>();
	
	@Override
	public void add(EntityLivingBase entity) {
		targets.add(entity);
	}

	@Override
	public void remove(EntityLivingBase entity) {
		targets.remove(entity);
	}

	@Override
	public Stream<EntityLivingBase> get(EntityPlayer commander) {
		return targets.stream();
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
