package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.config.ConfigUtils.createInfoFromConfig;
import static bassebombecraft.config.ModConfiguration.vacuumMistDuration;
import static bassebombecraft.config.ModConfiguration.vacuumMistForce;
import static bassebombecraft.config.ModConfiguration.vacuumMistParticleInfo;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of
 * mist action. This class builds a mist with vacuum effect that forcefully
 * pulls mobs.
 */
public class VacuumMist implements EntityMistActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = VacuumMist.class.getSimpleName();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo info;

	/**
	 * Effect pull force in blocks.
	 */
	int force;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Constructor.
	 */
	public VacuumMist() {
		info = createInfoFromConfig(vacuumMistParticleInfo);
		duration = vacuumMistDuration.get();
		force = vacuumMistForce.get();
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3 mistPos, LivingEntity invoker) {

		// calculate pull vector
		Vec3 targetPosVec = target.position();
		Vec3 pullVec = mistPos.subtract(targetPosVec);
		pullVec = pullVec.normalize();

		// pull mob
		double x = pullVec.x * force;
		double y = pullVec.y * force;
		double z = pullVec.z * force;
		Vec3 motionVecForced = new Vec3(x, y, z);
		target.move(MoverType.SELF, motionVecForced);
	}

	@Override
	public int getEffectDuration() {
		return duration;
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
	public ParticleRenderingInfo getRenderingInfos() {
		return info;
	}

}
