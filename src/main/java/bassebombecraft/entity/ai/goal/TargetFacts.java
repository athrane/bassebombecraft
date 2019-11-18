package bassebombecraft.entity.ai.goal;

import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_MINIMUM_RANGE;

import net.minecraft.entity.LivingEntity;

/**
 * Facts about a target.
 */
public class TargetFacts {

	/**
	 * Observation counter..
	 */
	int observationCounter = 0;

	/**
	 * Distance to target.
	 */
	double distance;

	/**
	 * Entity, owner of the facts.
	 */
	LivingEntity entity;

	/**
	 * Entity attack target.
	 */
	LivingEntity target;

	/**
	 * Initial target health.
	 */
	float initialTargetHealth;

	/**
	 * TargetFacts constructor.
	 * 
	 * @param entity       owner of the facts.
	 * @param attackTarget entity attack target
	 */
	public TargetFacts(LivingEntity entity, LivingEntity attackTarget) {
		this.entity = entity;
		this.target = attackTarget;
		this.initialTargetHealth = attackTarget.getHealth();
	}

	/**
	 * get observed distance to target.
	 * 
	 * @return observed distance to target.
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Observe target.
	 */
	public void observe() {
		this.distance = entity.getDistanceSq(target.posX, target.getBoundingBox().minY, target.posZ);
		observationCounter++;
	}

	/**
	 * Return whether target is close, i.e. within the minimum attack range.
	 * 
	 * @return true if target is close.
	 */
	public boolean isTargetClose() {
		return (distance < AI_COMPANION_ATTACK_MINIMUM_RANGE);
	}

	/**
	 * Return whether target has more health.
	 * 
	 * @return true if target has more health.
	 */
	public boolean hasTargetMoreHealth() {
		return (entity.getHealth() < target.getHealth());
	}

	/**
	 * Return whether target initially had more initial health.
	 * 
	 * @return true if target initially had more initial health.
	 */
	public boolean hasTargetInitiallyMoreHealth() {
		return (entity.getHealth() < target.getHealth());
	}

	/**
	 * Get current observations as string.
	 * 
	 * @param context the context for the observation.
	 * 
	 * @return current observations as string.
	 */
	public String getCurrentObservationAsString(String context) {
		String str = new StringBuilder().append(this.hashCode()).append("-").append(observationCounter).append("-")
				.append(context).append(";")

				// add source info
				.append("sn=").append(entity.getName()).append(",sh=").append(entity.getHealth()).append(",sp=")
				.append(entity.getPosition()).append(";")

				// add target info
				.append("tn=").append(target.getName()).append(",th=").append(target.getHealth()).append(",tp=")
				.append(target.getPosition()).append(";")

				// add observations
				.append("oc=").append(isTargetClose()).append(",oh=").append(hasTargetMoreHealth()).append(";")

				.toString();
		return str;
	}

}
