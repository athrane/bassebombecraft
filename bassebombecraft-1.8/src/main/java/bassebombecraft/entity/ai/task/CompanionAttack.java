package bassebombecraft.entity.ai.task;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootLargeFireball;
import bassebombecraft.item.action.ShootMultipleArrows;
import bassebombecraft.item.action.ShootSmallFireball;
import bassebombecraft.item.action.ShootWitherSkull;
import bassebombecraft.item.action.mist.entity.EntityMistActionStrategy;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.ToxicMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;
import bassebombecraft.projectile.action.EmitHorizontalForce;
import bassebombecraft.projectile.action.EmitVerticalForce;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnCobweb;
import bassebombecraft.projectile.action.SpawnIceBlock;
import bassebombecraft.projectile.action.SpawnLavaBlock;
import bassebombecraft.projectile.action.SpawnSquid;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

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
	static final ProjectileAction EMIT_FORCE_PROJECTILE_ACTION = new EmitHorizontalForce();
	static final ProjectileAction EMIT_VERTICAL_FORCE_PROJECTILE_ACTION = new EmitVerticalForce();
	static final ProjectileAction SPAWN_SQUID_PROJECTILE_ACTION = new SpawnSquid();
	static final EntityMistActionStrategy SPAWN_VACUUM_MIST_PROJECTILE_ACTION = new VacuumMist();		
	static final EntityMistActionStrategy TOXIC_MIST_STRATEGY	 = new ToxicMist();		
	
	static final int MINIMUM_RANGE = 5;
	static final int UPDATE_FREQUENCY = 10; // Measured in ticks

	final EntityLiving entity;
	EntityLivingBase attackTarget;
	double entityMoveSpeed = 1.0D;
	double distanceToTargetSq;
	boolean isTargetClose;
	int ticksCounter = 0;

	/**
	 * GuardianAttack constructor.
	 * 
	 * @param entity
	 *            entity that the tasks is applied to.
	 */
	public CompanionAttack(EntityLiving entity) {
		this.entity = entity;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {

		GenericEntityMist action = new GenericEntityMist(new ToxicMist());
		action.onRightClick(entity.worldObj, entity);

		EntityLivingBase targetCandidate = this.entity.getAttackTarget();

		if (targetCandidate == null) {
			return false;
		} else {
			attackTarget = targetCandidate;
			return true;
		}
	}

	@Override
	public void startExecuting() {
		// NO-OP
	}

	@Override
	public void updateTask() {
		ticksCounter++;

		// look at target
	    entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
	       		
		// update task
		if (ticksCounter % UPDATE_FREQUENCY == 0) {

			// get movement info
			distanceToTargetSq = this.entity.getDistanceSq(attackTarget.posX, attackTarget.getEntityBoundingBox().minY,
					attackTarget.posZ);
			isTargetClose = (distanceToTargetSq < MINIMUM_RANGE);

			// move closer if target isn't a minimum range
			if (isTargetClose) {
				entity.getNavigator().clearPathEntity();
			} else {
				entity.getNavigator().tryMoveToEntityLiving(attackTarget, entityMoveSpeed);
			}

			// select behaviour type
			if (isTargetClose) {
				doCloseRangeAction();
			} else {
				doLongRangeAction();
			}

		}

	}

	/**
	 * Perform close attack action toward target mob.
	 * 
	 * Mists are not used due to update problems.
	 */
	void doCloseRangeAction() {
		int choice = entity.getRNG().nextInt(8);

		switch (choice) {

		case 0:
			RightClickedItemAction tmAction =  new GenericEntityMist(TOXIC_MIST_STRATEGY);
			tmAction.onRightClick(entity.worldObj, entity);
			break;
		
		case 1:
			RightClickedItemAction cwAction = new GenericShootEggProjectile(COWEB_PROJECTILE_ACTION);
			cwAction.onRightClick(entity.worldObj, entity);
			break;

		case 2:
			RightClickedItemAction efAction = new GenericShootEggProjectile(EMIT_FORCE_PROJECTILE_ACTION);
			efAction.onRightClick(entity.worldObj, entity);
			break;

		case 3:
			RightClickedItemAction evfAction = new GenericShootEggProjectile(EMIT_VERTICAL_FORCE_PROJECTILE_ACTION);
			evfAction.onRightClick(entity.worldObj, entity);
			break;

		case 4:
			RightClickedItemAction ibAction = new GenericShootEggProjectile(ICEBLOCK_PROJECTILE_ACTION);
			ibAction.onRightClick(entity.worldObj, entity);
			break;

		case 5:
			RightClickedItemAction lbAction = new GenericShootEggProjectile(LAVABLOCK_PROJECTILE_ACTION);
			lbAction.onRightClick(entity.worldObj, entity);
			break;
			
		case 6:
			RightClickedItemAction sasAction = new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION);
			sasAction.onRightClick(entity.worldObj, entity);
			break;

		case 7:
			RightClickedItemAction svmAction = new GenericEntityMist(SPAWN_VACUUM_MIST_PROJECTILE_ACTION);
			svmAction.onRightClick(entity.worldObj, entity);
			break;
						
		default:
			// NO-OP
		}
	}

	/**
	 * Perform long range attack action toward target mob.
	 * 
	 * Mists are not used due to update problems.
	 */
	void doLongRangeAction() {
		int choice = entity.getRNG().nextInt(4);

		switch (choice) {

		case 0:
			RightClickedItemAction sfAction = new ShootSmallFireball();
			sfAction.onRightClick(entity.worldObj, entity);
			break;

		case 1:
			RightClickedItemAction lfAction = new ShootLargeFireball();
			lfAction.onRightClick(entity.worldObj, entity);
			break;

		case 2:
			RightClickedItemAction wsAction = new ShootWitherSkull();
			wsAction.onRightClick(entity.worldObj, entity);
			break;

		case 3:
			RightClickedItemAction maAction = new ShootMultipleArrows();
			maAction.onRightClick(entity.worldObj, entity);
			break;

		case 4:
			RightClickedItemAction bbAction = new ShootBaconBazooka();
			bbAction.onRightClick(entity.worldObj, entity);
			break;
			
		case 5:
			RightClickedItemAction sasAction = new GenericShootEggProjectile(SPAWN_SQUID_PROJECTILE_ACTION);
			sasAction.onRightClick(entity.worldObj, entity);
			break;
			
		default:
			// NO-OP
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return shouldExecute() || !entity.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		attackTarget = null;
		ticksCounter = 0;
	}
}
