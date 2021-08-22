package bassebombecraft.potion.effect;

import static bassebombecraft.config.ModConfiguration.creeperCannonProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.creeperCannonProjectileEffectForce;

/**
 * Effect which shoots a mob along its view vector.
 */
public class CreeperCannonEffect extends MobProjectileEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = CreeperCannonEffect.class.getSimpleName();

	/**
	 * No-arg constructor.
	 */
	public CreeperCannonEffect() {
		super(() -> creeperCannonProjectileEffectForce.get(), () -> creeperCannonProjectileEffectExplosion.get());
	}

}
