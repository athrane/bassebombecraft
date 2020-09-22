package bassebombecraft.event.projectile;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.RECEIVE_AGGRO_EFFECT;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectDuration;
import static bassebombecraft.entity.projectile.ProjectileUtils.resolveInvoker;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.run;
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
import bassebombecraft.operator.entity.Explode2;
import bassebombecraft.operator.entity.ShootMeteor2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import bassebombecraft.operator.entity.raytraceresult.Charm2;
import bassebombecraft.operator.entity.raytraceresult.DigMobHole2;
import bassebombecraft.operator.entity.raytraceresult.ExplodeOnImpact2;
import bassebombecraft.operator.entity.raytraceresult.SpawnAnvil2;
import bassebombecraft.operator.entity.raytraceresult.SpawnCobweb2;
import bassebombecraft.operator.entity.raytraceresult.SpawnDecoy2;
import bassebombecraft.operator.entity.raytraceresult.TeleportInvoker2;
import bassebombecraft.operator.entity.raytraceresult.TeleportMob2;
import bassebombecraft.operator.projectile.modifier.tag.ReceiveAggro2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
	 * 
	 * The reason for not using the no-arg constructor for {@linkplain ShootMeteor2}
	 * is that it by default gets the target entity as living entity #2 from the
	 * ports.
	 * 
	 * But the event handler currently only sets the invoker (in the living entity
	 * #1 port) and the ray trace result (in the ray trace result #1 port) from the
	 * event.
	 * 
	 * In order for {@linkplain ShootMeteor2} to resolve the target entity from the
	 * ray trace result then its constructor is invoked with adapted functions.
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
				receiveAggroEffectDuration.get(), receiveAggroEffectAmplifier.get()));
	};

	/**
	 * Spawn decoy operator.
	 */
	static final Operator2 DECOY_OPERATOR = splDecoyOp.get();

	/**
	 * Explode operator.
	 */
	static final Operator2 EXPLODE_OPERATOR = new Explode2();

	/**
	 * Explode on impact operator.
	 */
	static final Operator2 EXPLODE_ON_IMPACT_OPERATOR = new ExplodeOnImpact2();

	/**
	 * Dig mob hole operator.
	 */
	static final Operator2 DIGMOBHOLE_OPERATOR = new DigMobHole2();

	/**
	 * Spawn cobweb operator.
	 */
	static final Operator2 COBWEB_OPERATOR = new SpawnCobweb2();

	/**
	 * Spawn anvil operator.
	 */
	static final Operator2 ANVIL_OPERATOR = new SpawnAnvil2();

	/**
	 * Create receive aggro operator.
	 * 
	 * The reason for not using the no-arg constructor for {@linkplain AddEffect2}
	 * is that it by default gets the target entity as living entity #1 from the
	 * ports.
	 * 
	 * But the event handler currently only sets the ray race result (in the ray
	 * trace result #1 port) from the event.
	 * 
	 * In order for {@linkplain AddEffect2} to resolve the target entity from the
	 * ray trace result then its constructor is invoked with adapted functions.
	 */
	static Supplier<Operator2> splReceiveAggroOp = () -> {
		Function<Ports, LivingEntity> fnGetTarget = p -> {
			RayTraceResult result = p.getRayTraceResult1();
			EntityRayTraceResult entityResult = (EntityRayTraceResult) result;
			return (LivingEntity) entityResult.getEntity();
		};
		BiConsumer<Ports, EffectInstance> bcSetEffectInstance = getBcSetEffectInstance1();
		return new Sequence2(new IsLivingEntityHitInRaytraceResult2(), new AddEffect2(fnGetTarget, bcSetEffectInstance,
				RECEIVE_AGGRO_EFFECT, receiveAggroEffectDuration.get(), receiveAggroEffectAmplifier.get()));
	};

	/**
	 * Receive aggro operator
	 */
	static final Operator2 RECEIVE_AGGGRO_OPERATOR = splReceiveAggroOp.get();

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

			// handle: teleport mob
			if (tags.contains(TeleportMob2.NAME))
				teleportMob(event);

			// handle: charm mob
			if (tags.contains(Charm2.NAME))
				charmMob(event);

			// handle: shoot meteor
			if (tags.contains(ShootMeteor2.NAME))
				shootMeteor(event);

			// handle: spawn decoy
			if (tags.contains(SpawnDecoy2.NAME))
				spawnDecoy(event);

			// handle: dig mob hole
			if (tags.contains(DigMobHole2.NAME))
				digMobHole(event);

			// handle: spawn cobweb
			if (tags.contains(SpawnCobweb2.NAME))
				spawnCobweb(event);

			// handle: spawn anvil
			if (tags.contains(SpawnAnvil2.NAME))
				spawnAnvil(event);

			// handle: explode on impact
			if (tags.contains(Explode2.NAME))
				explodeOnImpact(event);

			// handle: receive aggro
			if (tags.contains(ReceiveAggro2.NAME))
				receiveAggro(event);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@SubscribeEvent
	static public void handleLivingDeathEvent(LivingDeathEvent event) {
		try {

			// exit if handler is executed at client side
			if (isLogicalClient(event.getEntity()))
				return;

			// get damage source
			DamageSource source = event.getSource();

			// get immediate source, i.e. some projectile
			Entity immediateSource = source.getImmediateSource();

			// exit if immediate source isn't defined
			if (immediateSource == null)
				return;

			// get tags
			Set<String> tags = immediateSource.getTags();

			// exit if no tags is defined
			if (tags.isEmpty())
				return;

			// handle: explode when killed
			if (tags.contains(Explode2.NAME))
				explodeMobWhenKilled(event);

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

		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());
		run(ports, TELEPORT_INVOKER_OPERATOR);
	}

	/**
	 * Execute teleport mob operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void teleportMob(ProjectileImpactEvent event) {
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		run(ports, TELEPORT_MOB_OPERATOR);
	}

	/**
	 * Execute charm operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void charmMob(ProjectileImpactEvent event) {

		// exit if invoker couldn't be resolved
		Optional<LivingEntity> optInvoker = resolveInvoker(event);
		if (!optInvoker.isPresent())
			return;

		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());
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

		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());
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

		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setLivingEntity1(optInvoker.get());
		run(ports, DECOY_OPERATOR);
	}

	/**
	 * Execute dig mob hole operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void digMobHole(ProjectileImpactEvent event) {
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setWorld(event.getEntity().getEntityWorld());
		run(ports, DIGMOBHOLE_OPERATOR);
	}

	/**
	 * Execute spawn cobweb operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void spawnCobweb(ProjectileImpactEvent event) {
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setWorld(event.getEntity().getEntityWorld());
		run(ports, COBWEB_OPERATOR);
	}

	/**
	 * Execute spawn anvil operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void spawnAnvil(ProjectileImpactEvent event) {
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setWorld(event.getEntity().getEntityWorld());
		run(ports, ANVIL_OPERATOR);
	}

	/**
	 * Execute explode operator.
	 * 
	 * @param event living death event.
	 */
	static void explodeMobWhenKilled(LivingDeathEvent event) {
		Ports ports = getInstance();
		ports.setEntity1(event.getEntity());
		run(ports, EXPLODE_OPERATOR);
	}

	/**
	 * Execute explode operator.
	 * 
	 * @param event projectile impact death event.
	 */
	static void explodeOnImpact(ProjectileImpactEvent event) {
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		ports.setWorld(event.getEntity().getEntityWorld());
		run(ports, EXPLODE_ON_IMPACT_OPERATOR);
	}

	/**
	 * Execute receive aggro operator.
	 * 
	 * @param event projectile impact event.
	 */
	static void receiveAggro(ProjectileImpactEvent event) {
		Ports ports = getInstance();
		ports.setRayTraceResult1(event.getRayTraceResult());
		run(ports, RECEIVE_AGGGRO_OPERATOR);
	}

}
