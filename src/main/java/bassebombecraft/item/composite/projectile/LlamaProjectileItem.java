package bassebombecraft.item.composite.projectile;

import static bassebombecraft.config.ModConfiguration.llamaProjectileEntity;
import static bassebombecraft.config.ModConfiguration.llamaProjectileItem;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.projectile.ShootLlamaProjectile2;

/**
 * Llama projectile item.
 */
public class LlamaProjectileItem extends GenericCompositeNullProjectileItem {

	/**
	 * Item identifier.
	 */
	public static final String NAME = LlamaProjectileItem.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public LlamaProjectileItem() {
		super(llamaProjectileItem, llamaProjectileEntity);
	}

	@Override
	public Operator2 createOperator() {
		return new ShootLlamaProjectile2();
	}

}
