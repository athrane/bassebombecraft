package bassebombecraft.item.inventory;

import static bassebombecraft.ModConstants.MARKER_ATTRIBUTE_IS_SET;
import static bassebombecraft.config.ModConfiguration.respawnIdolInventoryItem;
import static bassebombecraft.entity.attribute.RegisteredAttributes.RESPAWN_ATTRIBUTE;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.SetEntityAttribute2;
import net.minecraft.world.entity.LivingEntity;

/**
 * Respawn idol implementation.
 */
public class RespawnIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = RespawnIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, LivingEntity> fnGetTarget = getFnGetLivingEntity2();
		return new SetEntityAttribute2(fnGetTarget, RESPAWN_ATTRIBUTE.get(), MARKER_ATTRIBUTE_IS_SET);
	};
	
	/**
	 * Operator to setup operator initializer function for lazy initialisation.
	 */
	static final Operator2 lazyInitOp = of(splOp);
	
	public RespawnIdolInventoryItem() {
		super(respawnIdolInventoryItem, new ExecuteOperatorOnTarget2(getInstance(), lazyInitOp));

	}
}
