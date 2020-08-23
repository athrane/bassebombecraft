package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.RECEIVE_AGGRO_EFFECT;
import static bassebombecraft.config.ModConfiguration.receiveAggroBook;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectAmplifier;
import static bassebombecraft.config.ModConfiguration.receiveAggroEffectDuration;
import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsLivingEntityHitInRaytraceResult2;
import bassebombecraft.operator.entity.potion.effect.AddEffect2;
import bassebombecraft.operator.projectile.ShootOperatorEggProjectile2;
import bassebombecraft.operator.projectile.formation.SingleProjectileFormation2;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

/**
 * Book of receive aggro implementation.
 */
public class ReceiveAggroBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = ReceiveAggroBook.class.getSimpleName();

	/**
	 * Create operators.
	 * 
	 * The reason for not using the no-arg constructor for {@linkplain AddEffect2}
	 * is that it by default gets the target entity as living entity #1 from the
	 * ports.
	 * 
	 * But the event handler currently only sets the invoker (in the living entity
	 * #1 port) and the ray race result (in the ray trace result #1 port) from the
	 * event.
	 * 
	 * In order for {@linkplain AddEffect2} to resolve the target entity from the
	 * ray trace result then its constructor is invoked with adapted functions.
	 */
	static Supplier<Operator2> splOp = () -> {

		Function<Ports, LivingEntity> fnGetTarget = p -> {
			RayTraceResult result = p.getRayTraceResult1();
			EntityRayTraceResult entityResult = (EntityRayTraceResult) result;
			return (LivingEntity) entityResult.getEntity();
		};
		BiConsumer<Ports, EffectInstance> bcSetEffectInstance = getBcSetEffectInstance1();

		Operator2 projectileLogicOp = new Sequence2(new IsLivingEntityHitInRaytraceResult2(),
				new AddEffect2(fnGetTarget, bcSetEffectInstance, RECEIVE_AGGRO_EFFECT, receiveAggroEffectDuration.get(),
						receiveAggroEffectAmplifier.get()));

		Operator2 formationOp = new SingleProjectileFormation2();
		Operator2 projectileOp = new ShootOperatorEggProjectile2(projectileLogicOp);
		return new Sequence2(formationOp, projectileOp);
	};

	public ReceiveAggroBook() {
		super(ITEM_NAME, receiveAggroBook, getInstance(), splOp.get());
	}

}
