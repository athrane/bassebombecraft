package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which creates a
 * mist with a custom effect when a living entity enters the mist.
 * 
 * The effect is implemented by the configured strategy
 * {@linkplain EntityMistActionStrategy}.
 */
public class GenericEntityMist implements RightClickedItemAction {

	/**
	 * Rendering frequency in ticks.
	 */
	static final int RENDERING_FREQUENCY = 5;

	/**
	 * Effect frequency when targeted mob are affect by most. Frequency is
	 * measured in ticks.
	 */
	static final int EFFECT_UPDATE_FREQUENCY = 10; // Measured in ticks

	/**
	 * Spawn distance of mist from invoker. Distance is measured in blocks.
	 */
	static final int INVOCATION_DIST = 4;

	/**
	 * Spiral size.
	 */
	static final int SPIRAL_SIZE = 20;
	
	/**
	 * Random generator.
	 */
	static Random random = new Random();

	/**
	 * Ticks counter.
	 */
	int ticksCounter = 0;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;
	
	/**
	 * Mist position.
	 */
	Vec3d mistPos;

	/*
	 * Invoking entity.
	 */
	LivingEntity entity;

	/**
	 * Invoking entity look unit vector.
	 */
	Vec3d entityLook;

	/**
	 * Defines whether behaviour is active.
	 */
	boolean isActive = false;

	/**
	 * Mist strategy.
	 */
	EntityMistActionStrategy strategy;

	/**
	 * GenericEntityMist constructor.
	 * 
	 * @param strategy
	 *            mist strategy.
	 */
	public GenericEntityMist(EntityMistActionStrategy strategy) {
		this.strategy = strategy;
		
		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(SPIRAL_SIZE, SPIRAL_SIZE);		
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		this.entity = entity;
		isActive = true;
		ticksCounter = 0;
		calculateMistPostition(world, entity);
	}

	/**
	 * Apply effect to creatures within range.
	 * 
	 * @param world
	 *            world object
	 * @param invokingEntity
	 *            entity object
	 */
	void applyEffect(World world, LivingEntity invokingEntity) {
		int aoeRange = strategy.getEffectRange();

		// get entities within AABB
		AxisAlignedBB aabb = new AxisAlignedBB(mistPos.x - aoeRange, mistPos.y - aoeRange,
				mistPos.z - aoeRange, mistPos.x + aoeRange, mistPos.y + aoeRange,
				mistPos.z + aoeRange);
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		for (LivingEntity foundEntity : entities) {

			// skip invoking entity if strategy specifies it
			if (hasIdenticalUniqueID(invokingEntity, foundEntity)) {
				if (!strategy.isEffectAppliedToInvoker())
					continue;
			}

			// apply effect
			strategy.applyEffectToEntity(foundEntity, mistPos, invokingEntity);
			
			// add entity as a targeted entity
			TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
			repository.add(foundEntity);			

			// exit if strategy is one shot effect
			if (strategy.isOneShootEffect()) {
				isActive = false;
				return;
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		// exit if mist isn't active
		if (!isActive())
			return;

		// render mist
		if (ticksCounter % RENDERING_FREQUENCY == 0) {
			render(worldIn);
		}

		// update game effect
		if (ticksCounter % EFFECT_UPDATE_FREQUENCY == 0) {
			applyEffect(worldIn, entity);
		}

		// disable if duration is completed
		if (ticksCounter > strategy.getEffectDuration()) {
			isActive = false;
			entity = null;
			return;
		}

		ticksCounter++;
	}

	/**
	 * Returns true if behaviour is active.
	 * 
	 * @return true if behaviour is active.
	 */
	boolean isActive() {
		return isActive;
	}

	/**
	 * Calculate mist position.
	 * 
	 * Mist is spawned 4 blocks away from the entity at eye height.
	 * 
	 * @param world
	 *            world object.
	 * @param entity
	 *            entity object
	 */

	void calculateMistPostition(World world, LivingEntity entity) {
		entityLook = entity.getLook(1);
		Vec3d entityLookX4 = new Vec3d(entityLook.x * INVOCATION_DIST, entityLook.y * INVOCATION_DIST,
				entityLook.z * INVOCATION_DIST);
		Vec3d entityPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
		double x = entityLookX4.x;
		float y = entity.getEyeHeight();
		double z = entityLookX4.z;
		mistPos = entityPos.addVector(x, y, z);
	}

	/**
	 * Render mist in world.
	 * 
	 * @param world
	 *            world object.
	 */
	void render(World world) {

		// update position if mist should move away from the invoking entity
		if (!strategy.isStationary()) {
			mistPos = mistPos.add(entityLook);
		}

		// get repository
		ParticleRenderingRepository particleRepository = getBassebombeCraft().getParticleRenderingRepository();

		// register particle for rendering
		BlockPos pos = new BlockPos(mistPos);

		// iterate over rendering info's
		for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {
			ParticleRendering particle = getInstance(pos, info);
			particleRepository.add(particle);
		}
		
		// calculate spiral index
		int spiralCounter = ( ticksCounter / RENDERING_FREQUENCY ) % SPIRAL_SIZE;
		
		// get next spiral coordinate
		BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);

		// calculate ground coordinates
		BlockPos pos2 = pos.add(spiralCoord.getX(), 0, spiralCoord.getZ());

		// iterate over rendering info's
		for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {
			ParticleRendering particle = getInstance(pos2, info);
			particleRepository.add(particle);
		}
		
	}

	@Override
	public String toString() {
		return super.toString() + ", strategy=" + strategy;
	}

}
