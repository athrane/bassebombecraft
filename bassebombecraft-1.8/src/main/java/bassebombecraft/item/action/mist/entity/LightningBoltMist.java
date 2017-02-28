package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.ModConstants.LIGHTNING_NOT_EFFECT_ONLY;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of
 * mist action. This class builds a mist with a lightning bolt effect.
 */
public class LightningBoltMist implements EntityMistActionStrategy {

	static final int EFFECT_DURATION = 500; // Measured in ticks

	ParticleRenderingInfo[] infos;

	/**
	 * LightningBoltMist constructor.
	 */
	public LightningBoltMist() {
		super();

		// initiate particle rendering info
		float r = 0.75F;
		float g = 0.75F;
		float b = 0.75F;
		int numbers = 5;
		EnumParticleTypes type = EnumParticleTypes.CLOUD;
		int duration = 20;
		double speed = 0.1;
		ParticleRenderingInfo flame = getInstance(type, numbers, duration, r, g, b, speed);

		r = 0.0F;
		g = 0.0F;
		b = 0.25F;
		numbers = 1;
		type = EnumParticleTypes.WATER_DROP;
		duration = 20;
		speed = 0.01;
		ParticleRenderingInfo lava = getInstance(type, numbers, duration, r, g, b, speed);

		infos = new ParticleRenderingInfo[] { flame, lava };

	}

	@Override
	public void applyEffectToEntity(EntityLivingBase target, Vec3d mistPos, EntityLivingBase invoker) {
		World world = target.getEntityWorld();

		AxisAlignedBB aabb = target.getEntityBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		for (Object pos : BlockPos.getAllInBox(min, max)) {
			BlockPos typedPos = (BlockPos) pos;
			EntityLightningBolt bolt = new EntityLightningBolt(world, typedPos.getX(), typedPos.getY(), typedPos.getZ(),
					LIGHTNING_NOT_EFFECT_ONLY);
			world.addWeatherEffect(bolt);
		}
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
		return infos;
	}

}
