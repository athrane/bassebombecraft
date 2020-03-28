package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.*;
import static bassebombecraft.config.ModConfiguration.decoyBook;
import static bassebombecraft.config.ModConfiguration.*;
import static bassebombecraft.config.ModConfiguration.decoyEffectDuration;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.item.action.ShootOperatorEggProjectile;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.Sequence;
import bassebombecraft.operator.entity.SpawnDecoy;
import bassebombecraft.operator.entity.potion.effect.AddEffect;
import bassebombecraft.operator.entity.potion.effect.AddEffectAtClient;

/**
 * Book of decoy implementation.
 */
public class DecoyBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = DecoyBook.class.getSimpleName();

	static IntSupplier splDuration = () -> decoyEffectDuration.get();
	static IntSupplier splAmplifier = () -> decoyEffectAmplifier.get();
	static IntSupplier splDuration2 = () -> receiveAggroEffectDuration.get();
	static IntSupplier splAmplifier2 = () -> receiveAggroEffectAmplifier.get();

	static Supplier<Operators> splOp = () -> {
		Operators ops = new Operators();
		SpawnDecoy spawnOp = new SpawnDecoy(ops.getSplLivingEntity(), ops.getSplRayTraceResult());
		AddEffect addOp = new AddEffect(spawnOp.getSplLivingEntity(), DECOY_EFFECT, splDuration, splAmplifier);
		AddEffect addOp2 = new AddEffect(spawnOp.getSplLivingEntity(), RECEIVE_AGGRO_EFFECT, splDuration, splAmplifier);		
		AddEffectAtClient addOp3 = new AddEffectAtClient(spawnOp.getSplLivingEntity(), addOp.getSplEffectInstance());
		Sequence seqOp = new Sequence(spawnOp, addOp, addOp2, addOp3);
		ops.setOperator(seqOp);
		return ops;
	};

	public DecoyBook() {
		super(ITEM_NAME, decoyBook, new ShootOperatorEggProjectile(splOp.get()));
	}

}
