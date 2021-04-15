package bassebombecraft.potion.effect;

import static bassebombecraft.config.ModConfiguration.baconBazookaProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.baconBazookaProjectileEffectForce;

/**
 * Effect which shoots a mob along its view vector.
 */
public class BaconBazookaEffect extends MobProjectileEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = BaconBazookaEffect.class.getSimpleName();

	/**
	 * No-arg constructor.
	 */
	public BaconBazookaEffect() {
		super(() -> baconBazookaProjectileEffectForce.get(), () -> baconBazookaProjectileEffectExplosion.get());
	}

}
