package bassebombecraft.operator.entity;

import static bassebombecraft.operator.DefaultPorts.getFnGetDouble1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

/**
 * Implementation of the {@linkplain Operator2} interface which applies damage
 * to entity.
 */
public class AddDamage2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = AddDamage2.class.getSimpleName();

	/**
	 * Null indirect damage source.
	 */
	static final Entity NULL_INDIRECT_SOURCE = null;

	/**
	 * Damage type.
	 */
	static final String DAMAGE_TYPE = "composite";
	
	/**
	 * Function to get source entity.
	 */
	Function<Ports, Entity> fnGetSource;

	/**
	 * Function to get target entity.
	 */
	Function<Ports, Entity> fnGetTarget;

	/**
	 * Function to get damage.
	 */
	Function<Ports, Double> fnGetDamage;
	
	/**
	 * Constructor.
	 * 
	 * @param fnGetSource function to get source entity in effect.
	 * @param fnGetTarget function to get target entity in effect.
	 * @param fnGetDamage function to get damage inflicted to target.
	 */
	public AddDamage2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTarget, Function<Ports, Double> fnGetDamage) {
		this.fnGetSource = fnGetSource;
		this.fnGetTarget = fnGetTarget;
		this.fnGetDamage = fnGetDamage;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as target from ports.
	 * 
	 * Instance is configured with double #1 as damage from ports.
	 */
	public AddDamage2(float damage) {
		this(getFnGetEntity1(), getFnGetEntity2(), getFnGetDouble1());
	}

	@Override
	public Ports run(Ports ports) {

		// get source
		Entity source = fnGetSource.apply(ports);
		if (source == null)
			return ports;

		// get target
		Entity target = fnGetTarget.apply(ports);
		if (target == null)
			return ports;

		// apply damage to target
		float damage = fnGetDamage.apply(ports).floatValue();
		DamageSource damageSource = new IndirectEntityDamageSource(DAMAGE_TYPE, source, NULL_INDIRECT_SOURCE);
		target.attackEntityFrom(damageSource, damage);

		return ports;
	}

}
