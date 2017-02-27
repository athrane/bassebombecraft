package bassebombecraft.event.charm;

import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;

/**
 * Default implementation of the {@linkplain CharmedMobsRepository}.
 */
public class DefaultCharmedMobsRepository implements CharmedMobsRepository {

	static final int EFFECT_DURATION = 1000; // Measured in ticks

	/**
	 * Charmed mobs.
	 */
	Map<EntityLiving, CharmedMob> charmedMobs = new ConcurrentHashMap<EntityLiving, CharmedMob>();

	@Override
	public void add(EntityLiving entity) {

		// create charmed mob container
		CharmedMob charmedMob = new CharmedMob(entity, EFFECT_DURATION);

		clearAiTasks(entity);
		buildCharmedMobAi(entity);

		// store mob
		charmedMobs.put(entity, charmedMob);
	}
	
	@Override
	public void add(EntityLiving entity, EntityLivingBase commander) {

		// create charmed mob container
		CharmedMob charmedMob = new CharmedMob(entity, EFFECT_DURATION);

		clearAiTasks(entity);
		buildCharmedMobAi(entity, commander);

		// store mob
		charmedMobs.put(entity, charmedMob);		
	}


	@Override
	public void remove(EntityLiving entity) {
		if (!contains(entity))
			return;
		
		// restore AI tasks
		CharmedMob charmedMob = charmedMobs.get(entity);
		clearAiTasks(entity);	
		assignAiTasks(entity, charmedMob.getTasks());
		assignAiTargetTasks(entity, charmedMob.getTargetTasks());
		
		// remove mob from repository 
		charmedMobs.remove(entity);
	}

	void assignAiTasks(EntityLiving entity, Set<EntityAITaskEntry> tasks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(EntityLiving entity) {
		if (!contains(entity))
			return;

		// update
		CharmedMob charmedMob = charmedMobs.get(entity);
		charmedMob.update();

		// remove if charm is expired
		if (charmedMob.isCharmExpired())
			remove(entity);
	}

	@Override
	public boolean contains(EntityLiving entity) {
		return charmedMobs.containsKey(entity);
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static CharmedMobsRepository getInstance() {
		return new DefaultCharmedMobsRepository();
	}

}
