package bassebombecraft.client.event.rendering.effect;

import java.util.stream.Stream;

import bassebombecraft.proxy.Proxy;
import net.minecraft.entity.Entity;

/**
 * Interface for repository for rendering graphics effects.
 * 
 * The repository is used at CLIENT side. Access to the repository is supported
 * via sided proxy, i.e.{@linkplain Proxy}.
 * 
 * The CLIENT side implementation {@linkplain ClientGraphicalEffectRepository}
 * implements logic to support rendering.
 */
public interface GraphicalEffectRepository {

	/**
	 * Support effects
	 */
	enum Effect {
		NO_EFFECT,
		LINE,
		ELECTROCUTE,
		PROJECTILE_TRAIL,
		WILDFIRE
	};
	
	/**
	 * Add effect.
	 * 
	 * @param source   source entity involved in the effect.
	 * @param target   target entity involved in the effect.
	 * @param duration effect duration.
	 * @param effect effect name.
	 */	
	public void add(Entity source, Entity target, int duration, Effect effect);

	/**
	 * Add unresolved effect where source Id isn't sync'ed to client yet.
	 * 
	 * @param sourceId   source entity involved in the effect.
	 * @param targetId   target entity involved in the effect.
	 * @param duration effect duration.
	 * @param effect effect name.
	 */	
	public void addUnresolvedSource(int sourceId, Entity target, int duration, Effect effect);
	
	/**
	 * Remove effect.
	 * 
	 * @param id ID of source entity which is removed. The ID is read from
	 *           {@linkplain Entity.getEntityId()}.
	 */
	public void remove(String id);

	/**
	 * Returns true if source entity is involved in effect..
	 * 
	 * @param id ID of entity to query for. The ID is read from
	 *           {@linkplain Entity.getEntityId()}.
	 * @return true if mob is already charmed.
	 */
	public boolean contains(String id);

	/**
	 * Get effects.
	 * 
	 * @return stream of effects.
	 */
	public Stream<GraphicalEffect> get();

}
