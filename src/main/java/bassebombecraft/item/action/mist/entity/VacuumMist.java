package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.config.ModConfiguration.vacuumMistParticleInfo;

import java.util.function.Supplier;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.Vec3d;

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
	ParticleRenderingInfo[] infos;

	/**
	 * Effect pull force in blocks.
	 */
	int force;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * VacuumMist constructor.
	 *
	 * @param splDuration effect duration.
	 * @param splForce    effect pull force in blocks.
	 */
	public VacuumMist(Supplier<Integer> splDuration, Supplier<Integer> splForce) {
		infos = createFromConfig(vacuumMistParticleInfo);
		duration = splDuration.get();
		force = splForce.get();
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3d mistPos, LivingEntity invoker) {

		// calculate pull vector
		Vec3d targetPosVec = target.getPositionVector();
		Vec3d pullVec = mistPos.subtract(targetPosVec);
		pullVec = pullVec.normalize();

		// pull mob
		double x = pullVec.x * force;
		double y = pullVec.y * force;
		double z = pullVec.z * force;
		Vec3d motionVecForced = new Vec3d(x, y, z);
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
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

}
