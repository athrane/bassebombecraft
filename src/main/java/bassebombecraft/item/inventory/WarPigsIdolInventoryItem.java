package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.spawnWarPigDamage;
import static bassebombecraft.config.ModConfiguration.spawnWarPigMovementSpeed;
import static bassebombecraft.config.ModConfiguration.warPigsIdolInventoryItem;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperator;
import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEntityIsntType;
import bassebombecraft.operator.entity.SpawnWarPig;
import net.minecraft.entity.passive.PigEntity;

/**
 * War pigs idol implementation.
 */
public class WarPigsIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = WarPigsIdolInventoryItem.class.getSimpleName();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		SpawnWarPig spawnOp = new SpawnWarPig(ops.getSplLivingEntity(), ops.getSplTargetEntity());
		Operator ifOp = new IfEntityIsntType(ops.getSplTargetEntity(), spawnOp, PigEntity.class);
		ops.setOperator(ifOp);
		return ops;
	};

	public WarPigsIdolInventoryItem() {
		super(ITEM_NAME, warPigsIdolInventoryItem, new ExecuteOperator(splOp.get()));
	}
}
