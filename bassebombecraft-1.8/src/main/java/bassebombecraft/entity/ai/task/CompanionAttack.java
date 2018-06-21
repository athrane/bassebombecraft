package bassebombecraft.entity.ai.task;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_MINIMUM_RANGE;
import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_UPDATE_FREQUENCY;
import static bassebombecraft.entity.EntityUtils.getAliveTarget;
import static bassebombecraft.entity.EntityUtils.hasAliveTarget;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.action.ShootLargeFireball;
import bassebombecraft.item.action.ShootMultipleArrows;
import bassebombecraft.item.action.ShootSmallFireball;
import bassebombecraft.item.action.ShootWitherSkull;
import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.LightningBoltMist;
import bassebombecraft.item.action.mist.entity.ToxicMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;
import bassebombecraft.projectile.action.DigMobHole;
import bassebombecraft.projectile.action.EmitHorizontalForce;
import bassebombecraft.projectile.action.EmitVerticalForce;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnAnvil;
import bassebombecraft.projectile.action.SpawnCobweb;
import bassebombecraft.projectile.action.SpawnFlamingChicken;
import bassebombecraft.projectile.action.SpawnIceBlock;
import bassebombecraft.projectile.action.SpawnKittenArmy;
import bassebombecraft.projectile.action.SpawnLavaBlock;
import bassebombecraft.projectile.action.SpawnLightningBolt;
import bassebombecraft.projectile.action.SpawnSquid;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * AI task for companion, e.g. charmed mob or guardian.
 * 
 * The task will attack the targeted mob using the abilities implement in the
 * BassBombeCraft mod.
 */
public class CompanionAttack extends EntityAIBase {

	static final ProjectileAction COWEB_PROJECTILE_ACTION = new SpawnCobweb();
	static final ProjectileAction ICEBLOCK_PROJECTILE_ACTION = new SpawnIceBlock();
	static final ProjectileAction LAVABLOCK_PROJECTILE_ACTION = new SpawnLavaBlock();
	static final ProjectileAction LIGHTNING_PROJECTILE_ACTION = new SpawnLightningBolt();
	static final ProjectileAction EMIT_FORCE_PROJECTILE_ACTION = new EmitHorizontalForce();
	static final ProjectileAction EMIT_VERTICAL_FORCE_PROJECTILE_ACTION = new EmitVerticalForce();
	static final ProjectileAction SPAWN_SQUID_PROJECTILE_ACTION = new SpawnSquid();
	static final ProjectileAction FALLING_ANVIL_PROJECTILE_ACTION = new SpawnAnvil();
	static final ProjectileAction MOB_HOLE_PROJECTILE_ACTION = new DigMobHole();
	static final ProjectileAction KITTEN_ARMY_PROJECTILE_ACTION = new SpawnKittenArmy();
	static final ProjectileAction FLAMING_CHICKEN_PROJECTILE_ACTION = new SpawnFlamingChicken();

	static final EntityMistActionStrategy SPAWN_VACUUM_MIST_PROJECTILE_ACTION = new VacuumMist();
	static final EntityMistActionStrategy TOXIC_MIST_STRATEGY = new ToxicMist();
	static final EntityMistActionStrategy LIGHTNING_MIST_STRATEGY = new LightningBoltMist();

	final static String CREEPER_CANNON_CONFIG_KEY = ShootCreeperCannon.class.getSimpleName();
	final static boolean ISNT_PRIMED = false;
	/**
	 * Target entity.
	 */
	final EntityLiving entity;

	EntityLivingBase attackTarget;
	double entityMoveSpeed = 1.0D;
	double distanceToTargetSq;
	boolean isTargetClose;

	/**
	 * List of long range actions.
	 */
	ArrayList<RightClickedItemAction> longRangeActions;

	/**
	 * List of close range actions.
	 */
	ArrayList<RightClickedItemAction> closeRangeActions;

