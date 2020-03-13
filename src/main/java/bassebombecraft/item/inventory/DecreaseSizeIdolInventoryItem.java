package bassebombecraft.item.inventory;

import static bassebombecraft.ModConstants.DECREASE_SIZE_EFFECT;
import static bassebombecraft.config.ModConfiguration.decreaseSizeEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.decreaseSizeEffectDuration;
import static bassebombecraft.config.ModConfiguration.decreaseSizeIdolInventoryItem;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget;
import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEntityIsntType;
import bassebombecraft.operator.entity.potion.effect.AddEffect;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Decrease size idol implementation.
 */
public class DecreaseSizeIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = DecreaseSizeIdolInventoryItem.class.getSimpleName();

	static IntSupplier splDuration = () -> decreaseSizeEffectDuration.get();
	static IntSupplier splAmplifier = () -> decreaseSizeEffectAmplifier.get();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		AddEffect addEffectOp = new AddEffect(ops.getSplTargetEntity(), DECREASE_SIZE_EFFECT, splDuration,
				splAmplifier);
		Operator ifOp = new IfEntityIsntType(ops.getSplTargetEntity(), addEffectOp, PlayerEntity.class);
		ops.setOperator(ifOp);
		return ops;
	};

	public DecreaseSizeIdolInventoryItem() {
		super(ITEM_NAME, decreaseSizeIdolInventoryItem, new ExecuteOperatorOnTarget(splOp.get()));
	}
}
