package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.BLOCK_EFFECT_FREQUENCY;
import static bassebombecraft.ModConstants.PARTICLE_RENDERING_FREQUENCY;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
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
	 * Action identifier.
	 */
	public static final String NAME = GenericEntityMist.class.getSimpleName();

	/**
	 * Spawn distance of mist from invoker. Distance is measured in blocks.
	 */
	static final int INVOCATION_DIST = 4;

	/**
	 * Ticks counter.
	 */
	@Deprecated
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
	 * Spiral size.
	 */
	int spiralSize;

	/**
	 * GenericEntityMist constructor.
	 * 
	 * @param strategy mist strategy.
	 */
	public GenericEntityMist(EntityMistActionStrategy strategy) {
		this.strategy = strategy;
		spiralSize = ModConfiguration.genericEntityMistSpiralSize.get();

		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(spiralSize, spiralSize);
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
	 * @param world          world object
	 * @param invokingEntity entity object
	 */
	void applyEffect(World world, LivingEntity invokingEntity) {
		int aoeRange = strategy.getEffectRange();

		// get entities within AABB
		AxisAlignedBB aabb = new AxisAlignedBB(mistPos.x - aoeRange, mistPos.y - aoeRange, mistPos.z - aoeRange,
				mistPos.x + aoeRange, mistPos.y + aoeRange, mistPos.z + aoeRange);
		List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		for (LivingEntity foundEntity : entities) {

			// skip invoking entity if strategy specifies it
			if (hasIdenticalUniqueID(invokingEntity, foundEntity)) {
				if (!strategy.isEffectAppliedToInvoker())
					continue;
			}

			// apply effect
			strategy.applyEffectToEntity(foundEntity, mistPos, invokingEntity);

			// exit if strategy is one shot effect
			if (strategy.isOneShootEffect()) {
				isActive = false;
				return;
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		try {

			// exit if mist isn't active
			if (!isActive())
				return;

			// render mist if frequency is active
			FrequencyRepository repository = getProxy().getFrequencyRepository();
			if (repository.isActive(PARTICLE_RENDERING_FREQUENCY))
				render(worldIn);

			// update effect if frequency is active
			if (repository.isActive(BLOCK_EFFECT_FREQUENCY))
				applyEffect(worldIn, entity);

			// disable if duration is completed
			if (ticksCounter > strategy.getEffectDuration()) {
				isActive = false;
				entity = null;
				return;
			}

			ticksCounter++;

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
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
	 * @param world  world object.
	 * @param entity entity object
	 */

	void calculateMistPostition(World world, LivingEntity entity) {
		entityLook = entity.getLook(1);
		Vec3d entityLookX4 = new Vec3d(entityLook.x * INVOCATION_DIST, entityLook.y * INVOCATION_DIST,
				entityLook.z * INVOCATION_DIST);
		Vec3d entityPos = new Vec3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());
		double x = entityLookX4.x;
		float y = entity.getEyeHeight();
		double z = entityLookX4.z;
		mistPos = entityPos.add(x, y, z);
	}

	/**
	 * Render mist in world.
	 * 
	 * @param world world object.
	 */
	void render(World world) {

		// update position if mist should move away from the invoking entity
		if (!strategy.isStationary()) {
			mistPos = mistPos.add(entityLook);
		}

		try {
			// Get particle position
			BlockPos pos = new BlockPos(mistPos);

			// iterate over rendering info's
			for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {

				// send particle rendering info to client
				ParticleRendering particle = getInstance(pos, info);
				getProxy().getNetworkChannel(world).sendAddParticleRenderingPacket(particle);
			}

			// calculate spiral index
			int spiralCounter = (ticksCounter / PARTICLE_RENDERING_FREQUENCY) % spiralSize;

			// get next spiral coordinate
			BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);

			// calculate ground coordinates
			BlockPos pos2 = pos.add(spiralCoord.getX(), 0, spiralCoord.getZ());

			// iterate over rendering info's
			for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {

				// send particle rendering info to client
				ParticleRendering particle = getInstance(pos2, info);
				getProxy().getNetworkChannel(world).sendAddParticleRenderingPacket(particle);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
