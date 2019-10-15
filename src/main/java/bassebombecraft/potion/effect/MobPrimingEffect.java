package bassebombecraft.potion.effect;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.entity.EntityUtils.explode;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Effect which primes a mob for explosion.
 */
public class MobPrimingEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public final static String NAME = MobPrimingEffect.class.getSimpleName();
	
	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = NAME;

	/**
	 * Explosion will make smoke.
	 */
	static final boolean IS_SMOKING = true;

	/**
	 * Class for doing primed entity count down.
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
	 * Explosion size.
	 */
	final int explosion;

	/**
	 * MobPrimingPotion constructor.
	 */
	public MobPrimingEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);

		Config configuration = getBassebombeCraft().getConfiguration();
		countDown = configuration.getInt(CONFIG_KEY + ".Countdown");
		explosion = configuration.getInt(CONFIG_KEY + ".Explosion");
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// create primed entity
		if (!primed.containsKey(entity)) {
			PrimedEntity value = new PrimedEntity(countDown);
			primed.put(entity, value);			
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
		DamageSource cause = DamageSource.MAGIC;
		entity.onDeath(cause);

		// explode
		World world = entity.getEntityWorld();
		explode(entity, world, explosion);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
