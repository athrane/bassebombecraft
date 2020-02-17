package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.killerBeesIdolInventoryItem;
import static bassebombecraft.config.ModConfiguration.spawnKillerBeesDamage;
import static bassebombecraft.config.ModConfiguration.spawnKillerBeesMovementSpeed;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.ExecuteOperator;
import bassebombecraft.operator.Operator;
import bassebombecraft.operator.Operators;
import bassebombecraft.operator.conditional.IfEntityIsntType;
import bassebombecraft.operator.entity.SpawnKillerBee;
import net.minecraft.entity.passive.BeeEntity;

/**
 * Killer bees idol implementation.
 */
public class KillerBeesIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = KillerBeesIdolInventoryItem.class.getSimpleName();

	static Supplier<Integer> splDamage = () -> spawnKillerBeesDamage.get();
	static Supplier<Double> splMovementSpeed = () -> spawnKillerBeesMovementSpeed.get();
	static Supplier<Operators> splOp = () -> {
		Operators operators = new Operators();
		SpawnKillerBee spawnOp = new SpawnKillerBee(operators.getSplLivingEntity(), operators.getSplTargetEntity(), splDamage, splMovementSpeed);
		Operator ifOp = new IfEntityIsntType(operators.getSplTargetEntity(), spawnOp, BeeEntity.class);
		operators.setOperator(ifOp);
		return operators;		
	};
		
	public KillerBeesIdolInventoryItem() {
		super(ITEM_NAME, killerBeesIdolInventoryItem, new ExecuteOperator(splOp.get()));
	}
}
