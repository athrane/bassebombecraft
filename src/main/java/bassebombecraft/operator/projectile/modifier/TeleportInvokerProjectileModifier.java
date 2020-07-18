package bassebombecraft.operator.projectile.modifier;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;

import java.util.Arrays;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;

/**
 * Implementation of the {@linkplain Operator2} interface which modifies a
 * projectile on impact by teleporting the invoker to the point of impact.
 */
public class TeleportInvokerProjectileModifier implements Operator2 {

	// TODO: Make generic tagging operator for projectile modifiers

	/**
	 * Operator identifier.
	 */
	public static final String NAME = TeleportInvokerProjectileModifier.class.getSimpleName();

	/**
	 * Function to get projectiles.
	 */
	Function<Ports, Entity[]> fnGetProjectiles;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectiles function to get projectiles.
	 */
	public TeleportInvokerProjectileModifier(Function<Ports, Entity[]> fnGetProjectiles) {
		this.fnGetProjectiles = fnGetProjectiles;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 */
	public TeleportInvokerProjectileModifier() {
		this(getFnGetEntities1());
	}

	@Override
	public Ports run(Ports ports) {

		// tag projectiles
		Entity[] projectiles = fnGetProjectiles.apply(ports);
		Arrays.stream(projectiles).forEach(p -> p.addTag(NAME));

		return ports;
	}

}
