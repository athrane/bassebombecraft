package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.RECEIVE_AGGRO_EFFECT;
import static bassebombecraft.config.ModConfiguration.receiveAggroBook;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectDuration;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.item.action.ShootOperatorEggProjectile;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEntityWasHit;
import bassebombecraft.operator.entity.potion.effect.AddEffect;

/**
 * Book of receive aggro implementation.
 */
public class ReceiveAggroBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = ReceiveAggroBook.class.getSimpleName();

	static IntSupplier splDuration = () -> receiveAggroEffectDuration.get();
	static IntSupplier splAmplifier = () -> receiveAggroEffectAmplifier.get();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		AddEffect addOp = new AddEffect(ops.getSplRaytracedEntity(), RECEIVE_AGGRO_EFFECT, splDuration, splAmplifier);
		IfEntityWasHit ifOp = new IfEntityWasHit(ops.getSplRayTraceResult(), addOp);
		ops.setOperator(ifOp);
		return ops;
	};

	public ReceiveAggroBook() {
		super(ITEM_NAME, receiveAggroBook, new ShootOperatorEggProjectile(splOp.get()));
	}

}
