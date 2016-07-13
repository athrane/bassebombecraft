package bassebombecraft.item.action.inventory;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spawns a lightning bolt to entities
 * within range.
 */
public class SpawnLightningBolt implements InventoryItemActionStrategy {

	static final int EFFECT_DURATION = 200; // Measured in ticks
	
	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.SPELL_MOB;
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
		if (targetIsInvoker) return false;
		return true;
	}

	@Override
	public void applyEffect(Entity target, World world) {
		AxisAlignedBB aabb = target.getEntityBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		for (Object pos : BlockPos.getAllInBox(min, max)) {
			BlockPos typedPos = (BlockPos) pos;
			EntityLightningBolt bolt = new EntityLightningBolt(world, typedPos.getX(), typedPos.getY(),
					typedPos.getZ());
			world.addWeatherEffect(bolt);
		}		
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
