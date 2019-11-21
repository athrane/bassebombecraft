package bassebombecraft.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Default implementation of the {@linkplain Observation} interface.
 */
public class DefaultObservation implements Observation {

	final String entityName;
	final float entityHealth;
	final float entityMaxHealth;
	final BlockPos entityPosition;
	final String targetName;
	final float targetHealth;
	final float targetMaxHealth;
	final BlockPos targetPosition;

	/**
	 * DefaultObservation constructor.
	 * 
	 * @param entity entity to observe.
	 * @param target target to observe.
	 */
	public DefaultObservation(LivingEntity entity, LivingEntity target) {
		entityName = entity.getName().getUnformattedComponentText();
		targetName = target.getName().getUnformattedComponentText();
		entityHealth = entity.getHealth();
		entityMaxHealth = entity.getMaxHealth();
		entityPosition = entity.getPosition();
		targetHealth = target.getHealth();
		targetMaxHealth = target.getMaxHealth();
		targetPosition = target.getPosition();
	}

	@Override
	public BlockPos getEntityPosition() {
		return entityPosition;
	}

	@Override
	public BlockPos getTargetPosition() {
		return targetPosition;
	}

	@Override
	public float getTargetHeatlh() {
		return targetHealth;
	}

	/**
	 * Get current observations as string.
	 * 
	 * @return current observations as string.
	 */
	public String getObservationAsString() {

		// build observation string
		return new StringBuilder()

				// add source info
				.append("sn=").append(entityName).append(",sh=").append(entityHealth).append("/")
				.append(entityMaxHealth).append(",sp=").append(entityPosition).append(";")

				// add target info
				.append("tn=").append(targetName).append(",th=").append(targetHealth).append("/")
				.append(targetMaxHealth).append(",tp=").append(targetPosition).append(";")

				.toString();
	}

	/**
	 * Factory method.
	 * 
	 * @param entity entity to observe.
	 * @param target target to observe.
	 * 
	 * @return repository.
	 */
	public static Observation getInstance(LivingEntity entity, LivingEntity target) {
		return new DefaultObservation(entity, target);
	}
}
