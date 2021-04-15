package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.decreaseSizeEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.decreaseSizeEffectDuration;
import static bassebombecraft.config.ModConfiguration.decreaseSizeIdolInventoryItem;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.potion.effect.RegisteredEffects.RECEIVE_AGGRO_EFFECT;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEntityOfType2;
import bassebombecraft.operator.conditional.IsNot2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;

/**
 * Decrease size idol implementation.
 */
public class DecreaseSizeIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = DecreaseSizeIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, LivingEntity> fnGetTarget = getFnGetLivingEntity2();
		BiConsumer<Ports, EffectInstance> bcSetEffectInstance = getBcSetEffectInstance1();
		return new Sequence2(new IsNot2(new IsEntityOfType2(fnGetTarget, PlayerEntity.class)),
				new AddEffect2(fnGetTarget, bcSetEffectInstance, RECEIVE_AGGRO_EFFECT.get(),
						decreaseSizeEffectDuration.get(), decreaseSizeEffectAmplifier.get()));
	};

	/**
	 * Operator to setup operator initializer function for lazy initialisation.
	 */
	static final Operator2 lazyInitOp = of(splOp);
	
	public DecreaseSizeIdolInventoryItem() {
		super(decreaseSizeIdolInventoryItem, new ExecuteOperatorOnTarget2(getInstance(), lazyInitOp));
	}
}
