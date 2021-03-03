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
		ELECTROCUTE,
		PROJECTILE_TRAIL		
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
