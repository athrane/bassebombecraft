package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.config.ModConfiguration.mobPrimingEffectCountdown;
import static bassebombecraft.entity.EntityUtils.explode;
import static bassebombecraft.entity.EntityUtils.isTypeCreeperEntity;
import static bassebombecraft.entity.EntityUtils.killEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

/**
 * Effect which primes a mob for explosion.
 * 
 * Final explosion effect is defined by amplifier.
 * 
 * The effect has no effect on the player.
 */
public class MobPrimingEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = MobPrimingEffect.class.getSimpleName();

	/**
	 * Explosion will make smoke.
	 */
	static final boolean IS_SMOKING = true;

	/**
	 * Class for doing primed entity count down.
	 * 
	 * Final explosion effect is defined by amplifier.
	 */
	class PrimedEntity {
		public PrimedEntity(int countDown) {
			this.countdown = countDown;
		}

		int countdown;
	}

	/**
	 * Primed entities.
	 */
	Map<LivingEntity, PrimedEntity> primed = new HashMap<LivingEntity, PrimedEntity>();

	/**
	 * MobPrimingEffect constructor.
	 */
	public MobPrimingEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// create primed entity
		int countDown = mobPrimingEffectCountdown.get();
		if (!primed.containsKey(entity)) {
			PrimedEntity value = new PrimedEntity(countDown);
			primed.put(entity, value);

			// if creeper then set primed
			if (isTypeCreeperEntity(entity)) {
				Creeper creeper = (Creeper) entity;
				creeper.ignite();
			}

			// set glowing effect
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, countDown, 0));

			return;
		}

		// remove if primed entity is dead
		if (!entity.isAlive()) {

			// remove from map
			primed.remove(entity);
			return;
		}

		// count down
		PrimedEntity value = primed.get(entity);
		value.countdown--;

		// explode if count down is done
		if (value.countdown > 0)
			return;

		// remove from map
		primed.remove(entity);

		// kill mob
		killEntity(entity);

		// explode
		Level world = entity.getCommandSenderWorld();
		explode(entity, world, amplifier);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

}
