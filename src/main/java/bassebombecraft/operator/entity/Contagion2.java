package bassebombecraft.operator.entity;

import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.CONTAGION;
import static bassebombecraft.config.ModConfiguration.contagionEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.contagionEffectAoeRange;
import static bassebombecraft.config.ModConfiguration.contagionEffectDuration;
import static bassebombecraft.config.ModConfiguration.contagionEffectUpdateFrequency;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getBcSetLivingEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.potion.effect.RegisteredEffects.CONTAGION_EFFECT;
import static bassebombecraft.util.function.Functions.getFnCastToEntity;
import static bassebombecraft.util.function.Predicates.hasLivingEntitiesDifferentIds;
import static bassebombecraft.util.function.Predicates.hasNotPotionEffect;

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
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import bassebombecraft.potion.effect.ContagionEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain Operator2} interface which applies AOE
 * effect {@linkplain ContagionEffect} to nearby mobs.
 * 
 * The {@linkplain Ports} object used to invoke the operator isn't used to
 * invoke the embedded operators. An separate {@linkplain Ports} instance is
 * used to invoked the embedded operators.
 * 
 * The effect {@linkplain ContagionEffect} is assigned to mobs and an graphical
 * effect is sent to clients.
 */
public class Contagion2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Contagion2.class.getSimpleName();

	/**
	 * Create AOE effect operators.
	 */
	static Supplier<Operator2> splAoeEffectOp = () -> {

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> contagionEffectDuration.get().doubleValue();

		// AddGraphicalEffectAtClient2: type cast living entity #1 to entity
		Function<Ports, Entity> fnCastToEntity = getFnGetLivingEntity1().andThen(getFnCastToEntity());

		// create operator for AOE effect
		return new Sequence2(
				new AddEffect2(getFnGetLivingEntity1(), getBcSetEffectInstance1(), CONTAGION_EFFECT.get(),
						contagionEffectDuration.get(), contagionEffectAmplifier.get()),
				new AddGraphicalEffectAtClient2(getFnGetEntity1(), fnCastToEntity, fnGetDuration, CONTAGION));
	};

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
				Function<Ports, Integer> fnGetFrequency = p -> contagionEffectUpdateFrequency.get().intValue();

				// FindLivingEntities2: get source position from source entity
				Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(fnGetSource, p).blockPosition();

				// FindLivingEntities2: get world from source entity
				Function<Ports, Level> fnGetWorld = p -> applyV(fnGetSource, p).getCommandSenderWorld();

				// FindLivingEntities2: exclude self and entity with contagion
				Function<Ports, Predicate<LivingEntity>> fnGetPredicate = p -> hasLivingEntitiesDifferentIds(
						applyV(fnGetTrueSource, p)).and(hasNotPotionEffect(CONTAGION_EFFECT.get()));

				// FindLivingEntities2: get search range from configuration
				Function<Ports, Integer> fnGetRange = p -> contagionEffectAoeRange.get().intValue();

				// create operator
				return new Sequence2(new ResetResult2(), new IsFrequencyActive2(fnGetFrequency),
						new FindLivingEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange,
								getBcSetLivingEntities1()),
						new ApplyOperatorToLivingEntity2(lazyInitAoeEffectOp));
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
	public Contagion2(Function<Ports, Entity> fnGetSource, Function<Ports, Entity> fnGetTrueSource) {
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
	public Contagion2() {
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
