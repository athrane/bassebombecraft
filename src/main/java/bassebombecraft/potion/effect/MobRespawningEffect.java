package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import bassebombecraft.event.potion.MobRespawningEffectEventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Effect which respawns multiple mobs of the same same when a mob is killed.
 * 
 * The logic of the effect is implemented in the
 * {@linkplain MobRespawningEffectEventHandler} which is invoked when an entity
 * is dead and should respawn if the effect is active.
 * 
 * The effect has no effect on the player.
 */
public class MobRespawningEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public final static String NAME = MobRespawningEffect.class.getSimpleName();

	/**
	 * MobAggroEffect constructor.
	 */
	public MobRespawningEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		// NO-OP
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
