package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of mist
 * action. This class builds a mist with vacuum effect that forcefully pulls
 * mobs.
 */
public class VacuumMist implements EntityMistActionStrategy {

	static final int EFFECT_DURATION = 500; // Measured in ticks
	static final int FORCE = 5; // pull force in blocks

	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.SPELL_MOB;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.75F;
	static final float B = 0.75F;
	static final float G = 0.75F;
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3d mistPos, LivingEntity invoker) {

		// calculate pull vector
		Vec3d targetPosVec = target.getPositionVector();
		Vec3d pullVec = mistPos.subtract(targetPosVec);
		pullVec = pullVec.normalize();

		// pull mob
		double x = pullVec.x * FORCE;
		double y = pullVec.y * FORCE;
		double z = pullVec.z * FORCE;
		Vec3d motionVecForced = new Vec3d(x, y, z);
		target.move(MoverType.SELF, motionVecForced.x, motionVecForced.y, motionVecForced.z);
	}

	@Override
	public int getEffectDuration() {
		return EFFECT_DURATION;
	}

	@Override
	public boolean isEffectAppliedToInvoker() {
		return false;
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
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

}
