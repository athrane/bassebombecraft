package bassebombecraft.item.inventory;

import static bassebombecraft.ModConstants.RESPAWN;
import static bassebombecraft.config.ModConfiguration.respawnIdolInventoryItem;

import java.util.function.Supplier;

import bassebombecraft.ModConstants;
import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.entity.AddAttribute;

/**
 * Respawn idol implementation.
 */
public class RespawnIdolInventoryItem extends GenericInventoryItem {

	/**
	 * Value for {@linkplain ModConstants.RESPAWN}. The value doesn't carry any
	 * significance
	 */
	static final double RESPAWN_VALUE = 1.0D;

	public static final String ITEM_NAME = RespawnIdolInventoryItem.class.getSimpleName();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		AddAttribute addAttrOp = new AddAttribute(ops.getSplTargetEntity(), RESPAWN, RESPAWN_VALUE);
		ops.setOperator(addAttrOp);
		return ops;
	};

	public RespawnIdolInventoryItem() {
		super(ITEM_NAME, respawnIdolInventoryItem, new ExecuteOperatorOnTarget(splOp.get()));
	}
}
