package bassebombecraft.entity.ai.goal;

import static bassebombecraft.ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_AGGRO;
import static bassebombecraft.ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE;
import static bassebombecraft.ModConstants.MOB_AGGRO_EFFECT;
import static bassebombecraft.entity.EntityUtils.getNullableTarget;
import static bassebombecraft.entity.EntityUtils.hasTarget;
import static net.minecraft.entity.ai.goal.Goal.Flag.TARGET;

import java.util.EnumSet;
import java.util.Optional;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.EffectInstance;

/**
 * AI target acquisition goal which attacks commanders target.
 * 
 * Entity self-destructs if commander has died and aggros everything.
 */
public class CommandersTargetGoal extends Goal {

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
	public CommandersTargetGoal(CreatureEntity entity, LivingEntity commander) {
		this.entity = entity;
		this.commander = commander;

		// "target" AI
		setMutexFlags(EnumSet.of(TARGET));
	}

	@Override
	public boolean shouldExecute() {

		// exit if commander is dead
		if (!commander.isAlive()) {
			selfDestruct();
			return false;
		}

		// stop goal execution if no target is defined for commander
		if (!hasTarget(commander)) 
			return false;

		// get target
		Optional<LivingEntity> optTarget = getNullableTarget(entity);

		// exit if target isn't defined (anymore)
		if (!optTarget.isPresent())
			return false;

		// continue goal execution if target is alive
		return (optTarget.get().isAlive());
	}

	@Override
	public void tick() {

		// get target
		Optional<LivingEntity> optTarget = getNullableTarget(entity);

		// exit if target isn't defined (anymore)
		if (!optTarget.isPresent())
			return;

		// update target
		entity.setAttackTarget(optTarget.get());
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
		entity.setFire(AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE);

		// add aggro effect
		EffectInstance effect = new EffectInstance(MOB_AGGRO_EFFECT, AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_AGGRO);
		entity.addPotionEffect(effect);
	}

}
