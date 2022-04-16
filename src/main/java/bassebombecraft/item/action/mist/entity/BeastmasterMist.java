package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeMobEntity;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of
 * mist action. This class builds a mist which charm a mob.
 */
public class BeastmasterMist implements EntityMistActionStrategy {

	static final int EFFECT_DURATION = 1000; // Measured in ticks

	static final SimpleParticleType PARTICLE_TYPE = ParticleTypes.EFFECT;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float G = 0.75F;
	static final float B = 0.0F;
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3 mistPos, LivingEntity invoker) {

		try {
			// skip if entity can't be charmed, i.e. is a mob entity
			if (!isTypeMobEntity(target))
				return;

			// type cast
			Mob mobEntity = (Mob) target;

			// register mob as charmed
			getProxy().getServerCharmedMobsRepository().add(mobEntity, invoker);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public int getEffectDuration() {
		return EFFECT_DURATION;
	}

	@Override
	public boolean isEffectAppliedToInvoker() {
		return true;
	}

	@Override
	public boolean isStationary() {
		return true;
	}

	@Override
	public boolean isOneShootEffect() {
		return false;
	}

	@Override
	public int getEffectRange() {
		return 5;
	}

	@Override
	public ParticleRenderingInfo getRenderingInfos() {
		return INFO;
	}

}
