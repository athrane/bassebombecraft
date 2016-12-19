package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;

import java.util.Random;

import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
	 * Rendering frequency in ticks.
	 */
	static final int RENDERING_FREQUENCY = 5;

	/**
	 * Effect frequency when targeted mob are affect by most. Frequency is
	 * measured in ticks.
	 */
	static final int EFFECT_UPDATE_FREQUENCY = 5; // Measured in ticks

	/**
	 * Spawn distance of mist from invoker. Distance is measured in blocks.
	 */
	static final float INVOCATION_DIST = 4;

	/**
	 * Random generator.
	 */
	static Random random = new Random();

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
	EntityLivingBase entity;

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
	 * Particle repository.
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * GenericBlockMist constructor.
	 * 
	 * @param strategy
	 *            mist strategy.
	 */
	public GenericBlockMist(BlockMistActionStrategy strategy) {
		this.strategy = strategy;
		particleRepository = getBassebombeCraft().getParticleRenderingRepository();
	}

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		this.entity = entity;
		isActive = true;
		ticksCounter = 0;
		initializeMistPostition(world, entity);
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
			applyEffect(worldIn);
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
	 * Initialize mist position.
	 * 
	 * Mist is spawned 2 blocks away from the entity at eye height.
	 * 
	 * @param world
	 *            world object.
	 * @param entity
	 *            entity object
	 */
	void initializeMistPostition(World world, EntityLivingBase entity) {

		// setup arrays
		mistDirections = new Vec3d[strategy.getNumberMists()];
		mistPositions = new Vec3d[strategy.getNumberMists()];

		// setup entity position and direction
		Vec3d entityLook = entity.getLook(1);
		Vec3d entityPos = new Vec3d(entity.posX, entity.posY, entity.posZ);

		// setup offset angle
		double offsetAngle = (strategy.getNumberMists()-1) * strategy.getMistAngle() * -0.5F;

		// setup mists
		for (int index = 0; index < strategy.getNumberMists(); index++) {
			double angle = offsetAngle + (index * strategy.getMistAngle());

			// rotate entity look vector
			Vec3d rotatedLookVector = GeometryUtils.rotateUnitVectorAroundYAxisAtOrigin(angle, entityLook);
			mistDirections[index] = rotatedLookVector;

			// calculate spawn vector
			Vec3d entityLookXn = new Vec3d(rotatedLookVector.xCoord * INVOCATION_DIST,
					rotatedLookVector.yCoord * INVOCATION_DIST, rotatedLookVector.zCoord * INVOCATION_DIST);

			double x = entityLookXn.xCoord;
			float y = entity.getEyeHeight();
			double z = entityLookXn.zCoord;

			mistPositions[index] = entityPos.addVector(x, y, z);
		}
	}

	/**
	 * Apply effect to block.
	 * 
	 * @param world
	 *            world object
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
	 * @param world
	 *            world object.
	 */
	void render(World world) {

		// update mist positions
		updateMistPositions();

		// render mists
		for (Vec3d mistPosition : mistPositions) {
			renderMist(mistPosition);
		}
	}

	/**
	 * Render a single mist.
	 * 
	 * @param mistPosition
	 */
	void renderMist(Vec3d mistPosition) {

		// register particle for rendering
		BlockPos pos = new BlockPos(mistPosition);

		// iterate over rendering info's
		for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {
			ParticleRendering particle = getInstance(pos, info);
			particleRepository.add(particle);
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

	@Override
	public String toString() {
		return super.toString() + ", strategy=" + strategy;
	}

}
