package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.warPigsIdolInventoryItem;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperatorOnTarget2;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.conditional.IsEntityOfType2;
import bassebombecraft.operator.conditional.IsNot2;
import bassebombecraft.operator.entity.SpawnWarPig2;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;

/**
 * War pigs idol implementation.
 */
public class WarPigsIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = WarPigsIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2[]> splOp = () -> {
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		Function<Ports, LivingEntity> fnGetTarget = getFnGetLivingEntity2();
		Operator2[] ops = new Operator2[] { new IsNot2(new IsEntityOfType2(fnGetTarget, PigEntity.class)),
				new SpawnWarPig2(fnGetInvoker, fnGetTarget) };
		return ops;
	};

	public WarPigsIdolInventoryItem() {
		super(ITEM_NAME, warPigsIdolInventoryItem, new ExecuteOperatorOnTarget2(getInstance(), splOp.get()));
	}
}
