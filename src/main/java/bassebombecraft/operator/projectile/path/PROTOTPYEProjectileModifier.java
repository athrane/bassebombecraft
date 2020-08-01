package bassebombecraft.operator.projectile.path;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.event.job.Job;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.job.ExecuteOperatorAsJob2;
import bassebombecraft.projectile.CompositeProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which adds a tag to a
 * projectile.
 * 
 * The tag is processed within the projectile
 * {@linkplain CompositeProjectileEntity} which and scans for projectile tages
 * and executes the configured operator for a projectile modifier.
 * 
 * TODO: Write description...
 * 
 * @deprecated Prototype to be deleted
 */
@Deprecated
public class PROTOTPYEProjectileModifier implements Operator2 {

	/**
	 * Function to get projectiles.
	 */
	Function<Ports, Entity[]> fnGetProjectiles;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectiles function to get projectiles.
	 */
	public PROTOTPYEProjectileModifier(Function<Ports, Entity[]> fnGetProjectiles) {
		this.fnGetProjectiles = fnGetProjectiles;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 */
	public PROTOTPYEProjectileModifier() {
		this(getFnGetEntities1());
	}

	@Override
	public Ports run(Ports ports) {

		// Job TTL
		int duration = 50;

		// get projectiles
		Entity[] projectiles = fnGetProjectiles.apply(ports);

		// create job operators
		// Operator2 jobOp = new Sequence2(new IsWorldAtServerSide2(), new
		// RandomPath());
		Operator2 jobOp = new RandomPath();

		for (Entity projectile : projectiles) {

			// get closest mob
			LivingEntity target = findTargetMob(projectile);
			getBassebombeCraft().getLogger().debug("target=" + target);

			// skip job creation if no targets
			if (target == null)
				continue;

			// create job id
			String id = new StringBuilder().append(projectile.hashCode()).toString();

			// create ports
			Ports jobPorts = getInstance();
			jobPorts.setEntity1(projectile);
			jobPorts.setLivingEntity1(target);

			jobPorts.setString1(id);

			// create job
			Job job = new ExecuteOperatorAsJob2(jobPorts, jobOp);
			getProxy().getServerJobRepository().add(id, duration, job);
		}

		// Arrays.stream(projectiles).forEach(p -> p.addTag(tag));

		return ports;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	LivingEntity findTargetMob(Entity entity) {
		double arreaOfEffect = 25;

		// get list of mobs
		AxisAlignedBB aabb = entity.getBoundingBox().grow(arreaOfEffect, arreaOfEffect, arreaOfEffect);
		List<LivingEntity> targetList = entity.world.getEntitiesWithinAABB(MobEntity.class, aabb, null);

		// exit if no targets where found
		if (targetList.isEmpty())
			return null;

		// sort mobs
		EntityDistanceSorter entityDistanceSorter = new EntityDistanceSorter();
		entityDistanceSorter.setEntity(entity);
		Collections.sort(targetList, entityDistanceSorter);

		// get new target
		return targetList.get(0);
	}

	class RandomPath implements Operator2 {

		@Override
		public Ports run(Ports ports) {

			// get projectile
			Entity projectile = ports.getEntity1();

			getBassebombeCraft().getLogger().debug("projectile=" + projectile);
			getBassebombeCraft().getLogger().debug("projectile.alive=" + projectile.isAlive());

			// exit if projectile isn't alive (anymore)
			if (!projectile.isAlive()) {

				// get job id
				String id = ports.getString1();

				getBassebombeCraft().getLogger().debug("projectile id=" + id);
				getBassebombeCraft().getLogger().debug("job exist=" + getProxy().getServerJobRepository().contains(id));

				// remove job
				getProxy().getServerJobRepository().remove(id);

				getBassebombeCraft().getLogger().debug("job exist=" + getProxy().getServerJobRepository().contains(id));

				return ports;
			}

			// get target
			LivingEntity target = ports.getLivingEntity1();
			getBassebombeCraft().getLogger().debug("target=" + target);

			// calculate vector to target
			Vec3d projectilePosition = projectile.getPositionVector();
			Vec3d projectileMotionVector = projectile.getMotion();
			Vec3d normalizedProjectileMotionVector = projectileMotionVector.normalize();
			getBassebombeCraft().getLogger().debug("projectile position=" + projectilePosition);
			getBassebombeCraft().getLogger().debug("projectile motion vector=" + projectileMotionVector);
			getBassebombeCraft().getLogger()
					.debug("normalized projectile motion vector=" + normalizedProjectileMotionVector);

			// calculate direction (to - from)
			Vec3d targetPosition = target.getPositionVector();
			getBassebombeCraft().getLogger().debug("target.position=" + projectileMotionVector);

			Vec3d directionVector = targetPosition.subtract(projectilePosition);
			getBassebombeCraft().getLogger().debug("directionVector=" + directionVector);

			// normalise direction vector
			Vec3d normalizedDirectionVector = directionVector.normalize();
			getBassebombeCraft().getLogger().debug("normalized directionVector=" + normalizedDirectionVector);

			double dotValue = normalizedDirectionVector.dotProduct(normalizedProjectileMotionVector);
			double angleRadians = Math.acos(dotValue);
			double angle = Math.toDegrees(angleRadians);
			getBassebombeCraft().getLogger().debug("angle=" + angle);

			float f1 = MathHelper.sqrt(Entity.horizontalMag(projectileMotionVector));
			float rotationYaw = (float) (MathHelper.atan2(projectileMotionVector.x, projectileMotionVector.z)
					* (double) (180F / (float) Math.PI));
			float rotationPitch = (float) (MathHelper.atan2(projectileMotionVector.y, f1)
					* (double) (180F / (float) Math.PI));
			getBassebombeCraft().getLogger().debug("rotationYaw =" + rotationYaw);
			getBassebombeCraft().getLogger().debug("rotationPitch =" + rotationPitch);

			// rotate motion vector
			Vec3d rotatedMotionVector = projectileMotionVector.rotateYaw((float) -angle);
			getBassebombeCraft().getLogger().debug("rotatedMotionVector=" + rotatedMotionVector);

			Vec3d newMotionVector = projectileMotionVector.scale(0.5F);
			Vec3d newPositionVector = projectileMotionVector.add(projectilePosition);
			getBassebombeCraft().getLogger().debug("newMotionVector=" + newMotionVector);
			getBassebombeCraft().getLogger().debug("newPositionVector=" + newPositionVector);

			// projectile.setMotion(rotatedMotionVector);
			// projectile.setMotion(normalizedDirectionVector.scale(0.25F));

			projectile.setMotion(newMotionVector);
			projectile.setPosition(newPositionVector.getX(), newPositionVector.getY(), newPositionVector.getZ());

			// projectileMotion
			// http://www.theappguruz.com/blog/create-homing-missiles-in-game-unity-tutorial
			// https://github.com/Brackeys/Homing-Missile/issues/1
			// https://stackoverflow.com/questions/29408214/calculate-angle-between-two-3d-vectors

			// calculate random angle
			// Random random = getBassebombeCraft().getRandom();
			// double angle = random.nextInt(DEGREES_90 * 2) - DEGREES_90;
			// getBassebombeCraft().getLogger().debug("angle="+angle);

			// rotate
			// Vec3d rotated = rotateUnitVectorAroundYAxisAtOrigin(angle, projectileMotion);
			// getBassebombeCraft().getLogger().debug("projectile.rotated="+rotated);

			// update motion
			// projectile.setMotion(rotated);

			return ports;
		}

	}
}
