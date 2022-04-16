package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.BLOCK_EFFECT_FREQUENCY;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;

import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

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
	Vec3[] mistPositions;

	/**
	 * Mist directions.
	 */
	Vec3[] mistDirections;

	/**
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
	public void onRightClick(Level world, LivingEntity entity) {
		try {
			this.entity = entity;
			isActive = true;
			ticksCounter = 0;
			initializeMistPostition(world, entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		try {

			// exit if mist isn't active
			if (!isActive())
				return;

			// render mist
			render(worldIn);

			// update effect if frequency is active
			FrequencyRepository repository = getProxy().getServerFrequencyRepository();
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
	void initializeMistPostition(Level world, LivingEntity entity) {

		// setup arrays
		mistDirections = new Vec3[strategy.getNumberMists()];
		mistPositions = new Vec3[strategy.getNumberMists()];

		// setup entity position and direction
		Vec3 entityLook = entity.getViewVector(1);
		Vec3 entityPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());

		// setup offset angle
		double offsetAngle = (strategy.getNumberMists() - 1) * strategy.getMistAngle() * -0.5F;

		// setup mists
		for (int index = 0; index < strategy.getNumberMists(); index++) {
			double angle = offsetAngle + (index * strategy.getMistAngle());

			// rotate entity look vector
			Vec3 rotatedLookVector = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(angle, entityLook);
			mistDirections[index] = rotatedLookVector;

			// calculate spawn vector
			Vec3 entityLookXn = new Vec3(rotatedLookVector.x * INVOCATION_DIST, rotatedLookVector.y * INVOCATION_DIST,
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
	void applyEffect(Level world) {
		for (Vec3 mistPosition : mistPositions) {
			BlockPos target = new BlockPos(mistPosition);
			strategy.applyEffectToBlock(target, world);
		}
	}

	/**
	 * Render mist in world.
	 * 
	 * @param world world object.
	 */
	void render(Level world) {

		// update mist positions
		updateMistPositions();

		// render mists
		for (Vec3 mistPosition : mistPositions) {
			renderMist(mistPosition);
		}
	}

	/**
	 * Render a single mist.
	 * 
	 * @param mistPosition position where the mist should is rendered.
	 */
	void renderMist(Vec3 mistPosition) {
		try {
			// Get particle position
			BlockPos pos = new BlockPos(mistPosition);

			// send particle rendering info to client
			ParticleRendering particle = getInstance(pos, strategy.getRenderingInfo());
			getProxy().getNetworkChannel().sendAddParticleRenderingPacket(particle);
			
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Update mist positions.
	 */
	void updateMistPositions() {
		int index = 0;
		for (Vec3 mistPosition : mistPositions) {
			Vec3 mistDirection = mistDirections[index];
			mistPositions[index] = mistPosition.add(mistDirection);
			index++;
		}
	}

}
