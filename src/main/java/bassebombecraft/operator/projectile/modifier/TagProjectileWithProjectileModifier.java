package bassebombecraft.operator.projectile.modifier;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetString1;

import java.util.Arrays;
import java.util.function.Function;

import bassebombecraft.event.projectile.ProjectileModifierEventHandler;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

/**
 * Implementation of the {@linkplain Operator2} interface which adds a tag to a
 * projectile.
 * 
 * The tag is processed by the event handler
 * {@linkplain ProjectileModifierEventHandler} which received
 * {@linkplain ProjectileImpactEvent} and scans for projectile tages and
 * executes the configured operator for a projectile modifier.
 */
public class TagProjectileWithProjectileModifier implements Operator2 {

	/**
	 * Function to get projectiles.
	 */
	Function<Ports, Entity[]> fnGetProjectiles;

	/**
	 * Function to get tag.
	 */
	Function<Ports, String> fnGetTag;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectiles function to get projectiles.
	 * @param fnGetTag function to get tag.
	 */
	public TagProjectileWithProjectileModifier(Function<Ports, Entity[]> fnGetProjectiles,
			Function<Ports, String> fnGetTag) {
		this.fnGetProjectiles = fnGetProjectiles;
		this.fnGetTag = fnGetTag;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 * 
	 * Instance is configured with string #1 as the tag to add to projectiles.
	 */
	public TagProjectileWithProjectileModifier() {
		this(getFnGetEntities1(), getFnGetString1());
	}

	@Override
	public Ports run(Ports ports) {

		// get tag
		String tag = fnGetTag.apply(ports);
		
		// tag projectiles
		Entity[] projectiles = fnGetProjectiles.apply(ports);
		Arrays.stream(projectiles).forEach(p -> p.addTag(tag));

		return ports;
	}

}
