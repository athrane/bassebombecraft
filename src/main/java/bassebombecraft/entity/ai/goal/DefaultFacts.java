package bassebombecraft.entity.ai.goal;

import static bassebombecraft.ModConstants.AI_COMPANION_ATTACK_MINIMUM_RANGE;

import java.util.stream.Stream;

import bassebombecraft.geom.GeometryUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;

/**
 * Implementation if the {@linkplain Facts} interface.
 */
public class DefaultFacts implements SituationalFacts {

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
	 * Last observation.
	 */
	Observation last;

	/**
	 * Second last observation.
	 */
	Observation seconLast;

	/**
	 * Fact: Is target close, i.e. less than 5 blocks away?
	 */
	boolean isTargetClose;

	/**
	 * Fact: Did target health decrease between observations?
	 */
	boolean isTargetHealthDecreased;

	/**
	 * Observe target.
	 */
	public void observe() {
	}

	@Override
	public boolean isTargetClose() {
		return isTargetClose;
	}

	/**
	 * Return whether target has more health than the entity.
	 * 
	 * @return true if target has more health than the entity.
	 */
	public boolean hasTargetMoreHealth() {
		return (entity.getHealth() < target.getHealth());
	}

	@Override
	public boolean isTargetHealthDecreased() {
		return isTargetHealthDecreased;
	}

	/**
	 * Return whether target initially had more initial health.
	 * 
	 * @return true if target initially had more initial health.
	 */
	public boolean hasTargetInitiallyMoreHealth() {
		return (entity.getHealth() < target.getHealth());
	}

	@Override
	public String getFactsAsString() {
		return new StringBuilder().append(this.hashCode()).append(";")

				// add observations
				.append("tc=").append(isTargetClose()).append(",thd=").append(isTargetHealthDecreased()).append(";")
				.toString();
	}

	@Override
	public void update(Stream<Observation> observations) {
		Observation[] array = observations.toArray(Observation[]::new);
		last = array[array.length - 1];
		seconLast = array[array.length - 2];

		// isTargetClose?
		BlockPos entityPosition = last.getEntityPosition();
		BlockPos targetPosition = last.getTargetPosition();
		double dist = GeometryUtils.calculateDistance(entityPosition, targetPosition);
		isTargetClose = (dist < AI_COMPANION_ATTACK_MINIMUM_RANGE);

		// isTargetHealthDecreased?
		isTargetHealthDecreased = (last.getTargetHeatlh() < seconLast.getTargetHeatlh());
	}

	/**
	 * Factory method.
	 * 
	 * @return fact instance.
	 */
	public static SituationalFacts getInstance() {
		return new DefaultFacts();
	}

}
