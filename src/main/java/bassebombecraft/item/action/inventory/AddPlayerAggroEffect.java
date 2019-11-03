package bassebombecraft.item.action.inventory;

import static bassebombecraft.ModConstants.PLAYER_AGGRO_EFFECT;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class makes adds a player aggro effect to
 * nearby entities.
 */
public class AddPlayerAggroEffect implements InventoryItemActionStrategy {

	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.AMBIENT_ENTITY_EFFECT;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.75F;
	static final float G = 0.25F;
	static final float B = 0.25F;
	static final double PARTICLE_SPEED = 0.7;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Area of effect.
	 */
	int arreaOfEffect;

	/**
	 * AddPlayerAggroEffect constructor
	 * 
	 * @param key configuration key to initialize particle rendering info from.
	 */
	public AddPlayerAggroEffect(String key) {
		duration = ModConfiguration.playerAggroEffectDuration.get();
		arreaOfEffect = ModConfiguration.playerAggroEffectAreaOfEffect.get();
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		return true;
	}

	@Override
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
		if (isTypeLivingEntity(target)) {
			LivingEntity entityLivingBase = (LivingEntity) target;
			entityLivingBase.addPotionEffect(createEffect());
		}
	}

	@Override
	public int getEffectRange() {
		return arreaOfEffect;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	EffectInstance createEffect() {
		return new EffectInstance(PLAYER_AGGRO_EFFECT, duration);
	}

}
