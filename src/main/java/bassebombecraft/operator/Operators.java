package bassebombecraft.operator;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
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
	 * Effect instance.
	 */
	EffectInstance effectInstance;

	/**
	 * Living entity instance.
	 */
	LivingEntity livingEntity;

	/**
	 * Target entity instance.
	 */
	Entity targetEntity;
	
	/**
	 * {@linkplain LivingDamageEvent} supplier.
	 */
	Supplier<LivingDamageEvent> splEvent = () -> livingDamageEvent;

	/**
	 * {@linkplain EffectInstance} supplier.
	 */
	Supplier<EffectInstance> splEffect = () -> effectInstance;

	/**
	 * {@linkplain LivingEntity} supplier.
	 */
	Supplier<LivingEntity> splEntity = () -> livingEntity;

	/**
	 * {@linkplain Entity} supplier.
	 */
	Supplier<Entity> splTargetEntity= () -> targetEntity;
	
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
	 * Get {@linkplain EffectInstance} supplier.
	 * 
	 * @return effect supplier.
	 */
	public Supplier<EffectInstance> getSplEffectInstance() {
		return splEffect;
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
	public Supplier<Entity> getSplTargetEntity() {
		return splTargetEntity;
	}
	
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Reset all fields.
	 */
	void reset() {
		livingDamageEvent = null;
		livingEntity = null;
		effectInstance = null;
		targetEntity = null;
	}

	/**
	 * Execute operator.
	 * 
	 * @param event  input event.
	 * @param entity input entity.
	 * @param effect input effect.
	 */
	public void run(LivingDamageEvent event, LivingEntity entity, EffectInstance effect) {
		this.livingDamageEvent = event;
		this.livingEntity = entity;
		this.effectInstance = effect;
		operator.run();
		reset();
	}

	/**
	 * Execute operator.
	 * 
	 * @param entity input entity
	 * @param target input target entity.
	 */
	public void run(LivingEntity entity, Entity target) {
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
