package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.decreaseSizeEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.decreaseSizeEffectDuration;
import static bassebombecraft.config.ModConfiguration.decreaseSizeIdolInventoryItem;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.potion.effect.RegisteredEffects.DECREASE_SIZE_EFFECT;

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
import bassebombecraft.operator.entity.potion.effect.AddEffectAtClient2;
import bassebombecraft.operator.sound.PlaySound2;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

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
		BiConsumer<Ports, MobEffectInstance> bcSetEffectInstance = getBcSetEffectInstance1();
		MobEffect effect = DECREASE_SIZE_EFFECT.get();
		int duration = decreaseSizeEffectDuration.get();
		int amplifier = decreaseSizeEffectAmplifier.get();
		return new Sequence2(new IsNot2(new IsEntityOfType2(fnGetTarget, Player.class)),
				new AddEffect2(fnGetTarget, bcSetEffectInstance, effect, duration, amplifier), new AddEffectAtClient2(),
				new PlaySound2(decreaseSizeIdolInventoryItem.getSplSound()));
	};

	/**
	 * Operator to setup operator initializer function for lazy initialisation.
	 */
	static final Operator2 lazyInitOp = of(splOp);

	public DecreaseSizeIdolInventoryItem() {
		super(decreaseSizeIdolInventoryItem, new ExecuteOperatorOnTarget2(getInstance(), lazyInitOp));
	}
}
