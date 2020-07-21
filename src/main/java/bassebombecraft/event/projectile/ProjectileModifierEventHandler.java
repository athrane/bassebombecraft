package bassebombecraft.event.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.projectile.ProjectileUtils.resolveInvoker;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsLivingEntityHitInRaytraceResult2;
import bassebombecraft.operator.entity.ShootMeteor2;
import bassebombecraft.operator.entity.raytraceresult.Charm2;
import bassebombecraft.operator.entity.raytraceresult.TeleportInvoker2;
import bassebombecraft.operator.entity.raytraceresult.TeleportMob2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
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
	static final Operator2 TELEPORT_INVOKER_OPERATOR = new TeleportInvoker2();

	/**
	 * Teleport mob operator.
	 */
	static final Operator2 TELEPORT_MOB_OPERATOR = new TeleportMob2();

	/**
	 * Charm mob operator.
	 */
	static final Operator2 CHARM_OPERATOR = new Charm2();

	/**
	 * Create meteor operator.
	 */
	static Supplier<Operator2> splMeteorOp = () -> {
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		Function<Ports, LivingEntity> fnGetTarget = p -> {
			RayTraceResult result = p.getRayTraceResult1();
			EntityRayTraceResult entityResult = (EntityRayTraceResult) result;
			return (LivingEntity) entityResult.getEntity();
		};
		return new Sequence2(new IsLivingEntityHitInRaytraceResult2(), new ShootMeteor2(fnGetInvoker, fnGetTarget));
	};

	/**
	 * Meteor operator.
	 */
	static final Operator2 METEOR_OPERATOR = splMeteorOp.get();

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
				teleportInvoker(event);

			// handle: teleport invoker
			if (tags.contains(TeleportMob2.NAME))
				teleportMob(event);

			// handle: charm
			if (tags.contains(Charm2.NAME))
				charm(event);

			// handle: meteor
			if (tags.contains(ShootMeteor2.NAME))
				shootMeteor(event);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Execute teleport invoker operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void teleportInvoker(ProjectileImpactEvent event) {

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
	static void teleportMob(ProjectileImpactEvent event) {

		// create ports
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());

		// execute
		run(ports, TELEPORT_MOB_OPERATOR);
	}

	/**
	 * Execute charm operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void charm(ProjectileImpactEvent event) {

		// exit if invoker couldn't be resolved
		Optional<LivingEntity> optInvoker = resolveInvoker(event);
		if (!optInvoker.isPresent())
			return;

		// create ports
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());

		// execute
		run(ports, CHARM_OPERATOR);
	}

	/**
	 * Execute meteor operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void shootMeteor(ProjectileImpactEvent event) {

		// exit if invoker couldn't be resolved
		Optional<LivingEntity> optInvoker = resolveInvoker(event);
		if (!optInvoker.isPresent())
			return;

		// create ports
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());

		// execute
		run(ports, METEOR_OPERATOR);
	}

}
