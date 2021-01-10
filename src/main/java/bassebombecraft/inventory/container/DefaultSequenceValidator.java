package bassebombecraft.inventory.container;

import net.minecraft.item.ItemStack;

/**
 * Implementation of the {@linkplain CompositeMagicItemSequenceValidator}.
 */
public class DefaultSequenceValidator implements CompositeMagicItemSequenceValidator {

	@Override
	public int resolveLegalSequenceLength(CompositeMagicItemItemStackHandler inventory) {
		
		// exit if index is undefined (-1)
		int index = inventory.getCompositeInventoryIndex();
		if(index == -1) return 0;			
		
		if(!isPos1LegalSequence(inventory)) return 0;
		if(!isPos2LegalSequence(inventory)) return 1;			
		if(!isPos3LegalSequence(inventory)) return 2;						
		if(!isPos4LegalSequence(inventory)) return 3;						
		if(!isPos5LegalSequence(inventory)) return 4;						
		if(!isPos6LegalSequence(inventory)) return 5;						
		return 6;
	}	
	
	@Override	
	public boolean isFirstItemProjectileFormation(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 0, "ProjectileFormationItem");
	}

	@Override	
	public boolean isSecondItemProjectileFormationModifier(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 1, "ProjectileFormationModifierItem");
	}

	@Override	
	public boolean isSecondItemProjectile(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 1, "ProjectileItem");
	}

	@Override	
	public boolean isThirdItemProjectile(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 2, "ProjectileItem");
	}

	@Override	
	public boolean isThirdItemProjectileFormationModifier(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 2, "ProjectileFormationModifierItem");
	}

	@Override	
	public boolean isThirdItemProjectilePath(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 2, "ProjectilePathItem");
	}

	@Override	
	public boolean isThirdItemProjectileModifier(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 2, "ProjectileModifierItem");
	}

	@Override	
	public boolean isFourthItemProjectilePath(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 3, "ProjectilePathItem");
	}

	@Override	
	public boolean isFourthItemProjectileModifier(CompositeMagicItemItemStackHandler inventory) {
		return isItemAt(inventory, 3, "ProjectileModifierItem");
	}
	
	/**
	 * Returns true if item matches name suffix.
	 * 
	 * @param inventory composite item inventory.
	 * @param offset    item number in sequence.
	 * @param suffix    name suffix.
	 * 
	 * @return true if second item matches name suffix.
	 */
	boolean isItemAt(CompositeMagicItemItemStackHandler inventory, int offset, String suffix) {

		// get item in inventory
		int index = offset + inventory.getCompositeInventoryIndex();
		ItemStack itemStack = inventory.getStackInSlot(index);
		String name = itemStack.getItem().getClass().getSimpleName();

		// determine if item name matches suffix
		boolean isProjectileFormation = name.endsWith(suffix);
		return isProjectileFormation;
	}

	boolean isPos1LegalSequence(CompositeMagicItemItemStackHandler inventory) {
		return isFirstItemProjectileFormation(inventory);
	}

	boolean isPos2LegalSequence(CompositeMagicItemItemStackHandler inventory) {
		boolean itemIsP = isSecondItemProjectile(inventory);
		boolean itemIsPFM = isSecondItemProjectileFormationModifier(inventory);
		if(itemIsP) return true;
		if(itemIsPFM) return true;
		return false;
	}

	boolean isPos3LegalSequence(CompositeMagicItemItemStackHandler inventory) {
		// item #2 is PFM
		if(isSecondItemProjectileFormationModifier(inventory)) {
			boolean itemIsP = isThirdItemProjectile(inventory);
			if(itemIsP) return true;
			return false;
		}
		
		// item #2 is P
		boolean itemIsPP = isThirdItemProjectilePath(inventory);				
		boolean itemIsPM = isThirdItemProjectileModifier(inventory);		
		if(itemIsPP) return true;		
		if(itemIsPM) return true;		
		return false;
	}

	boolean isPos4LegalSequence(CompositeMagicItemItemStackHandler inventory) {
		// item #2,3 is PFM P
		if(isSecondItemProjectileFormationModifier(inventory)) {
			boolean itemIsPP = isFourthItemProjectilePath(inventory);				
			boolean itemIsPM = isFourthItemProjectileModifier(inventory);		
			if(itemIsPP) return true;		
			if(itemIsPM) return true;		
			return false;
		}
		
		// item #2,3 is P PP
		if(isThirdItemProjectilePath(inventory)) {
			boolean itemIsPP = isFourthItemProjectilePath(inventory);				
			boolean itemIsPM = isFourthItemProjectileModifier(inventory);		
			if(itemIsPP) return true;		
			if(itemIsPM) return true;		
			return false;
		}
		
		// item #2,3 is P PM
		boolean itemIsPM = isFourthItemProjectileModifier(inventory);		
		if(itemIsPM) return true;		
		return false;		
	}

	boolean isPos5LegalSequence(CompositeMagicItemItemStackHandler inventory) {
		return false;
	}

	boolean isPos6LegalSequence(CompositeMagicItemItemStackHandler inventory) {
		return false;
	}	
	
}
