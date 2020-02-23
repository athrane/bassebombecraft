package bassebombecraft.operator.event;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

/**
 * Implementation of the {@linkplain Operator} interface which reflects "mob"
 * damage received.
 * 
 * Damage calculation is affected by amplifier.
 */
public class ReflectMobDamageAmplified implements Operator {

	/**
	 * Event supplier.
	 */
	Supplier<LivingDamageEvent> splEvent;

	/**
	 * Effect supplier
	 */
	Supplier<EffectInstance> splEffect;
	
	/**
	 * ReflectMobDamageAmplfied constructor.
	 * 
	 * @param splEvent event supplier.
	 * @param splEffect effect supplier.
	 */
	public ReflectMobDamageAmplified(Supplier<LivingDamageEvent> splEvent, Supplier<EffectInstance> splEffect) {
		this.splEvent = splEvent;
		this.splEffect = splEffect;		
	}

	@Override
	public void run() {

		// get event
		LivingDamageEvent event = splEvent.get();

		// get source
		DamageSource source = event.getSource();

		// exit if damage isn't mob damage
		if (!source.getDamageType().equalsIgnoreCase("mob"))
			return;

		// get amplifier 
		int amplifier = splEffect.get().getAmplifier();
		
		// apply damage to source
		Entity srcEntity = source.getTrueSource();				
		float amount = event.getAmount() * amplifier;
		DamageSource newSource = DamageSource.causeMobDamage((LivingEntity) srcEntity);
		srcEntity.attackEntityFrom(newSource, amount);
	}

	/**
	 * Factory method.
	 * 
	 * @param splEvent event supplier.
	 * @param splEffect effect supplier.
	 * 
	 * @return operator instance.
	 */
	public static Operator getInstance(Supplier<LivingDamageEvent> splEvent, Supplier<EffectInstance> splEffect) {
		return new ReflectMobDamageAmplified(splEvent, splEffect);
	}
}
