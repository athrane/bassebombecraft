package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
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
	 * Random generator.
	 */
	static Random random = new Random();

	/**
	 * Ticks counter.
	 */
	int ticksCounter = 0;

	/**
	 * Mist position.
	 */
	Vec3 mistPos;

	/*
	 * Invoking entity.
	 */
	EntityLivingBase entity;

	/**
	 * Invoking entity look unit vector.
	 */
	Vec3 entityLook;

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
	}

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
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
	void applyEffect(World world, EntityLivingBase invokingEntity) {
		int aoeRange = strategy.getEffectRange();

		// get entities within AABB
		AxisAlignedBB aabb = AxisAlignedBB.fromBounds(mistPos.xCoord - aoeRange, mistPos.yCoord - aoeRange,
				mistPos.zCoord - aoeRange, mistPos.xCoord + aoeRange, mistPos.yCoord + aoeRange,
				mistPos.zCoord + aoeRange);
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

		for (EntityLivingBase foundEntity : entities) {

			// skip invoking entity if strategy specifies it
			if (hasIdenticalUniqueID(invokingEntity, foundEntity)) {
				if (!strategy.isEffectAppliedToInvoker())
					continue;
			}

			// apply effect
			strategy.applyEffectToEntity(foundEntity, mistPos);

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

	void calculateMistPostition(World world, EntityLivingBase entity) {
		entityLook = entity.getLook(1);
		Vec3 entityLookX4 = new Vec3(entityLook.xCoord * INVOCATION_DIST, entityLook.yCoord * INVOCATION_DIST,
				entityLook.zCoord * INVOCATION_DIST);
		Vec3 entityPos = new Vec3(entity.posX, entity.posY, entity.posZ);
		double x = entityLookX4.xCoord;
		float y = entity.getEyeHeight();
		double z = entityLookX4.zCoord;
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

	}

	@Override
	public String toString() {
		return super.toString() + ", strategy=" + strategy;
	}

}
