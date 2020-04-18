package bassebombecraft.event.item;

import static bassebombecraft.ModConstants.RESPAWN;

import bassebombecraft.ModConstants;
import bassebombecraft.item.inventory.RespawnIdolInventoryItem;
import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEntityAttributeIsDefined;
import bassebombecraft.operator.conditional.IfWorldAtServerSide;
import bassebombecraft.operator.entity.Respawn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for the {@linkplain RespawnIdolInventoryItem} which respawns
 * any number of instances of a tagged and dead entity.
 * 
 * An entity is tagged if it has the entity attribute
 * {@linkplain ModConstants.RESPAWN} defined.
 * 
 * The handler only executes events SERVER side. 
 */
@Mod.EventBusSubscriber
public class RespawnEventHandler {

	/**
	 * Operator execution.
	 */
	static Operators ops;

	static {
		ops = new Operators();
		Operator respawnOp = new Respawn(ops.getSplLivingEntity());
		Operator ifOp2 = new IfEntityAttributeIsDefined(ops.getSplLivingEntity(), respawnOp, RESPAWN);		
		Operator ifOp = new IfWorldAtServerSide(ops.getSplLivingEntity(), ifOp2); ;		
		ops.setOperator(ifOp);
	}

	@SubscribeEvent
	public static void handleLivingDeathEvent(LivingDeathEvent event) {
		ops.run(event.getEntityLiving());
	}
}
