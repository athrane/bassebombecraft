package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.BLOCK_EFFECT_FREQUENCY;
import static bassebombecraft.ModConstants.PARTICLE_SPAWN_FREQUENCY;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;
import static bassebombecraft.player.PlayerUtils.hasIdenticalUniqueID;

import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.action.RightClickedItemAction;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

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
	Vec3 mistPos;

	/*
	 * Invoking entity.
	 */
	LivingEntity entity;

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
	 * Spiral size.
	 */
	int spiralSize;

	/**
	 * Particle rendering ports.
	 * 
	 * The ports is defined as a field to reuse it across update ticks.
	 */
	Ports addParticlesPorts;

	/**
	 * Client side particle rendering operator.
	 */
	Operator2 addParticlesOp;

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
	public void onRightClick(Level world, LivingEntity entity) {
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
	void applyEffect(Level world, LivingEntity invokingEntity) {
		int aoeRange = strategy.getEffectRange();

		// get entities within AABB
		AABB aabb = new AABB(mistPos.x - aoeRange, mistPos.y - aoeRange, mistPos.z - aoeRange,
				mistPos.x + aoeRange, mistPos.y + aoeRange, mistPos.z + aoeRange);
		List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, aabb);

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
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		try {

			// exit if mist isn't active
			if (!isActive())
				return;

			// render mist
			render();

			// update effect if frequency is active
			FrequencyRepository repository = getProxy().getServerFrequencyRepository();
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

	void calculateMistPostition(Level world, LivingEntity entity) {
		entityLook = entity.getViewVector(1);
		Vec3 entityLookX4 = new Vec3(entityLook.x * INVOCATION_DIST, entityLook.y * INVOCATION_DIST,
				entityLook.z * INVOCATION_DIST);
		Vec3 entityPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
		double x = entityLookX4.x;
		float y = entity.getEyeHeight();
		double z = entityLookX4.z;
		mistPos = entityPos.add(x, y, z);
	}

	/**
	 * Render mist in world.
	 */
	void render() {

		// update position if mist should move away from the invoking entity
		if (!strategy.isStationary()) {
			mistPos = mistPos.add(entityLook);
		}

		try {
			// Get particle position
			BlockPos pos = new BlockPos(mistPos);

			// send particle rendering info to client
			ParticleRendering particle = getInstance(pos, strategy.getRenderingInfos());
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);

			// calculate spiral index
			int spiralCounter = (ticksCounter / PARTICLE_SPAWN_FREQUENCY) % spiralSize;

			// get next spiral coordinate
			BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);

			// calculate ground coordinates
			BlockPos pos2 = pos.offset(spiralCoord.getX(), 0, spiralCoord.getZ());

			// send particle rendering info to client
			particle = getInstance(pos, strategy.getRenderingInfos());
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
