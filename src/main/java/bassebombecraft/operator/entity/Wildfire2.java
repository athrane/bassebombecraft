package bassebombecraft.operator.entity;

import static bassebombecraft.config.ModConfiguration.wildfireEffectAoeRange;
import static bassebombecraft.config.ModConfiguration.wildfireEffectUpdateFrequency;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.potion.effect.WildfireEffect.splAoeEffectOp;
import static bassebombecraft.util.function.Predicates.hasDifferentIds;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.ResetResult2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsFrequencyActive2;
import bassebombecraft.potion.effect.WildfireEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which applies AOE
 * effect wildfire to nearby mobs.
 * 
 * The {@linkplain Ports} object used to invoke the operator isn't used to
 * invoke the embedded operators. An separate {@linkplain Ports} instance is
 * used to invoked the embedded operators.
 * 
 * The effect {@linkplain WildfireEffect} is assigned to mobs and an graphical
 * effect is sent to clients.
 */
public class Wildfire2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Wildfire2.class.getSimpleName();

	/**
	 * Operator to setup operator initializer function for lazy initialisation.
	 */
	static final Operator2 lazyInitAoeEffectOp = of(splAoeEffectOp);

	/**
	 * Create operators to locate AOE candidates (using the get source function as
	 * input).
	 */
	static BiFunction<Function<Ports, Entity>, Function<Ports, Entity>, Operator2> fnAoeCoreOp = (fnGetSource,
			fnGetTrueSource) -> {

		// IsFrequencyActive2: get frequency from configuration
		Function<Ports, Integer> fnGetFrequency = p -> wildfireEffectUpdateFrequency.get().intValue();

		// FindEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(fnGetSource, p).getPosition();

		// FindEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(fnGetSource, p).getEntityWorld();

		// FindEntities2: get function to create exclusion predicate using the true
		// source entity
		Function<Ports, Predicate<Entity>> fnGetPredicate = p -> hasDifferentIds(applyV(fnGetTrueSource, p));

		// FindEntities2: get search range from configuration
		Function<Ports, Integer> fnGetRange = p -> wildfireEffectAoeRange.get().intValue();

		// ApplyOperatorTo2: set target as entity #2
		BiConsumer<Ports, Entity> bcSetTarget = getBcSetEntity2();

		// create operator
		return new Sequence2(new ResetResult2(), new IsFrequencyActive2(fnGetFrequency),
				new FindEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange, getBcSetEntities1()),
				new ApplyOperatorToEntity2(getFnGetEntities1(), bcSetTarget, lazyInitAoeEffectOp));
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
	public Wildfire2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTrueSource) {
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
	public Wildfire2() {
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
