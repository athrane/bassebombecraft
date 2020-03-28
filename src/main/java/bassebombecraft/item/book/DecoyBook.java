package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.RECEIVE_AGGRO_EFFECT;
import static bassebombecraft.config.ModConfiguration.decoyBook;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectDuration;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.item.action.ShootOperatorEggProjectile;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.Sequence;
import bassebombecraft.operator.entity.SpawnDecoy;
import bassebombecraft.operator.entity.potion.effect.AddEffect;

/**
 * Book of decoy implementation.
 */
public class DecoyBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = DecoyBook.class.getSimpleName();

	static IntSupplier splDuration = () -> receiveAggroEffectDuration.get();
	static IntSupplier splAmplifier = () -> receiveAggroEffectAmplifier.get();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		SpawnDecoy spawnOp = new SpawnDecoy(ops.getSplLivingEntity(), ops.getSplRayTraceResult());
		AddEffect addOp2 = new AddEffect(spawnOp.getSplLivingEntity(), RECEIVE_AGGRO_EFFECT, splDuration, splAmplifier);
		Sequence seqOp = new Sequence(spawnOp, addOp2);
		ops.setOperator(seqOp);
		return ops;
	};

	public DecoyBook() {
		super(ITEM_NAME, decoyBook, new ShootOperatorEggProjectile(splOp.get()));
	}

}
