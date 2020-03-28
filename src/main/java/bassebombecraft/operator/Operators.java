package bassebombecraft.operator;

import java.util.function.Supplier;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

/**
 * Class for execution of operator.
 */
public class Operators {

	/**
	 * Event.
	 */
	LivingDamageEvent livingDamageEvent;

	/**
	 * Event.
	 */
	RenderLivingEvent<PlayerEntity, PlayerModel<PlayerEntity>> renderLivingEvent;

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
	EffectInstance effectInstance;

	/**
	 * Ray trace result 
	 */
	RayTraceResult rayTraceResult;
	
	/**
	 * {@linkplain LivingDamageEvent} supplier.
	 */
	Supplier<LivingDamageEvent> splEvent = () -> livingDamageEvent;

	/**
	 * {@linkplain RenderLivingEvent} supplier.
	 */
	Supplier<RenderLivingEvent<PlayerEntity, PlayerModel<PlayerEntity>>> splRenderLivingEvent = () -> renderLivingEvent;

	/**
	 * {@linkplain LivingEntity} supplier.
	 */
	Supplier<LivingEntity> splEntity = () -> livingEntity;

	/**
	 * {@linkplain Entity} supplier.
	 */
	Supplier<LivingEntity> splTargetEntity = () -> targetEntity;

	/**
	 * {@linkplain EffectInstance} supplier.
	 */
	Supplier<EffectInstance> splEffectInstance = () -> effectInstance;

	/**
	 * {@linkplain RayTraceResult} supplier.
	 */
	Supplier<RayTraceResult> splRayTraceResult = () -> rayTraceResult;
	
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
	public Supplier<RenderLivingEvent<PlayerEntity, PlayerModel<PlayerEntity>>> getSplRenderLivingEvent() {
		return splRenderLivingEvent;
	}

	/**
	 * Get {@linkplain LivingEntity} supplier.
	 * 
	 * @return entity supplier.
	 */
	public Supplier<LivingEntity> getSplLivingEntity() {
		return splEntity;
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
	public Supplier<EffectInstance> getSplEffectInstance() {
		return splEffectInstance;
	}

	/**
	 * Get {@linkplain RayTraceResult} supplier.
	 * 
	 * @return result supplier.
	 */
	public Supplier<RayTraceResult> getSplRayTraceResult() {
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
	public void run(LivingDamageEvent event, LivingEntity entity, EffectInstance effectInstance) {
		this.livingDamageEvent = event;
		this.livingEntity = entity;
		this.effectInstance = effectInstance;
		operator.run();
		reset();
	}

	/**
	 * Execute operator.
	 * 
	 * @param event  input event.
	 * @param entity input entity.
	 */
	public void run(RenderLivingEvent<PlayerEntity, PlayerModel<PlayerEntity>> event, LivingEntity entity) {
		this.renderLivingEvent = event;
		this.livingEntity = entity;
		operator.run();
		reset();
	}

	/**
	 * Execute operator.
	 * 
	 * @param entity input entity
	 * @param result input ray trace result.
	 */
	public void run(LivingEntity entity, RayTraceResult result) {
		this.livingEntity = entity;
		this.rayTraceResult = result;
		operator.run();
		reset();
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
		operator.run();
		reset();
	}

	/**
	 * Execute operator.
	 * 
	 * @param entity input entity
	 */
	public void run(LivingEntity entity) {
		this.livingEntity = entity;
		operator.run();
		reset();
	}

	
}
