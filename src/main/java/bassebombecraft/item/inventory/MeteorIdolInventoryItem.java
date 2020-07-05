package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.meteorIdolInventoryItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.ShootMeteor2;
import net.minecraft.entity.LivingEntity;

/**
 * Meteor idol implementation.
 */
public class MeteorIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MeteorIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2[]> splOp = () -> {

		/**
		 * create functions for accessing the ports:
		 * 
		 * 1) ports.livingentity#1 is used for the invoker.
		 * 
		 * 2) ports.livingentity#2 is used for the target.
		 * 
		 * NB: the ports is updated by the ExecuteOperatorOnInvoker2.
		 */
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		Function<Ports, LivingEntity> fnGetTarget = getFnGetLivingEntity2();
		Operator2[] ops = new Operator2[] { new ShootMeteor2(fnGetInvoker, fnGetTarget) };
		return ops;
	};

	public MeteorIdolInventoryItem() {
		super(ITEM_NAME, meteorIdolInventoryItem, new ExecuteOperatorOnTarget2(getInstance(), splOp.get()));
	}
}
