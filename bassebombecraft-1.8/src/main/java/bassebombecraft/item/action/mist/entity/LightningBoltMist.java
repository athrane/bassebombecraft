package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.addLightning;
import static net.minecraft.particles.ParticleTypes.CLOUD;
import static net.minecraft.particles.ParticleTypes.FALLING_WATER;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.particles.BasicParticleType;
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
		BasicParticleType type = CLOUD;
		int duration = 20;
		double speed = 0.1;
		ParticleRenderingInfo cloud = getInstance(type, numbers, duration, r, g, b, speed);

		r = 0.0F;
		g = 0.0F;
		b = 0.25F;
		numbers = 1;
		type = FALLING_WATER;
		duration = 20;
		speed = 0.01;
		ParticleRenderingInfo water = getInstance(type, numbers, duration, r, g, b, speed);

		infos = new ParticleRenderingInfo[] { cloud, water };
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3d mistPos, LivingEntity invoker) {
		World world = target.getEntityWorld();

		AxisAlignedBB aabb = target.getBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		BlockPos.getAllInBox(min, max).forEach(pos -> addLightningAtBlockPos(world, pos));
	}

	/**
	 * Add lightning at block position.
	 * 
	 * @param world world.
	 * @param pos   block position where lightning is added.
	 */
	void addLightningAtBlockPos(World world, BlockPos pos) {
		LightningBoltEntity bolt = EntityType.LIGHTNING_BOLT.create(world);
		bolt.setPosition(pos.getX(), pos.getY(), pos.getZ());
		addLightning(bolt, world);
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
