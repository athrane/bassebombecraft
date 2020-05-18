package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.BLOCK_EFFECT_FREQUENCY;
import static bassebombecraft.ModConstants.PARTICLE_RENDERING_FREQUENCY;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;

import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which creates one
 * or more mists with a custom effect when a mist passes a block.
 * 
 * The effect is implemented by the configured strategy
 * {@linkplain BlockMistActionStrategy}.
 */
public class GenericBlockMist implements RightClickedItemAction {

	/**
	 * Spawn distance of mist from invoker. Distance is measured in blocks.
	 */
	static final float INVOCATION_DIST = 4;

	/**
	 * Ticks counter.
	 */
	int ticksCounter = 0;

	/**
	 * Mist positions.
	 */
	Vec3d[] mistPositions;

	/**
	 * Mist directions.
	 */
	Vec3d[] mistDirections;

	/**
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
	BlockMistActionStrategy strategy;

	/**
	 * GenericBlockMist constructor.
	 * 
	 * @param strategy mist strategy.
	 */
	public GenericBlockMist(BlockMistActionStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		this.entity = entity;
		isActive = true;
		ticksCounter = 0;
		initializeMistPostition(world, entity);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		try {

			// exit if mist isn't active
			if (!isActive())
				return;

			// render mist if frequency is active
			FrequencyRepository repository = getProxy().getServerFrequencyRepository();
			if (repository.isActive(PARTICLE_RENDERING_FREQUENCY))
				render(worldIn);

			// update effect if frequency is active
			if (repository.isActive(BLOCK_EFFECT_FREQUENCY))
				applyEffect(worldIn);

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
	 * Initialize mist position.
	 * 
	 * Mist is spawned 2 blocks away from the entity at eye height.
	 * 
	 * @param world  world object.
	 * @param entity entity object
	 */
	void initializeMistPostition(World world, LivingEntity entity) {

		// setup arrays
		mistDirections = new Vec3d[strategy.getNumberMists()];
		mistPositions = new Vec3d[strategy.getNumberMists()];

		// setup entity position and direction
		Vec3d entityLook = entity.getLook(1);
		Vec3d entityPos = new Vec3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());

		// setup offset angle
		double offsetAngle = (strategy.getNumberMists() - 1) * strategy.getMistAngle() * -0.5F;

		// setup mists
		for (int index = 0; index < strategy.getNumberMists(); index++) {
			double angle = offsetAngle + (index * strategy.getMistAngle());

			// rotate entity look vector
			Vec3d rotatedLookVector = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(angle, entityLook);
			mistDirections[index] = rotatedLookVector;

			// calculate spawn vector
			Vec3d entityLookXn = new Vec3d(rotatedLookVector.x * INVOCATION_DIST, rotatedLookVector.y * INVOCATION_DIST,
					rotatedLookVector.z * INVOCATION_DIST);

			double x = entityLookXn.x;
			float y = entity.getEyeHeight();
			double z = entityLookXn.z;

			mistPositions[index] = entityPos.add(x, y, z);
		}
	}

	/**
	 * Apply effect to block.
	 * 
	 * @param world world object
	 */
	void applyEffect(World world) {
		for (Vec3d mistPosition : mistPositions) {
			BlockPos target = new BlockPos(mistPosition);
			strategy.applyEffectToBlock(target, world);
		}
	}

	/**
	 * Render mist in world.
	 * 
	 * @param world world object.
	 */
	void render(World world) {

		// update mist positions
		updateMistPositions();

		// render mists
		for (Vec3d mistPosition : mistPositions) {
			renderMist(mistPosition, world);
		}
	}

	/**
	 * Render a single mist.
	 * 
	 * @param mistPosition position where the mist should is rendered.
	 * @param world world object.
	 */
	void renderMist(Vec3d mistPosition, World world) {
		try {
			// Get particle position
			BlockPos pos = new BlockPos(mistPosition);

			// iterate over rendering info's
			for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {

				// send particle rendering info to client
				ParticleRendering particle = getInstance(pos, info);
				getProxy().getNetworkChannel(world).sendAddParticleRenderingPacket(particle);
			}
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Update mist positions.
	 */
	void updateMistPositions() {
		int index = 0;
		for (Vec3d mistPosition : mistPositions) {
			Vec3d mistDirection = mistDirections[index];
			mistPositions[index] = mistPosition.add(mistDirection);
			index++;
		}
	}

}
