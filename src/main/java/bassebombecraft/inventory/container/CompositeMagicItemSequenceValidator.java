package bassebombecraft.inventory.container;

/**
 * Interface for calculation of legal sequence of composite magic items in the
 * {@linkplain CompositeMagicItemItemStackHandler}.
 */
public interface CompositeMagicItemSequenceValidator {

	/**
	 * Resolve legal sequence length.
	 * 
	 * The length of the legal sequence can be shorter than the actual sequence length.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return legal sequence length
	 */
	public int resolveLegalSequenceLength(CompositeMagicItemItemStackHandler inventory);
	
	/**
	 * Returns true if first item is a projectile formation.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if first item is a projectile formation.
	 */
	boolean isFirstItemProjectileFormation(CompositeMagicItemItemStackHandler inventory);

	/**
	 * Returns true if second item is a projectile formation modifier.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if second item is a projectile formation modifier.
	 */
	boolean isSecondItemProjectileFormationModifier(CompositeMagicItemItemStackHandler inventory);

	/**
	 * Returns true if second item is a projectile.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if second item is a projectile.
	 */
	boolean isSecondItemProjectile(CompositeMagicItemItemStackHandler inventory);

	/**
	 * Returns true if third item is a projectile.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if third item is a projectile.
	 */
	boolean isThirdItemProjectile(CompositeMagicItemItemStackHandler inventory);
	
	/**
	 * Returns true if third item is a projectile formation modifier.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if third item is a projectile formation modifier.
	 */
	boolean isThirdItemProjectileFormationModifier(CompositeMagicItemItemStackHandler inventory);

	/**
	 * Returns true if third item is a projectile path.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if third item is a projectile path.
	 */
	boolean isThirdItemProjectilePath(CompositeMagicItemItemStackHandler inventory);
	
	/**
	 * Returns true if third item is a projectile modifier.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if third item is a projectile modifier.
	 */
	boolean isThirdItemProjectileModifier(CompositeMagicItemItemStackHandler inventory);

	/**
	 * Returns true if fourth item is a projectile path.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if fourth item is a projectile path.
	 */
	boolean isFourthItemProjectilePath(CompositeMagicItemItemStackHandler inventory);
	
	/**
	 * Returns true if fourth item is a projectile modifier.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if fourth item is a projectile modifier.
	 */
	boolean isFourthItemProjectileModifier(CompositeMagicItemItemStackHandler inventory);

	/**
	 * Returns true if fifth item is a projectile path.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if fifth item is a projectile path.
	 */
	boolean isFifthItemProjectilePath(CompositeMagicItemItemStackHandler inventory);
	
	/**
	 * Returns true if fifth item is a projectile modifier.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if fifth item is a projectile modifier.
	 */
	boolean isFifthItemProjectileModifier(CompositeMagicItemItemStackHandler inventory);

	/**
	 * Returns true if sixth item is a projectile path.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if sixth item is a projectile path.
	 */
	boolean isSixthItemProjectilePath(CompositeMagicItemItemStackHandler inventory);
	
	/**
	 * Returns true if sixth item is a projectile modifier.
	 * 
	 * @param inventory composite item inventory.
	 * 
	 * @return true if sixth item is a projectile modifier.
	 */
	boolean isSixthItemProjectileModifier(CompositeMagicItemItemStackHandler inventory);
	
}
