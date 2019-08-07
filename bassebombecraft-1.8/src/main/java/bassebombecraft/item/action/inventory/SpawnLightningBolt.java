package bassebombecraft.item.action.inventory;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.addLightning;
import static net.minecraft.particles.ParticleTypes.EFFECT;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spawns a lightning bolt to entities
 * within range.
 */
public class SpawnLightningBolt implements InventoryItemActionStrategy {

	static final int EFFECT_DURATION = 200; // Measured in ticks

	static final BasicParticleType PARTICLE_TYPE = EFFECT;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.75F;
	static final float G = 0.75F;
	static final float B = 0.75F;
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		return true;
	}

	@Override
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
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
	public int getEffectRange() {
		return 5;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

}
