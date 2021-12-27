package bassebombecraft.potion.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.CONTAGION;
import static bassebombecraft.config.ModConfiguration.contagionEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.contagionEffectAoeRange;
import static bassebombecraft.config.ModConfiguration.contagionEffectDuration;
import static bassebombecraft.config.ModConfiguration.contagionEffectUpdateFrequency;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getBcSetEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.potion.effect.RegisteredEffects.CONTAGION_EFFECT;
import static bassebombecraft.util.function.Predicates.hasDifferentIds;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
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
import bassebombecraft.operator.entity.potion.effect.CloneEffect2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Effect takes potion effect from entity and spreads the effect to nearby
 * entities.
 * 
 * The effect has no effect on the player.
 */
public class ContagionEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = ContagionEffect.class.getSimpleName();

	/**
	 * Create AOE effect operators: 
	 */
	static Supplier<Operator2> splAoeEffectOp = () -> {

		// IsEffectActive2 + AddEffect2: HACK to type cast from entity to living entity
		Function<Ports, LivingEntity> fnGetTarget = p -> (LivingEntity) applyV(getFnGetEntity2(), p);

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> contagionEffectDuration.get().doubleValue();

		// create operator for AOE effect
		return new Sequence2(new IsNot2(new IsEffectActive2(fnGetTarget, CONTAGION_EFFECT.get())),
				new CloneEffect2(fnGetTarget, getFnEffectInstance1()),
				new AddEffect2(fnGetTarget, getBcSetEffectInstance1(), CONTAGION_EFFECT.get(),
						contagionEffectDuration.get(), contagionEffectAmplifier.get()),
				new AddGraphicalEffectAtClient2(getFnGetEntity1(), getFnGetEntity2(), fnGetDuration, CONTAGION));
	};

	/**
	 * Create AOE effect operators.
	 */
	static Supplier<Operator2> splAoeEffectOp2 = () -> {

		// IsEffectActive2 + AddEffect2: HACK to type cast from entity to living entity
		Function<Ports, LivingEntity> fnGetTarget = p -> (LivingEntity) applyV(getFnGetEntity2(), p);

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> contagionEffectDuration.get().doubleValue();

		// create operator for AOE effect
		return new Sequence2(new IsNot2(new IsEffectActive2(fnGetTarget, CONTAGION_EFFECT.get())),
				new CloneEffect2(fnGetTarget, getFnEffectInstance1()),
				new AddEffect2(fnGetTarget, getBcSetEffectInstance1(), CONTAGION_EFFECT.get(),
						contagionEffectDuration.get(), contagionEffectAmplifier.get()),
				new AddGraphicalEffectAtClient2(getFnGetEntity1(), getFnGetEntity2(), fnGetDuration, CONTAGION));
	};
	
	/**
	 * Create operators to locate AOE candidates.
	 */
	static Supplier<Operator2> splAoeCoreOp = () -> {

		// IsFrequencyActive2: get frequency from configuration
		Function<Ports, Integer> fnGetFrequency = p -> contagionEffectUpdateFrequency.get().intValue();

		// FindEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(getFnGetEntity1(), p).getPosition();

		// FindEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(getFnGetEntity1(), p).getEntityWorld();

		// FindEntities2: get function to create exclusion predicate using entity #1
		Function<Ports, Predicate<Entity>> fnGetPredicate = p -> hasDifferentIds(applyV(getFnGetEntity1(), p));

		// FindEntities2: get search range from configuration
		Function<Ports, Integer> fnGetRange = p -> contagionEffectAoeRange.get().intValue();

		// create operator
		return new Sequence2(new ResetResult2(), new IsFrequencyActive2(fnGetFrequency),
				new FindEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange, getBcSetEntities1()),
				new ApplyOperatorToEntity2(getFnGetEntities1(), getBcSetEntity2(), splAoeEffectOp.get()));
	};

	/**
	 * AOE operator instance.
	 */
	Operator2 lazyInitAoeCoreOp = of(splAoeCoreOp);
	
	/**
	 * AOE ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports aoePorts;

	/**
	 * Constructor.
	 */
	public ContagionEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		aoePorts = getInstance().enableDebug();
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		Optional<EffectInstance> optInstance = resolveEffectInstance(entity);
		BassebombeCraft.getBassebombeCraft().getLogger().debug("optInstance=" + optInstance);

		// exit if null instance is returned
		if (!optInstance.isPresent())
			return;

		// find entities and add effect
		aoePorts.setEntity1(entity);
		aoePorts.setEffectInstance1(optInstance.get());
		run(aoePorts, lazyInitAoeCoreOp);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int frequency = contagionEffectUpdateFrequency.get();
		return getProxy().getServerFrequencyRepository().isActive(frequency);
	}

	/**
	 * Resolves first effect which isn't contagion effect.
	 * 
	 * @param entity to resolve effects from.
	 * 
	 * @return Resolves first effect which isn't contagion effect. If no effect
	 *         could resolved then empty optional is returned.
	 */
	Optional<EffectInstance> resolveEffectInstance(LivingEntity entity) {
		Collection<EffectInstance> effects = entity.getActivePotionEffects();
		return effects.stream().filter(e -> !(e.getPotion().equals(CONTAGION_EFFECT.get()))).findFirst();
	}

}
