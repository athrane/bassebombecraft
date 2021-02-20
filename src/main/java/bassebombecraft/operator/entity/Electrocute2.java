package bassebombecraft.operator.entity;

import static bassebombecraft.config.ModConfiguration.electrocuteAoeRange;
import static bassebombecraft.config.ModConfiguration.electrocuteDamage;
import static bassebombecraft.config.ModConfiguration.electrocuteDuration;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
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
	 * Operator instance (can be null due to class loading).
	 */
	static Optional<Operator2> optAoeOp = Optional.empty();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splAoeOp = () -> {

		// return operator instance if defined
		if (optAoeOp.isPresent()) {
			return optAoeOp.get();
		}

		// AddDamage2: get damage from configuration
		Function<Ports, Double> fnGetDamage = p -> electrocuteDamage.get();

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> electrocuteDuration.get().doubleValue();

		// create operator for AOE effect
		Operator2 op = new Sequence2(new AddDamage2(getFnGetEntity1(), getFnGetEntity2(), fnGetDamage),
				new AddGraphicalEffectAtClient2(getFnGetEntity1(), getFnGetEntity2(), fnGetDuration, NAME));

		optAoeOp = Optional.of(op);
		return optAoeOp.get();
	};

	/**
	 * Operator instance (can be null due to class loading).
	 */
	static Optional<Operator2> optOp = Optional.empty();

	/**
	 * Create operators. (using the get source function as input).
	 */
	static Function<Function<Ports, Entity>, Operator2> fnOp = fnGetSource -> {

		// return operator instance if defined
		if (optOp.isPresent()) {
			return optOp.get();
		}

		// FindEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(fnGetSource, p).getPosition();

		// FindEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(fnGetSource, p).getEntityWorld();

		// ApplyOperatorTo2: set target as entity #2
		BiConsumer<Ports, Entity> bcSetTarget = getBcSetEntity2();

		// create operator
		Operator2 op = new Sequence2(
				new FindEntities2(fnGetSourcePos, fnGetWorld, getBcSetEntities1(), electrocuteAoeRange.get()),
				new ApplyOperatorToEntity2(getFnGetEntities1(), bcSetTarget, splAoeOp.get()));
		optOp = Optional.of(op);
		return optOp.get();
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
	 * Operator ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports ports2;

	/**
	 * Constructor.
	 * 
	 * @param fnGetSource     function to get source entity.
	 * @param fnGetTrueSource function to get thrower entity.
	 */
	public Electrocute2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTrueSource) {
		this.fnGetSource = fnGetSource;
		this.fnGetTrueSource = fnGetTrueSource;
		ports2 = getInstance();
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

		// TODO: add support for predicate in ApplyOperatorTo2 to support filtering for
		// determine if target is invoker boolean isInvoker =
		// hasIdenticalUniqueID(originalSource, target); if (!isInvoker)
		// Entity originalSource = applyV(fnGetTrueSource, ports);

		ports2.setEntity1(source);
		Operators2.run(ports2, fnOp.apply(fnGetSource));
	}

}
