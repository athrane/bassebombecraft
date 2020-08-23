package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.RECEIVE_AGGRO_EFFECT;
import static bassebombecraft.config.ModConfiguration.decoyBook;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectDuration;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import bassebombecraft.operator.entity.raytraceresult.SpawnDecoy2;
import bassebombecraft.operator.projectile.ShootOperatorEggProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

/**
 * Book of decoy implementation.
 */
public class DecoyBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = DecoyBook.class.getSimpleName();

	/**
	 * Create operators.
	 * 
	 * The reason for not using the no-arg constructor for {@linkplain AddEffect2}
	 * is that it by default gets the target entity as living entity #1 from the
	 * ports.
	 * 
	 * But {@linkplain SpawnDecoy2} sets the created entity as living entity #2 in
	 * the ports.
	 * 
	 * In order for {@linkplain AddEffect2} to pick up the target from living entity
	 * #2 then its constructor is invoked with adapted functions.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, LivingEntity> fnGetTarget = getFnGetLivingEntity2();
		BiConsumer<Ports, EffectInstance> bcSetEffectInstance = getBcSetEffectInstance1();
		Operator2 projectileLogicOp = new Sequence2(new SpawnDecoy2(), new AddEffect2(fnGetTarget, bcSetEffectInstance,
				RECEIVE_AGGRO_EFFECT, receiveAggroEffectDuration.get(), receiveAggroEffectAmplifier.get()));
		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootOperatorEggProjectile2(projectileLogicOp);
		return new Sequence2(formationOp, projectileOp);
	};

	public DecoyBook() {
		super(ITEM_NAME, decoyBook, getInstance(), splOp.get());
	}

}
