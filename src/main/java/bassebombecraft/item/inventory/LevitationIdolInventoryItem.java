package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.levitationEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.levitationEffectDuration;
import static bassebombecraft.config.ModConfiguration.levitationIdolInventoryItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static net.minecraft.potion.Effects.LEVITATION;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnInvoker2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEntityOfType2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import bassebombecraft.operator.sound.PlaySound2;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

vanet.minecraft.world.effect.MobEffectsport java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnInvoker2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEntityOfType2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import bassebombecraft.operator.sound.PlaySound2;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Levitation idol implementation.
 */
public class LevitationIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = LevitationIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		return new Sequence2(new IsEntityOfType2(fnGetInvoker, Player.class),
				new AddEffect2(LEVITATION, levitationEffectDuration.get(), levitationEffectAmplifier.get()),
				new PlaySound2(levitationIdolInventoryItem.getSplSound()));
	};

	/**
	 * Operator to setup operator initializer function for lazy initialisation.
	 */
	static final Operator2 lazyInitOp = of(splOp);

	public LevitationIdolInventoryItem() {
		super(levitationIdolInventoryItem, new ExecuteOperatorOnInvoker2(getInstance(), lazyInitOp));
	}
}
