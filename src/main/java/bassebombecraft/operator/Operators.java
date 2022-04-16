package bassebombecraft.operator;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.function.Supplier;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

/**
 * Class for execution of operator.
 * 
 * @deprecated Version 1 of the operator framework should be replaced with
 *             version 2, {@linkplain Operators2}.
 */
@Deprecated
public class Operators {

	/**
	 * Event.
	 */
	LivingDamageEvent livingDamageEvent;

	/**
	 * Event.
	 */
	RenderLivingEvent<Player, PlayerModel<Player>> renderLivingEvent;

	/**
	 * Living entity instance.
	 */
	LivingEntity livingEntity;

	/**
	 * Target entity instance.
	 */
	LivingEntity targetEntity;

	/**
	 * Effect instance.
	 */
	MobEffectInstance effectInstance;

	/**
	 * Ray trace result
	 */
	HitResult rayTraceResult;

	/**
	 * {@linkplain LivingDamageEvent} supplier.
	 */
	Supplier<LivingDamageEvent> splEvent = () -> livingDamageEvent;

	/**
	 * {@linkplain RenderLivingEvent} supplier.
	 */
	Supplier<RenderLivingEvent<Player, PlayerModel<Player>>> splRenderLivingEvent = () -> renderLivingEvent;

	/**
	 * {@linkplain LivingEntity} supplier.
	 */
	Supplier<LivingEntity> splLivingEntity = () -> livingEntity;

	/**
	 * {@linkplain Entity} supplier.
	 */
	Supplier<LivingEntity> splTargetEntity = () -> targetEntity;

	/**
	 * {@linkplain EffectInstance} supplier.
	 */
	Supplier<MobEffectInstance> splEffectInstance = () -> effectInstance;

	/**
	 * {@linkplain RayTraceResult} supplier.
	 */
	Supplier<HitResult> splRayTraceResult = () -> rayTraceResult;

	/**
	 * Operator to execute, initially the null operator.
	 */
	Operator operator = new NullOp();

	/**
	 * Get {@linkplain LivingDamageEvent} supplier.
	 * 
	 * @return event supplier.
	 */
	public Supplier<LivingDamageEvent> getSplLivingDamageEvent() {
		return splEvent;
	}

	/**
	 * Get {@linkplain RenderLivingEvent} supplier.
	 * 
	 * @return event supplier.
	 */
	public Supplier<RenderLivingEvent<Player, PlayerModel<Player>>> getSplRenderLivingEvent() {
		return splRenderLivingEvent;
	}

	/**
	 * Get {@linkplain LivingEntity} supplier.
	 * 
	 * @return entity supplier.
	 */
	public Supplier<LivingEntity> getSplLivingEntity() {
		return splLivingEntity;
	}

	/**
	 * Get {@linkplain Entity} supplier.
	 * 
	 * @return target entity supplier.
	 */
	public Supplier<LivingEntity> getSplTargetEntity() {
		return splTargetEntity;
	}

	/**
	 * Get {@linkplain EffectInstance} supplier.
	 * 
	 * @return effect supplier.
	 */
	public Supplier<MobEffectInstance> getSplEffectInstance() {
		return splEffectInstance;
	}

	/**
	 * Get {@linkplain RayTraceResult} supplier.
	 * 
	 * @return result supplier.
	 */
	public Supplier<HitResult> getSplRayTraceResult() {
		return splRayTraceResult;
	}

	/**
	 * Set operator.
	 * 
	 * @param operator operator.
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Reset all fields.
	 */
	void reset() {
		livingDamageEvent = null;
		renderLivingEvent = null;
		livingEntity = null;
		targetEntity = null;
		effectInstance = null;
	}

	/**
	 * Execute operator.
	 * 
	 * @param event          input event.
	 * @param entity         input entity.
	 * @param effectInstance input effect instance.
	 */
	public void run(LivingDamageEvent event, LivingEntity entity, MobEffectInstance effectInstance) {
		this.livingDamageEvent = event;
		this.livingEntity = entity;
		this.effectInstance = effectInstance;
		try {
			operator.run();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		} finally {
			reset();
		}
	}

	/**
	 * Execute operator.
	 * 
	 * @param event  input event.
	 * @param entity input entity.
	 */
	public void run(RenderLivingEvent<Player, PlayerModel<Player>> event, LivingEntity entity) {
		this.renderLivingEvent = event;
		this.livingEntity = entity;
		try {
			operator.run();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		} finally {
			reset();
		}
	}

	/**
	 * Execute operator.
	 * 
	 * @param entity input entity
	 * @param result input ray trace result.
	 */
	public void run(LivingEntity entity, HitResult result) {
		this.livingEntity = entity;
		this.rayTraceResult = result;
		try {
			operator.run();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		} finally {
			reset();
		}
	}

	/**
	 * Execute operator.
	 * 
	 * @param entity input entity
	 * @param target input target entity.
	 */
	public void run(LivingEntity entity, LivingEntity target) {
		this.livingEntity = entity;
		this.targetEntity = target;
		try {
			operator.run();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		} finally {
			reset();
		}
	}

	/**
	 * Execute operator.
	 * 
	 * @param entity input entity
	 */
	public void run(LivingEntity entity) {
		this.livingEntity = entity;
		try {
			operator.run();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		} finally {
			reset();
		}
	}

	/**
	 * Get {@linkplain LivingEntity} supplier which returns entity contained in
	 * {@linkplain RayTraceResult}.
	 * 
	 * @return entity supplier which resolves entity contained in ray trace result.
	 */
	public Supplier<LivingEntity> getSplRaytracedEntity() {
		Supplier<LivingEntity> splRaytracedEntity = () -> {
			Entity entity = ((EntityHitResult) rayTraceResult).getEntity();
			return (LivingEntity) entity;
		};
		return splRaytracedEntity;
	}

}
