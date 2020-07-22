package bassebombecraft.event.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.RECEIVE_AGGRO_EFFECT;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectDuration;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.projectile.ProjectileUtils.resolveInvoker;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsLivingEntityHitInRaytraceResult2;
import bassebombecraft.operator.entity.ShootMeteor2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import bassebombecraft.operator.entity.raytraceresult.Charm2;
import bassebombecraft.operator.entity.raytraceresult.SpawnDecoy2;
import bassebombecraft.operator.entity.raytraceresult.TeleportInvoker2;
import bassebombecraft.operator.entity.raytraceresult.TeleportMob2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
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

	/**
	 * Create decoy operator.
	 * 
	 * The reason for not using the no-arg constructor for {@linkplain AddEffect2}
	 * is that it by default gets the target entity as living entity #1 from the
	 * ports.
	 * 
	 * But {@linkplain SpawnDecoy2} sets the created entity as living entity #2 in
	 * the ports.
	 * 
	 * In order for {@linkplain AddEffect2} to pick up the target from living entity
	 * #2 then its constructor is invoked with adapted functions.
	 */
	static Supplier<Operator2> splDecoyOp = () -> {
		Function<Ports, LivingEntity> fnGetTarget = getFnGetLivingEntity2();
		BiConsumer<Ports, EffectInstance> bcSetEffectInstance = getBcSetEffectInstance1();
		return new Sequence2(new SpawnDecoy2(), new AddEffect2(fnGetTarget, bcSetEffectInstance, RECEIVE_AGGRO_EFFECT,
				receiveAggroEffectDuration.get(), receiveAggroEffectAmplifier.get())
		);
	};

	/**
	 * Spawn decoy operator.
	 */
	static final Operator2 DECOY_OPERATOR = splDecoyOp.get();

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

			// exit if no tags is defined
			if (tags.isEmpty())
				return;

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

			// handle: decoy
			if (tags.contains(SpawnDecoy2.NAME))
				spawnDecoy(event);

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

	/**
	 * Execute spawn decoy operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void spawnDecoy(ProjectileImpactEvent event) {

		// exit if invoker couldn't be resolved
		Optional<LivingEntity> optInvoker = resolveInvoker(event);
		if (!optInvoker.isPresent())
			return;

		// create ports
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());

		// execute
		run(ports, DECOY_OPERATOR);
	}

}
