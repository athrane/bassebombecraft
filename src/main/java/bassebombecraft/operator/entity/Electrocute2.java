package bassebombecraft.operator.entity;

import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.ELECTROCUTE;
import static bassebombecraft.config.ModConfiguration.electrocuteAoeRange;
import static bassebombecraft.config.ModConfiguration.electrocuteDamage;
import static bassebombecraft.config.ModConfiguration.*;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.util.function.Predicates.hasDifferentIds;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.ResetResult2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
import bassebombecraft.operator.conditional.IsFrequencyActive2;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which applies AOE
 * effect electrocute to nearby mobs.
 * 
 * The {@linkplain Ports} object used to invoke the operator isn't used to
 * invoke the embedded operators. An separate {@linkplain Ports} instance is
 * used to invoked the embedded operators.
 * 
 * Damage is assigned to mobs and an graphical effect is sent to clients.
 */
public class Electrocute2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Electrocute2.class.getSimpleName();

	/**
	 * Create AOE effect operators.
	 */
	static Supplier<Operator2> splAoeEffectOp = () -> {

		// AddDamage2: get damage from configuration
		Function<Ports, Double> fnGetDamage = p -> electrocuteDamage.get();

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> electrocuteDuration.get().doubleValue();

		// create operator for AOE effect
		return new Sequence2(new AddDamage2(getFnGetEntity1(), getFnGetEntity2(), fnGetDamage),
				new AddGraphicalEffectAtClient2(getFnGetEntity1(), getFnGetEntity2(), fnGetDuration, ELECTROCUTE));
	};

	/**
	 * Create operators to locate AOE candidates (using the get source function as
	 * input).
	 */
	static BiFunction<Function<Ports, Entity>, Function<Ports, Entity>, Operator2> fnAoeCoreOp = (fnGetSource,
			fnGetTrueSource) -> {

		// IsFrequencyActive2: get frequency from configuration
		Function<Ports, Integer> fnGetFrequency = p -> electrocuteAoeFrequency.get().intValue();
		
		// FindEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(fnGetSource, p).getPosition();

		// FindEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(fnGetSource, p).getEntityWorld();

		// FindEntities2: get function to create exclusion predicate using the true
		// source entity
		Function<Ports, Predicate<Entity>> fnGetPredicate = p -> hasDifferentIds(applyV(fnGetTrueSource, p));

		// FindEntities2: get search range from configuration
		Function<Ports, Integer> fnGetRange = p -> electrocuteAoeRange.get().intValue();

		// ApplyOperatorTo2: set target as entity #2
		BiConsumer<Ports, Entity> bcSetTarget = getBcSetEntity2();

		// create operator
		return new Sequence2(new ResetResult2(), new IsFrequencyActive2(fnGetFrequency),
				new FindEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange, getBcSetEntities1()),
				new ApplyOperatorToEntity2(getFnGetEntities1(), bcSetTarget, splAoeEffectOp.get()));
	};

	/**
	 * Function to get source entity.
	 */
	Function<Ports, Entity> fnGetSource;

	/**
	 * Function to get true source entity (e.g projectile thrower).
	 */
	Function<Ports, Entity> fnGetTrueSource;

	/**
	 * AOE ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports aoePorts;

	/**
	 * AOE operator instance.
	 */
	Operator2 aoeOp;

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource     function to get source entity.
	 * @param fnGetTrueSource function to get thrower entity.
	 */
	public Electrocute2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTrueSource) {
		this.fnGetSource = fnGetSource;
		this.fnGetTrueSource = fnGetTrueSource;
		aoePorts = getInstance();
		aoeOp = fnAoeCoreOp.apply(fnGetSource, fnGetTrueSource);
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as source from ports.
	 * 
	 * Instance is configured with entity #2 as (e.g projectile thrower) from ports.
	 */
	public Electrocute2() {
		this(getFnGetEntity1(), getFnGetEntity2());
	}

	@Override
	public void run(Ports ports) {
		Entity source = applyV(fnGetSource, ports);
		Entity trueSource = applyV(fnGetTrueSource, ports);

		aoePorts.setEntity1(source);
		aoePorts.setEntity2(trueSource);
		Operators2.run(aoePorts, aoeOp);
	}

}
