package bassebombecraft.potion;

import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootCreeperCannon;
import net.minecraft.potion.Potion;

/**
 * Helper class for static potion instances.
 */
public class MobEffects {
	
	/**
	 * Bacon Bazooka potion, used by {@linkplain ShootBaconBazooka}.
	 */
	public static final Potion BACON_BAZOOKA_POTION = new MobProjectilePotion("BaconBazookaProjectilePotion");

	/**
	 * Creeper cannon potion, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Potion CREEPER_CANNON_POTION = new MobProjectilePotion("CreeperCannonProjectilePotion");

	/**
	 * Creeper cannon potion, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Potion PRIMED_CREEPER_CANNON_POTION = new MobProjectilePotion("PrimedCreeperCannonProjectilePotion");
	
}
