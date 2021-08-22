package bassebombecraft.potion.effect;

import static bassebombecraft.config.ModConfiguration.bearBlasterProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.bearBlasterProjectileEffectForce;

/**
 * Effect which shoots a mob along its view vector.
 */
public class BearBlasterEffect extends MobProjectileEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = BearBlasterEffect.class.getSimpleName();

	/**
	 * No-arg constructor.
	 */
	public BearBlasterEffect() {
		super(() -> bearBlasterProjectileEffectForce.get(), () -> bearBlasterProjectileEffectExplosion.get());
	}

}
