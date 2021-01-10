package bassebombecraft.client.screen;

import bassebombecraft.inventory.container.CompositeMagicItemContainer;
import bassebombecraft.inventory.container.CompositeMagicItemItemStackHandler;
import bassebombecraft.inventory.container.CompositeMagicItemSequenceValidator;
import bassebombecraft.inventory.container.DefaultSequenceValidator;
import bassebombecraft.item.composite.CompositeMagicItem;

/**
 * Implementation of the {@linkplain ItemAdviceGenerator} for generating tips
 * for using the {@linkplain CompositeMagicItem}.
 */
public class DefaultAdviceGenerator implements ItemAdviceGenerator {

	static final String[] ADVICE_L0 = new String[] { "As item #1, add a Projectile Formation (PF).", };
	static final String[] ADVICE_L1 = new String[] { "As item #2, add a Projectile (P) or a",
			"Projectile Formation Modifier (PFM)." };
	static final String[] ADVICE_L2_P = new String[] { "As item #3, add a Projectile Path (PP)",
			"or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L2_PFM = new String[] { "As item #3, add another PFM or a Projectile Path (PP)",
			"or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L3 = new String[] { "As item #4, ", "L3" };
	static final String[] ADVICE_L4 = new String[] { "L4", "L4" };
	static final String[] ADVICE_LX_PF = new String[] { "Item #1 must be a Projectile Formation (PF)." };

	/**
	 * Item container.
	 */
	CompositeMagicItemContainer container;

	/**
	 * Item sequence validator.
	 */
	CompositeMagicItemSequenceValidator validator;

	/**
	 * Constructor.
	 * 
	 * @param container item inventory.
	 */
	public DefaultAdviceGenerator(CompositeMagicItemContainer container) {
		this.container = container;
		validator = new DefaultSequenceValidator();
	}

	@Override
	public String[] generate() {

		// get composite item inventory information
		CompositeMagicItemItemStackHandler inventory = container.getCompositeItemInventory();
		int legalSequenceLength = validator.resolveLegalSequenceLength(inventory);

		switch (legalSequenceLength) {
		case 0:
			return adviceOnLength0();
		case 1:
			return adviceOnLength1(inventory);
		case 2:
			return adviceOnLength2(inventory);
		case 3:
			return adviceOnLength3(inventory);
		case 4:
			return adviceOnLength4(inventory);

		default:
			return defaultAdvice();
		}
	}

	String[] adviceOnLength0() {
		return ADVICE_L0;
	}

	String[] adviceOnLength1(CompositeMagicItemItemStackHandler inventory) {

		// add advice for item #1 != PF
		if (!validator.isFirstItemProjectileFormation(inventory))
			return ADVICE_LX_PF;

		return ADVICE_L1;
	}

	String[] adviceOnLength2(CompositeMagicItemItemStackHandler inventory) {

		// add advice for item #1 != PF
		if (!validator.isFirstItemProjectileFormation(inventory))
			return ADVICE_LX_PF;

		// add advice for item #2 == P
		if (validator.isSecondItemProjectile(inventory))
			return ADVICE_L2_P;

		// add advice for item #2 == PFM
		if (validator.isSecondItemProjectileFormationModifier(inventory))
			return ADVICE_L2_PFM;

		return ADVICE_L1;
	}

	String[] adviceOnLength3(CompositeMagicItemItemStackHandler inventory) {

		// add advice for item #1 != PF
		if (!validator.isFirstItemProjectileFormation(inventory))
			return ADVICE_LX_PF;

		return new String[] { "S=3", "S=3" };
	}

	String[] adviceOnLength4(CompositeMagicItemItemStackHandler inventory) {

		// add advice for item #1 != PF
		if (!validator.isFirstItemProjectileFormation(inventory))
			return ADVICE_LX_PF;

		return new String[] { "S=4", "S=4" };
	}

	String[] defaultAdvice() {
		return new String[] { "The legal syntax is:", "Sequence = PF [PFM] P {PP} {PM}" };
	}

}
