package bassebombecraft.potion.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.ELECTROCUTE;
import static bassebombecraft.config.ModConfiguration.wildfireEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.wildfireEffectAoeRange;
import static bassebombecraft.config.ModConfiguration.wildfireEffectDuration;
import static bassebombecraft.config.ModConfiguration.wildfireEffectUpdateFrequency;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.potion.effect.RegisteredEffects.WILDFIRE_EFFECT;
import static bassebombecraft.util.function.Predicates.hasDifferentIds;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.ResetResult2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
import bassebombecraft.operator.conditional.IsEffectActive2;
import bassebombecraft.operator.conditional.IsFrequencyActive2;
import bassebombecraft.operator.conditional.IsNot2;
import bassebombecraft.operator.entity.ApplyOperatorToEntity2;
import bassebombecraft.operator.entity.FindEntities2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Effect puts entity and fire and spreads the effect to nearby entities.
 * 
 * The effect has no effect on the player.
 */
public class WildfireEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = WildfireEffect.class.getSimpleName();

	/**
	 * Create AOE effect operators.
	 */
	static Supplier<Operator2> splAoeEffectOp = () -> {

		// IsEffectActive2 + AddEffect2: HACK to type cast from entity to living entity
		Function<Ports, LivingEntity> fnGetTarget = p -> (LivingEntity) applyV(getFnGetEntity2(), p);

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> wildfireEffectDuration.get().doubleValue();

		// create operator for AOE effect
		return new Sequence2(new IsNot2(new IsEffectActive2(fnGetTarget, WILDFIRE_EFFECT.get())),
				new AddEffect2(fnGetTarget, getBcSetEffectInstance1(), WILDFIRE_EFFECT.get(),
						wildfireEffectDuration.get(), wildfireEffectAmplifier.get()),
				new AddGraphicalEffectAtClient2(getFnGetEntity1(), getFnGetEntity2(), fnGetDuration, ELECTROCUTE));
	};

	/**
	 * Create operators to locate AOE candidates.
	 */
	static Supplier<Operator2> splAoeCoreOp = () -> {

		// IsFrequencyActive2: get frequency from configuration
		Function<Ports, Integer> fnGetFrequency = p -> wildfireEffectUpdateFrequency.get().intValue();

		// FindEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(getFnGetEntity1(), p).getPosition();

		// FindEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(getFnGetEntity1(), p).getEntityWorld();

		// FindEntities2: get function to create exclusion predicate using entity #1
		Function<Ports, Predicate<Entity>> fnGetPredicate = p -> hasDifferentIds(applyV(getFnGetEntity1(), p));

		// FindEntities2: get search range from configuration
		Function<Ports, Integer> fnGetRange = p -> wildfireEffectAoeRange.get().intValue();

		// create operator
		return new Sequence2(new ResetResult2(), new IsFrequencyActive2(fnGetFrequency),
				new FindEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange, getBcSetEntities1()),
				new ApplyOperatorToEntity2(getFnGetEntities1(), getBcSetEntity2(), splAoeEffectOp.get()));
	};

	/**
	 * AOE ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports aoePorts;

	/**
	 * AOE operator instance.
	 */
	Operator2 lazyInitAoeCoreOp = of(splAoeCoreOp);

	/**
	 * Constructor.
	 */
	public WildfireEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		aoePorts = getInstance();
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// find entities and add effect
		aoePorts.setEntity1(entity);
		run(aoePorts, lazyInitAoeCoreOp);

		// if not burning then set mob on fire
		if (entity.isBurning())
			return;
		
		entity.setFire(wildfireEffectDuration.get());
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int frequency = wildfireEffectUpdateFrequency.get();
		return getProxy().getServerFrequencyRepository().isActive(frequency);
	}

}
