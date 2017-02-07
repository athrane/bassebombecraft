package bassebombecraft.potion;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.entity.EntityUtils.explode;

import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

/**
 * Potion which primes a mob for explosion.
 */
public class MobPrimingPotion extends Potion {

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = MobPrimingPotion.class.getSimpleName();

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
	Map<EntityLivingBase, PrimedEntity> primed = new HashMap<EntityLivingBase, PrimedEntity>();

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
	public MobPrimingPotion() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);

		Config configuration = getBassebombeCraft().getConfiguration();
		countDown = configuration.getInt(CONFIG_KEY + ".Countdown");
		explosion = configuration.getInt(CONFIG_KEY + ".Explosion");
	}

	@Override
	public void performEffect(EntityLivingBase entity, int magicNumber) {

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
		entity.setDead();
			
		// explode
		World world = entity.getEntityWorld();
		explode(entity, world, explosion);		
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
