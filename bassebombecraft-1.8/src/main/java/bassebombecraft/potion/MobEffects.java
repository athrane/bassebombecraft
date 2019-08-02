package bassebombecraft.potion;

import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootBearBlaster;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.inventory.MobsAggroIdolInventoryItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import net.minecraft.potion.Effect;

/**
 * Helper class for static Effect instances.
 */
public class MobEffects {

	/**
	 * Bacon Bazooka effect, used by {@linkplain ShootBaconBazooka}.
	 */
	public static final Effect BACON_BAZOOKA_POTION = new MobProjectileEffect("BaconBazookaProjectileEffect");

	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect CREEPER_CANNON_POTION = new MobProjectileEffect("CreeperCannonProjectileEffect");

	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect PRIMED_CREEPER_CANNON_POTION = new MobProjectileEffect(
			"PrimedCreeperCannonProjectilePotion");

	/**
	 * Bear blaster effect, used by {@linkplain ShootBearBlaster}.
	 */
	public static final Effect BEAR_BLASTER_POTION = new MobProjectileEffect("BearBlasterProjectileEffect");

	/**
	 * Primed mob effect, used by {@linkplain PrimeMobIdolInventoryItem}.
	 */
	public static final Effect PRIMED_MOB_POTION = new MobPrimingEffect();

	/**
	 * Mobs aggro effect, used by {@linkplain MobsAggroIdolInventoryItem}.
	 */
	public static final Effect MOBS_AGGRO_POTION = new MobsAggroEffect();

}
