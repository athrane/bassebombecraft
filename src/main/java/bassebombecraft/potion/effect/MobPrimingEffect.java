package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.config.ModConfiguration.mobPrimingEffectCountdown;
import static bassebombecraft.entity.EntityUtils.explode;
import static bassebombecraft.entity.EntityUtils.isTypeCreeperEntity;
import static bassebombecraft.entity.EntityUtils.killEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.potion.Effect;
import net.minecraft.world.World;

/**
 * Effect which primes a mob for explosion.
 * 
 * Final explosion effect is defined by amplifier.
 * 
 * The effect has no effect on the player.
 */
public class MobPrimingEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public final static String NAME = MobPrimingEffect.class.getSimpleName();

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
	 * Priming count down
	 */
	final int countDown;

	/**
	 * MobPrimingEffect constructor.
	 */
	public MobPrimingEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
		countDown = mobPrimingEffectCountdown.get();
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// create primed entity
		if (!primed.containsKey(entity)) {
			PrimedEntity value = new PrimedEntity(countDown);
			primed.put(entity, value);

			// if creeper then set primed
			if (isTypeCreeperEntity(entity)) {
				CreeperEntity creeper = (CreeperEntity) entity;
				creeper.ignite();
			}

			// set glowing
			entity.setGlowing(true);

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
		World world = entity.getEntityWorld();
		explode(entity, world, amplifier);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
