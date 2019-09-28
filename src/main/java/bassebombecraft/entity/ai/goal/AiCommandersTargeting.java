package bassebombecraft.entity.ai.goal;

import static bassebombecraft.entity.EntityUtils.getAliveTarget;
import static bassebombecraft.entity.EntityUtils.hasAliveTarget;
import static net.minecraft.entity.ai.goal.Goal.Flag.TARGET;

import java.util.EnumSet;

import bassebombecraft.ModConstants;
import bassebombecraft.potion.MobEffects;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.EffectInstance;

/**
 * AI target acquisition goal which attacks commanders target.
 * 
 * Entity self-destructs if commander has died and aggros everything.
 */
public class AiCommandersTargeting extends Goal {

	/**
	 * Null/No target value to use when clearing the target.
	 */
	static final LivingEntity NO_TARGET = null;

	/**
	 * Goal owner.
	 */
	final CreatureEntity entity;

	/**
	 * Mob commander.
	 */
	LivingEntity commander;

	/**
	 * AiCommandersTargeting constructor.
	 * 
	 * @param entity    commanded entity.
	 * @param commander entity which commands entity.
	 */
	public AiCommandersTargeting(CreatureEntity entity, LivingEntity commander) {
		this.entity = entity;
		this.commander = commander;

		// "target" AI
		setMutexFlags(EnumSet.of(TARGET));
	}

	@Override
	public boolean shouldExecute() {

		// exit if commander is dead
		if (!commander.isAlive())
			selfDestruct();

		// exit if commander doesn't has a target
		if (!hasAliveTarget(commander))
			return false;

		// otherwise attack
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {

		// exit if commander doesn't has a target
		if (!hasAliveTarget(commander))
			return false;

		// get target
		LivingEntity target = getAliveTarget(commander);

		// update target
		entity.setAttackTarget(target);
		return true;
	}

	@Override
	public void startExecuting() {
		// NO-OP
	}

	@Override
	public void resetTask() {
		// clear target
		entity.setAttackTarget(NO_TARGET);
	}

	/**
	 * Self-destruct entity by settings on fire and apply aggro effect.
	 */
	void selfDestruct() {
		entity.setFire(ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE);

		// add aggro effect
		EffectInstance effect = new EffectInstance(MobEffects.MOBS_AGGRO_POTION,
				ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_AGGRO);
		entity.addPotionEffect(effect);
	}

}
