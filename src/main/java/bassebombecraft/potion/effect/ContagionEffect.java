package bassebombecraft.potion.effect;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository.Effect.CONTAGION;
import static bassebombecraft.config.ModConfiguration.contagionEffectAoeRange;
import static bassebombecraft.config.ModConfiguration.contagionEffectDuration;
import static bassebombecraft.config.ModConfiguration.contagionEffectUpdateFrequency;
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
import static bassebombecraft.potion.PotionUtils.resolveFirstEffect;
import static bassebombecraft.util.function.Functions.getFnCastToEntity;
import static bassebombecraft.util.function.Predicates.hasLivingEntitiesDifferentIds;

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
import bassebombecraft.operator.entity.potion.effect.CloneEffect2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Effect takes potion effect from entity and spreads the effect to nearby
 * entities. And spread the contagion effect as well.
 * 
 * The effect has no effect on the player.
 */
public class ContagionEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = ContagionEffect.class.getSimpleName();

	/**
	 * Create AOE effect operators for spread of potion effect.
	 * 
	 * Living entity #1 contains the input entity with the potion effect active.
	 * Living entity #2 contains with the target entity which will receive the
	 * effect.
	 */
	static Supplier<Operator2> splAoeEffectOp = () -> {

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
	static Supplier<Operator2> splAoeCoreOp = () -> {

		// IsFrequencyActive2: get frequency from configuration
		Function<Ports, Integer> fnGetFrequency = p -> contagionEffectUpdateFrequency.get().intValue();

		// FindLivingEntities2: get source position from source entity
		Function<Ports, BlockPos> fnGetSourcePos = p -> applyV(getFnGetLivingEntity1(), p).blockPosition();

		// FindLivingEntities2: get world from source entity
		Function<Ports, Level> fnGetWorld = p -> applyV(getFnGetLivingEntity1(), p).getCommandSenderWorld();

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
						splAoeEffectOp.get()));
	};

	/**
	 * AOE operator instance.
	 */
	Operator2 lazyInitAoeCoreOp = of(splAoeCoreOp);

	/**
	 * AOE ports for spread of potion effect.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports aoePorts;

	/**
	 * Constructor.
	 */
	public ContagionEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		aoePorts = getInstance();
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// exit if entity has no effects
		Optional<MobEffectInstance> optInstance = resolveFirstEffect(entity);
		if (!optInstance.isPresent())
			return;

		// find entities and add effect
		aoePorts.setLivingEntity1(entity);
		aoePorts.setEffectInstance1(optInstance.get());
		run(aoePorts, lazyInitAoeCoreOp);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int frequency = contagionEffectUpdateFrequency.get();
		return getProxy().getServerFrequencyRepository().isActive(frequency);
	}

}