	/**
	 * Mob commander.
	 */
	EntityPlayer commander;

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity
	 *            entity that the tasks is applied to.
	 */
	public CompanionAttack(EntityLiving entity) {
		this.entity = entity;

		// "interactive" AI, including attack
		this.setMutexBits(3);

		longRangeActions = new ArrayList<RightClickedItemAction>();
		longRangeActions.add(new ShootSmallFireball());
		longRangeActions.add(new ShootLargeFireball());
		longRangeActions.add(new ShootWitherSkull());
		longRangeActions.add(new ShootMultipleArrows());
		longRangeActions.add(new ShootBaconBazooka());
		longRangeActions.add(new ShootCreeperCannon(ISNT_PRIMED, CREEPER_CANNON_CONFIG_KEY));
		longRangeActions.add(new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(FALLING_ANVIL_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(MOB_HOLE_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(KITTEN_ARMY_PROJECTILE_ACTION));
		longRangeActions.add(new GenericShootEggProjectile(FLAMING_CHICKEN_PROJECTILE_ACTION));

		closeRangeActions = new ArrayList<RightClickedItemAction>();
		closeRangeActions.add(new GenericEntityMist(TOXIC_MIST_STRATEGY));
		closeRangeActions.add(new GenericShootEggProjectile(COWEB_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(EMIT_FORCE_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(EMIT_VERTICAL_FORCE_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(ICEBLOCK_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(LAVABLOCK_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(FALLING_ANVIL_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericEntityMist(SPAWN_VACUUM_MIST_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericShootEggProjectile(LIGHTNING_PROJECTILE_ACTION));
		closeRangeActions.add(new GenericEntityMist(LIGHTNING_MIST_STRATEGY));
		closeRangeActions.add(new GenericShootEggProjectile(MOB_HOLE_PROJECTILE_ACTION));
	}

	/**
	 * CompanionAttack constructor.
	 * 
	 * @param entity
	 *            entity that the tasks is applied to.
	 * @param commander
	 *            entity which commands entity.
	 */
	public CompanionAttack(EntityLiving entity, EntityPlayer commander) {
		this(entity);
		this.commander = commander;
	}

	@Override
	public boolean shouldExecute() {

		// exit if no living target is defined
		if (!hasAliveTarget(this.entity))
			return false;

		// get candidate
		EntityLivingBase targetCandidate = getAliveTarget(this.entity);

		// set target
		attackTarget = targetCandidate;
		return true;
	}

	@Override
	public void startExecuting() {
		// NO-OP
	}

	@Override
	public void updateTask() {

		// look at target
		entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);

		// get repository
		FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();

		// exit if frequency isn't active
		if (!repository.isActive(AI_COMPANION_ATTACK_UPDATE_FREQUENCY))
			return;

		// get movement info
		distanceToTargetSq = this.entity.getDistanceSq(attackTarget.posX, attackTarget.getEntityBoundingBox().minY,
				attackTarget.posZ);
		isTargetClose = (distanceToTargetSq < AI_COMPANION_ATTACK_MINIMUM_RANGE);

		// move closer if target isn't a minimum range
		if (isTargetClose) {
			entity.getNavigator().clearPath();
		} else {
			entity.getNavigator().tryMoveToEntityLiving(attackTarget, entityMoveSpeed);
		}

		// select behaviour type
		String aiAction = "";
		if (isTargetClose)
			aiAction = doCloseRangeAction();
		else
			aiAction = doLongRangeAction();

		// add AI observation event
		// System.out.println("target=" + attackTarget);
		// System.out.println("dist=" + distanceToTargetSq);
		// System.out.println("isTargetClose=" + isTargetClose);
		// System.out.println("action=" + aiAction);
		// String uid = commander.getName();
		try {
			// aiObserve(uid, distanceToTargetSq, isTargetClose, aiAction);
		} catch (Exception e) {
			// TODO: add to centralized exception handling.
			// NOP
		}

	}

	/**
	 * Perform close attack action toward target mob.
	 * 
	 * Mists are not used due to update problems.
	 * 
	 * @return chosen action.
	 */
	String doCloseRangeAction() {
		int numberActions = longRangeActions.size();
		int choice = entity.getRNG().nextInt(numberActions);
		RightClickedItemAction action = closeRangeActions.get(choice);
		action.onRightClick(entity.getEntityWorld(), entity);
		return ReflectionToStringBuilder.toString(action);
		// action.getClass().getSimpleName();
	}

	/**
	 * Perform long range attack action toward target mob.
	 * 
	 * Mists are not used due to update problems.
	 * 
	 * @return chosen action.
	 */
	String doLongRangeAction() {
		int numberActions = longRangeActions.size();
		int choice = entity.getRNG().nextInt(numberActions);
		RightClickedItemAction action = longRangeActions.get(choice);
		action.onRightClick(entity.getEntityWorld(), entity);
		return ReflectionToStringBuilder.toString(action);
		// action.getClass().getSimpleName();
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute() || !entity.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		attackTarget = null;
	}
}
