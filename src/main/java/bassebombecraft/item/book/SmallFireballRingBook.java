package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballRingBook;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.ShootFireballRing2;
import net.minecraft.entity.LivingEntity;

/**
 * Book of small fireball ring implementation.
 */
public class SmallFireballRingBook extends GenericRightClickedBook2 {

	public static final String ITEM_NAME = SmallFireballRingBook.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2[]> splOp = () -> {
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		Operator2[] ops = new Operator2[] { new ShootFireballRing2(fnGetInvoker) };
		return ops;
	};

	public SmallFireballRingBook() {
		super(ITEM_NAME, smallFireballRingBook, getInstance(), splOp.get());
	}
}
