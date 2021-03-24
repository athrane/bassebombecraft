package bassebombecraft.operator.entity;

import static bassebombecraft.ModConstants.MOB_DAMAGE;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

/**
 * Implementation of the {@linkplain Operator2} interface which reflects "mob"
 * damage received.
 * 
 * Damage calculation is affected by amplifier.
 */
public class ReflectMobDamageAmplified2 implements Operator2 {

	/**
	 * Function to get damage source.
	 */
	Function<Ports, DamageSource> fnGetSource;

	/**
	 * Function to get damage amplifier.
	 */
	Function<Ports, Integer> fnGetAmplifier;

	/**
	 * Function to get damage amount.
	 */
	Function<Ports, Float> fnGetAmount;

	/**
	 * constructor.
	 * 
	 * @param fnGetSource    function to get damage source.
	 * @param fnGetAmplifier function to get damage amplifier value.
	 * @param fnGetAmount    function to get damage amount
	 */
	public ReflectMobDamageAmplified2(Function<Ports, DamageSource> fnGetSource,
			Function<Ports, Integer> fnGetAmplifier, Function<Ports, Float> fnGetAmount) {
		this.fnGetSource = fnGetSource;
		this.fnGetAmplifier = fnGetAmplifier;
		this.fnGetAmount = fnGetAmount;
	}

	@Override
	public void run(Ports ports) {
		DamageSource source = applyV(fnGetSource, ports);
		int amplifier = applyV(fnGetAmplifier, ports);
		float damageAmount = applyV(fnGetAmount, ports);

		// exit if damage isn't mob damage
		if (!source.getDamageType().equalsIgnoreCase(MOB_DAMAGE))
			return;

		// apply damage to source
		Entity srcEntity = source.getTrueSource();
		float finalAmount = damageAmount * amplifier;
		DamageSource newSource = DamageSource.causeMobDamage((LivingEntity) srcEntity);
		srcEntity.attackEntityFrom(newSource, finalAmount);
	}

}
