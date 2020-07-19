package bassebombecraft.event.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.projectile.ProjectileUtils.resolveInvoker;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.Optional;
import java.util.Set;

import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.TeleportInvoker2;
import bassebombecraft.operator.entity.TeleportMob2;
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

	/**
	 * Teleport mob operator.
	 */
	static final TeleportMob2 TELEPORT_MOB_OPERATOR = new TeleportMob2();

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

			// handle: teleport invoker
			if (tags.contains(TeleportInvoker2.NAME))
				handleTeleportInvoker(event);

			// handle: teleport invoker
			if (tags.contains(TeleportMob2.NAME))
				handleTeleportMob(event);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Execute teleport invoker operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void handleTeleportInvoker(ProjectileImpactEvent event) {

		// exit if invoker couldn't be resolved
		Optional<LivingEntity> optInvoker = resolveInvoker(event);
		if (!optInvoker.isPresent())
			return;

		// create ports
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());

		// execute
		run(ports, TELEPORT_INVOKER_OPERATOR);
	}

	/**
	 * Execute teleport mob operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void handleTeleportMob(ProjectileImpactEvent event) {

		// create ports
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());

		// execute
		run(ports, TELEPORT_MOB_OPERATOR);
	}

}
