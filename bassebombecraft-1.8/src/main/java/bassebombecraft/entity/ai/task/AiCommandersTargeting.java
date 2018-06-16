package bassebombecraft.entity.ai.task;

import static bassebombecraft.entity.EntityUtils.getAliveTarget;
import static bassebombecraft.entity.EntityUtils.hasAliveTarget;

import bassebombecraft.ModConstants;
import bassebombecraft.potion.MobEffects;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.potion.PotionEffect;

/**
 * AI target acquisition task which attacks commanders target.
 */
public class AiCommandersTargeting extends EntityAITarget {

	/**
	 * Mob AI commander.
	 */
	EntityLiving commander;

	/**
	 * AiCommandersTargeting constructor.
	 * 
	 * @param owner
	 *            commanded entity.
	 * @param commander
	 *            entity which commands entity.
	 */
	public AiCommandersTargeting(EntityCreature owner, EntityLiving commander) {
		super(owner, false);
		this.commander = commander;

		// Uncategorised compatible with every task
		// this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		
		// exit if command is dead
		if(commander.isDead) selfDestruct();

		// exit if commander doesn't has a target
		if (!hasAliveTarget(commander)) return false;
		
		// otherwise attack
		return true;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */	
	@Override
	public boolean shouldContinueExecuting() {

		// exit if commander doesn't has a target
		if (!hasAliveTarget(commander)) return false;
		
		// get target
		EntityLivingBase target = getAliveTarget(commander);
		
		// update target
		super.taskOwner.setAttackTarget(target);
		
		return true;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		super.resetTask();
	}

	/**
	 * Self-destruct entity by settings on fire and apply aggro effect.
	 */
	void selfDestruct() {
		super.taskOwner.setFire(ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE);
		
		// add aggro effect
		PotionEffect effect = new PotionEffect(MobEffects.MOBS_AGGRO_POTION, ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_AGGRO);
		super.taskOwner.addPotionEffect(effect);		
	}
	
}
