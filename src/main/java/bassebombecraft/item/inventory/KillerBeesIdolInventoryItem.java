package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.killerBeesIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget;
import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEntityIsntType;
import bassebombecraft.operator.entity.SpawnKillerBee;
import net.minecraft.entity.passive.BeeEntity;

/**
 * Killer bees idol implementation.
 */
public class KillerBeesIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = KillerBeesIdolInventoryItem.class.getSimpleName();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		SpawnKillerBee spawnOp = new SpawnKillerBee(ops.getSplLivingEntity(), ops.getSplTargetEntity());
		Operator ifOp = new IfEntityIsntType(ops.getSplTargetEntity(), spawnOp, BeeEntity.class);
		ops.setOperator(ifOp);
		return ops;
	};

	public KillerBeesIdolInventoryItem() {
		super(killerBeesIdolInventoryItem, new ExecuteOperatorOnTarget(splOp.get()));
	}
}
