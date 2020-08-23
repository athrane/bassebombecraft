package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.llamaProjectileItem;

import bassebombecraft.item.composite.GenericCompositeNullItem;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootLlamaProjectile2;

/**
 * Llama projectile item.
 */
public class LlamaProjectileItem extends GenericCompositeNullItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = LlamaProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public LlamaProjectileItem() {
		super(NAME, llamaProjectileItem);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootLlamaProjectile2();
	}

}
