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
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.conditional.IsEntityOfType2;
import bassebombecraft.operator.conditional.IsNot2;
import bassebombecraft.operator.entity.SpawnWarPig2;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;

/**
 * War pigs idol implementation.
 */
public class WarPigsIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = WarPigsIdolInventoryItem.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Function<Ports, LivingEntity> fnGetInvoker = getFnGetLivingEntity1();
		Function<Ports, LivingEntity> fnGetTarget = getFnGetLivingEntity2();		
		return new Sequence2( new IsNot2(new IsEntityOfType2(fnGetTarget, Pig.class)),
				new SpawnWarPig2(fnGetInvoker, fnGetTarget) );
	};

	public WarPigsIdolInventoryItem() {
		super(warPigsIdolInventoryItem, new ExecuteOperatorOnTarget2(getInstance(), splOp.get()));
	}
}
