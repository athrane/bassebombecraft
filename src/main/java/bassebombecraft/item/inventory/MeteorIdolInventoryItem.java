package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.meteorIdolInventoryItem;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.entity.ShootMeteor2;

/**
 * Meteor idol implementation.
 */
public class MeteorIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MeteorIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		return new ShootMeteor2();
	};

	public MeteorIdolInventoryItem() {
		super(ITEM_NAME, meteorIdolInventoryItem, new ExecuteOperatorOnTarget2(getInstance(), splOp.get()));
	}
}
