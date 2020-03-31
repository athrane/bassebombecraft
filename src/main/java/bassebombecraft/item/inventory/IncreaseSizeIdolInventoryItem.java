package bassebombecraft.item.inventory;

import static bassebombecraft.ModConstants.INCREASE_SIZE_EFFECT;
import static bassebombecraft.config.ModConfiguration.increaseSizeEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.increaseSizeEffectDuration;
import static bassebombecraft.config.ModConfiguration.increaseSizeIdolInventoryItem;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget;
import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IfEntityIsntType;
import bassebombecraft.operator.entity.potion.effect.AddEffect;
import bassebombecraft.operator.entity.potion.effect.AddEffectAtClient;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Increase size idol implementation.
 */
public class IncreaseSizeIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = IncreaseSizeIdolInventoryItem.class.getSimpleName();

	static IntSupplier splDuration = () -> increaseSizeEffectDuration.get();
	static IntSupplier splAmplifier = () -> increaseSizeEffectAmplifier.get();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		AddEffect addOp = new AddEffect(ops.getSplTargetEntity(), INCREASE_SIZE_EFFECT, splDuration, splAmplifier);
		AddEffectAtClient addOp2 = new AddEffectAtClient(ops.getSplTargetEntity(), addOp.getSplEffectInstance());
		Sequence2 seqOp = new Sequence2(addOp, addOp2);
		Operator ifOp = new IfEntityIsntType(ops.getSplTargetEntity(), seqOp, PlayerEntity.class);
		ops.setOperator(ifOp);
		return ops;
	};

	public IncreaseSizeIdolInventoryItem() {
		super(ITEM_NAME, increaseSizeIdolInventoryItem, new ExecuteOperatorOnTarget(splOp.get()));
	}
}
