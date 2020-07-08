package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.ShootFireball2;
import net.minecraft.entity.LivingEntity;

/**
 * Book of small fireball implementation.
 */
public class SmallFireballBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = SmallFireballBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2[]> splOp = () -> {
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		Operator2[] ops = new Operator2[] { new ShootFireball2(fnGetInvoker) };
		return ops;
	};

	public SmallFireballBook() {
		super(ITEM_NAME, smallFireballBook, getInstance(), splOp.get());
	}
}
