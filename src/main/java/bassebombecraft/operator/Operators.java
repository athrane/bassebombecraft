package bassebombecraft.operator;

import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

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
	 * Entity instance.
	 */
	LivingEntity livingEntity;

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
	 * Operator to execute
	 */
	Operator operator;

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
	}

	/**
	 * Run operator.
	 * 
	 * @param event  input event.
	 * @param entity input entity.
	 * @param effect input effect.
	 */
	public void run(LivingDamageEvent event, LivingEntity entity, EffectInstance effect) {
		livingDamageEvent = event;
		livingEntity = entity;
		effectInstance = effect;
		operator.run();
		reset();
	}

}
