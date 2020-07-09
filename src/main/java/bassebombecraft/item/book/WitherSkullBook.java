package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.witherSkullBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.ShootWitherSkull2;
import net.minecraft.entity.LivingEntity;

/**
 * Book of wither skull implementation.
 */
public class WitherSkullBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = WitherSkullBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2[]> splOp = () -> {
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		Operator2[] ops = new Operator2[] { new ShootWitherSkull2(fnGetInvoker) };
		return ops;
	};

	public WitherSkullBook() {
		super(ITEM_NAME, witherSkullBook, getInstance(), splOp.get());
	}
}
