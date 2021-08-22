package bassebombecraft.event.item;

import static bassebombecraft.entity.attribute.RegisteredAttributes.RESPAWN_ATTRIBUTE;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.LazyInitOp2.of;
import static bassebombecraft.operator.Operators2.run;

import java.util.function.Supplier;

import bassebombecraft.entity.attribute.RegisteredAttributes;
import bassebombecraft.item.inventory.RespawnIdolInventoryItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEntityAttributeSet2;
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
 * {@linkplain RegisteredAttributes.RESPAWN_ATTRIBUTE} defined.
 * 
 * The handler only executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class RespawnEventHandler {

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		return new Sequence2(new IsWorldAtServerSide2(), new IsEntityAttributeSet2(RESPAWN_ATTRIBUTE.get()),
				new Respawn2());
	};

	/**
	 * Operator to setup operator initializer function for lazy initialisation.
	 */
	static final Operator2 lazyInitOp = of(splOp);

	@SubscribeEvent
	public static void handleLivingDeathEvent(LivingDeathEvent event) {
		Ports ports = getInstance();
		ports.setLivingEntity1(event.getEntityLiving());
		run(ports, lazyInitOp);
	}

}
