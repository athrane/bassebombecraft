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

	static final String[] ADVICE_L0 = { "As item #1, add a Projectile Formation (PF).", };
	static final String[] ADVICE_L1 = { "As item #2, add a Projectile (P) or a",
			"Projectile Formation Modifier (PFM)." };
	static final String[] ADVICE_L2_P = { "As item #3, add a Projectile Path (PP)", "or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L2_PFM = { "As item #3, add another PFM or a",
			"Projectile Path (PP) or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L3_P = { "As item #4, add a Projectile Path (PP)", "or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L3_PP = { "As item #4, add a Projectile Path (PP)", "or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L3_PM = { "As item #4, add a Projectile Modifier (PM)." };
	static final String[] ADVICE_L4_PP = { "As item #5, add a Projectile Path (PP)", "or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L4_PM = { "As item #5, add a Projectile Modifier (PM)." };
	static final String[] ADVICE_L5_PP = { "As item #6, add a Projectile Path (PP)", "or a Projectile Modifier (PM)." };
	static final String[] ADVICE_L5_PM = { "As item #6, add a Projectile Modifier (PM)." };
	static final String[] ADVICE_L6 = { "Impressive. A 6 item combo.."};
	static final String[] ADVICE_DEFAULT = { "The legal syntax is:", "Sequence = PF [PFM] P {PP} {PM}" };

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
		case 5:
			return adviceOnLength5(inventory);
		case 6:
			return adviceOnLength6(inventory);

		default:
			return defaultAdvice();
		}
	}

	String[] adviceOnLength0() {
		return ADVICE_L0;
	}

	String[] adviceOnLength1(CompositeMagicItemItemStackHandler inventory) {
		return ADVICE_L1;
	}

	String[] adviceOnLength2(CompositeMagicItemItemStackHandler inventory) {
		// item #2 is PFM
		if (validator.isSecondItemProjectileFormationModifier(inventory))
			return ADVICE_L2_PFM;

		// item #2 is P
		return ADVICE_L2_P;
	}

	String[] adviceOnLength3(CompositeMagicItemItemStackHandler inventory) {
		// item #2,3 is PFM P
		if (validator.isSecondItemProjectileFormationModifier(inventory))
			return ADVICE_L3_P;

		// item #2,3 is P PP
		if (validator.isThirdItemProjectilePath(inventory))
			return ADVICE_L3_PP;

		// item #2,3 is P PM
		return ADVICE_L3_PM;
	}

	String[] adviceOnLength4(CompositeMagicItemItemStackHandler inventory) {
		// item #4 is PP
		if (validator.isFourthItemProjectilePath(inventory))
			return ADVICE_L4_PP;

		// item #4 is PM
		return ADVICE_L4_PM;
	}

	String[] adviceOnLength5(CompositeMagicItemItemStackHandler inventory) {
		// item #5 is PP
		if (validator.isFifthItemProjectilePath(inventory))
			return ADVICE_L5_PP;

		// item #5 is PM
		return ADVICE_L5_PM;
	}

	String[] adviceOnLength6(CompositeMagicItemItemStackHandler inventory) {
		return ADVICE_L6;
	}

	String[] defaultAdvice() {
		return ADVICE_DEFAULT;
	}

}
