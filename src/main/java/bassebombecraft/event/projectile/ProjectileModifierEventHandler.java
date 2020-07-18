package bassebombecraft.event.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.projectile.ProjectileUtils.resolveInvoker;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.Optional;
import java.util.Set;

import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.TeleportInvoker2;
import bassebombecraft.operator.projectile.modifier.TeleportInvokerProjectileModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for projectile modifier updates for composite items.
 * 
 * The handler executes events SERVER side.
 */
@Mod.EventBusSubscriber
public class ProjectileModifierEventHandler {

	/**
	 * Teleport invoker operator.
	 */
	static final TeleportInvoker2 TELEPORT_INVOKER_OPERATOR = new TeleportInvoker2();

	@SubscribeEvent
	static public void handleProjectileImpactEvent(ProjectileImpactEvent event) {
		try {

			// exit if handler is executed at client side
			if (isLogicalClient(event.getEntity()))
				return;

			// get projectile
			Entity projectile = event.getEntity();

			// get tags
			Set<String> tags = projectile.getTags();

			if (tags.contains(TeleportInvokerProjectileModifier.NAME)) {

				// exit if invoker couldn't be resolved
				Optional<LivingEntity> optInvoker = resolveInvoker(event);
				if (!optInvoker.isPresent())
					return;

				// create ports
				Ports ports = getInstance();
				ports.setRayTraceResult1(event.getRayTraceResult());
				ports.setLivingEntity1(optInvoker.get());

				// set projectile, TODO: should this be done?
				ports.setEntity1(event.getEntity());

				// execute
				Operators2.run(ports, TELEPORT_INVOKER_OPERATOR);
			}

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}
}
