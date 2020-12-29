package bassebombecraft.event.item;

import static bassebombecraft.ModConstants.RESPAWN;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;

import bassebombecraft.ModConstants;
import bassebombecraft.item.inventory.RespawnIdolInventoryItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEntityAttributeDefined2;
import bassebombecraft.operator.conditional.IsWorldAtServerSide2;
import bassebombecraft.operator.entity.Respawn2;
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
	 * Create operators.
	 */
	static Operator2 respawnOp = new Sequence2(new IsWorldAtServerSide2(), new IsEntityAttributeDefined2(RESPAWN),
			new Respawn2());

	@SubscribeEvent
	public static void handleLivingDeathEvent(LivingDeathEvent event) {
		Ports ports = getInstance();
		ports.setLivingEntity1(event.getEntityLiving());
		run(ports, respawnOp);
	}

}
