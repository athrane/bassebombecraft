package bassebombecraft.potion.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.CONTAGION;
import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.CONTAGION_SPREAD;
import static bassebombecraft.config.ModConfiguration.contagionEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.contagionEffectAoeRange;
import static bassebombecraft.config.ModConfiguration.contagionEffectDuration;
import static bassebombecraft.config.ModConfiguration.contagionEffectUpdateFrequency;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getBcSetLivingEntities1;
import static bassebombecraft.operator.DefaultPorts.getBcSetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getFnEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.potion.effect.RegisteredEffects.CONTAGION_EFFECT;
import static bassebombecraft.util.function.Functions.getFnCastToEntity;
import static bassebombecraft.util.function.Predicates.hasLivingEntitiesDifferentIds;
import static bassebombecraft.util.function.Predicates.hasNotPotionEffect;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.ResetResult2;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.client.rendering.AddGraphicalEffectAtClient2;
import bassebombecraft.operator.conditional.IsFrequencyActive2;
import bassebombecraft.operator.entity.ApplyOperatorToLivingEntity2;
import bassebombecraft.operator.entity.FindLivingEntities2;
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
 * entities. And spread the contagion effect as well.
 * 
 * The effect has no effect on the player.
 */
public class ContagionEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = ContagionEffect.class.getSimpleName();

	/**
	 * Create AOE effect operators for spread of contagion.
	 * 
	 * Living entity #1 contains the input entity with contagion. Living entity #2
	 * contains with the target entity which will receive contagion.
	 */
	static Supplier<Operator2> splAoeEffectOp = () -> {

		// AddGraphicalEffectAtClient2: type cast living entity #1 to entity
		Function<Ports, Entity> fnCastToEntity = getFnGetLivingEntity1().andThen(getFnCastToEntity());

		// AddGraphicalEffectAtClient2: type cast living entity #2 to entity
		Function<Ports, Entity> fnCastToEntity2 = getFnGetLivingEntity2().andThen(getFnCastToEntity());

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> contagionEffectDuration.get().doubleValue();

		// create operator for AOE effect
		return new Sequence2(
				new AddEffect2(getFnGetLivingEntity2(), getBcSetEffectInstance1(), CONTAGION_EFFECT.get(),
						contagionEffectDuration.get(), contagionEffectAmplifier.get()),
				new AddGraphicalEffectAtClient2(fnCastToEntity, fnCastToEntity2, fnGetDuration, CONTAGION_SPREAD));
	};

	/**
	 * Create operators to locate AOE candidates for spread of contagion.
	 * 
	 * Living entity #1 contains the input entity with the potion effect active.
	 * Living entity #2 is updated with the target entity.
	 */
	static Supplier<Operator2> splAoeCoreOp = () -> {

		// IsFrequencyActive2: get frequency from configuration
		Function<Ports, Integer> fnGetFrequency = p -> contagionEffectUpdateFrequency.get().intValue();

		// FindLivingEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(getFnGetLivingEntity1(), p).getPosition();

		// FindLivingEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(getFnGetLivingEntity1(), p).getEntityWorld();

		// FindLivingEntities2: exclude self using entity #1 and entity with contagion
		Function<Ports, Predicate<LivingEntity>> fnGetPredicate = p -> hasLivingEntitiesDifferentIds(
				applyV(getFnGetLivingEntity1(), p)).and(hasNotPotionEffect(CONTAGION_EFFECT.get()));

		// FindLivingEntities2: get search range from configuration
		Function<Ports, Integer> fnGetRange = p -> contagionEffectAoeRange.get().intValue();

		// create operator
		return new Sequence2(new ResetResult2(), new IsFrequencyActive2(fnGetFrequency),
				new FindLivingEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange,
						getBcSetLivingEntities1()),
				new ApplyOperatorToLivingEntity2(getFnGetLivingEntities1(), getBcSetLivingEntity2(),
						splAoeEffectOp.get()));
	};

	/**
	 * AOE operator instance.
	 */
	Operator2 lazyInitAoeCoreOp = of(splAoeCoreOp);

	/**
	 * Create AOE effect operators for spread of potion effect.
	 * 
	 * Living entity #1 contains the input entity with the potion effect active.
	 * Living entity #2 contains with the target entity which will receive the
	 * effect.
	 */
	static Supplier<Operator2> splAoeEffectOp2 = () -> {

		// AddGraphicalEffectAtClient2: type cast living entity #1 to entity
		Function<Ports, Entity> fnCastToEntity = getFnGetLivingEntity1().andThen(getFnCastToEntity());

		// AddGraphicalEffectAtClient2: type cast living entity #2 to entity
		Function<Ports, Entity> fnCastToEntity2 = getFnGetLivingEntity2().andThen(getFnCastToEntity());

		// AddGraphicalEffectAtClient2: get effect duration from configuration
		Function<Ports, Double> fnGetDuration = p -> contagionEffectDuration.get().doubleValue();

		// create operator for AOE effect
		return new Sequence2(new CloneEffect2(getFnGetLivingEntity2(), getFnEffectInstance1()),
				new AddGraphicalEffectAtClient2(fnCastToEntity, fnCastToEntity2, fnGetDuration, CONTAGION));
	};

	/**
	 * Create operators to locate AOE candidates for spread of potion effect
	 * 
	 * Living entity #1 contains the input entity with the potion effect active.
	 * Living entity #2 is updated with the target entity.
	 */
	static Supplier<Operator2> splAoeCoreOp2 = () -> {

		// IsFrequencyActive2: get frequency from configuration
		Function<Ports, Integer> fnGetFrequency = p -> contagionEffectUpdateFrequency.get().intValue();

		// FindLivingEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(getFnGetLivingEntity1(), p).getPosition();

		// FindLivingEntities2: get world from source entity
		Function<Ports, World> fnGetWorld = p -> applyV(getFnGetLivingEntity1(), p).getEntityWorld();

		// FindLivingEntities2: exclude self using entity #1
		Function<Ports, Predicate<LivingEntity>> fnGetPredicate = p -> hasLivingEntitiesDifferentIds(
				applyV(getFnGetLivingEntity1(), p));

		// FindLivingEntities2: get search range from configuration
		Function<Ports, Integer> fnGetRange = p -> contagionEffectAoeRange.get().intValue();

		// create operator
		return new Sequence2(new ResetResult2(), new IsFrequencyActive2(fnGetFrequency),
				new FindLivingEntities2(fnGetSourcePos, fnGetWorld, fnGetPredicate, fnGetRange,
						getBcSetLivingEntities1()),
				new ApplyOperatorToLivingEntity2(getFnGetLivingEntities1(), getBcSetLivingEntity2(),
						splAoeEffectOp2.get()));
	};

	/**
	 * AOE operator instance.
	 */
	Operator2 lazyInitAoeCoreOp2 = of(splAoeCoreOp2);

	/**
	 * AOE ports for spread of contagion.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports aoePorts;

	/**
	 * AOE ports for spread of potion effect.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports aoePorts2;

	/**
	 * Constructor.
	 */
	public ContagionEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		aoePorts = getInstance().enableDebug();
		aoePorts2 = getInstance().enableDebug();
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// find entities and add contagion
		aoePorts.setLivingEntity1(entity);
		run(aoePorts, lazyInitAoeCoreOp);

		// exit if entity has no effects
		Optional<EffectInstance> optInstance = resolveEffectInstance(entity);
		if (!optInstance.isPresent())
			return;

		// find entities and add effect
		//aoePorts2.setLivingEntity1(entity);
		//aoePorts2.setEffectInstance1(optInstance.get());
		//run(aoePorts2, lazyInitAoeCoreOp2);
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
