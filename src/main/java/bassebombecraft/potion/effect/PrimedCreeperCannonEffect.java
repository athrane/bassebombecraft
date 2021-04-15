package bassebombecraft.potion.effect;

import static bassebombecraft.config.ModConfiguration.primedCreeperCannonProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.primedCreeperCannonProjectileEffectForce;

/**
 * Effect which shoots a mob along its view vector.
 */
public class PrimedCreeperCannonEffect extends MobProjectileEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = PrimedCreeperCannonEffect.class.getSimpleName();

	/**
	 * No-arg constructor.
	 */
	public PrimedCreeperCannonEffect() {
		super(() -> primedCreeperCannonProjectileEffectForce.get(),
				() -> primedCreeperCannonProjectileEffectExplosion.get());
	}

}
