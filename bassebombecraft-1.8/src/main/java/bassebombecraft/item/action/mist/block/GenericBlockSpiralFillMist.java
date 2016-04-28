package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRendering.getInstance;

import java.util.List;
import java.util.Random;

import bassebombecraft.block.BlockUtils;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.geom.GeometryUtils;
import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which creates one
 * or more mists with a custom effect when a mist passes a block.
 * 
 * The effect is implemented by the configured strategy
 * {@linkplain BlockMistActionStrategy}.
 */
public class GenericBlockSpiralFillMist implements RightClickedItemAction {

	/**
	 * Rendering frequency in ticks.
	 */
	static final int RENDERING_FREQUENCY = 5;

	/**
	 * Effect frequency when targeted mob are affect by most. Frequency is
	 * measured in ticks.
	 */
	static final int EFFECT_UPDATE_FREQUENCY = 5; 

	/**
	 * Spawn distance of mist from invoker. Distance is measured in blocks.
	 */
	static final float INVOCATION_DIST = 4;

	/**
	 * Spiral size.
	 */
	static final int SPIRAL_SIZE = 20;
	
	
	/**
	 * Ticks counter.
	 */
	int ticksCounter = 0;

	/**
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
	BlockMistActionStrategy strategy;

	/**
	 * Particle repository
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

	/**
	 * Spiral counter.
	 */
	int spiralCounter;

	/**
	 * Global centre of the spiral.
	 */
	BlockPos spiralCenter;

	/**
	 * Current position in the mist.
	 */
	private BlockPos mistPosition;

	/**
	 * GenericBlockMist constructor.
	 * 
	 * @param strategy
	 *            mist strategy.
	 */
	public GenericBlockSpiralFillMist(BlockMistActionStrategy strategy) {
		this.strategy = strategy;
		particleRepository = getBassebombeCraft().getParticleRenderingRepository();
		
		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(SPIRAL_SIZE, SPIRAL_SIZE);		
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
	 * Mist is calculated as a spiral.
	 * 
	 * @param world
	 *            world object.
	 * @param entity
	 *            entity object
	 */
	void initializeMistPostition(World world, EntityLivingBase entity) {
		spiralCounter = strategy.getSpiralOffset();
		spiralCenter = new BlockPos(entity);	
 	}

	/**
	 * Apply effect to block.
	 * 
	 * @param world
	 *            world object
	 */
	void applyEffect(World world) {
		strategy.applyEffectToBlock(mistPosition, world);
	}
	
	/**
	 * Render mist in world.
	 * 
	 * @param world
	 *            world object.
	 */
	void render(World world) {
		updateMistPosition(world);		

		// render mists
		// iterate over rendering info's		
		for (ParticleRenderingInfo info : strategy.getRenderingInfos()) {
			ParticleRendering particle = getInstance(mistPosition, info);
			particleRepository.add(particle);
		}
	}

	/**
	 * Update mist positions.
	 * 
	 * @param world
	 *            world object.
	 */
	void updateMistPosition(World world) {
		
		// exit if entire spiral is processed
		if (spiralCounter >= spiralCoordinates.size()) return;
		
		// get next spiral coordinate
		BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);
		
		// calculate ground coordinates
		int x = spiralCenter.getX() + spiralCoord.getX();
		int y = spiralCenter.getY();
		int z = spiralCenter.getZ() + spiralCoord.getZ();
		BlockPos groundCandidate = new BlockPos(x,y,z);
		
		// locate ground block
		mistPosition = locateGroundBlockPos(groundCandidate, world);
		
		spiralCounter++;
	}

	/**
	 * Locate ground block, i.e. a block with air/water above and solid ground
	 * below.
	 * 
	 * If block is air/water block then move down until ground block is located.
	 * If block is ground block then move up until block above air/water.
	 * 
	 * @param target
	 *            block to process.
	 * @param world
	 *            world object.
	 * @return ground block, i.e. a block with air/water above and solid ground
	 *         below.
	 */
	BlockPos locateGroundBlockPos(BlockPos target, World world) {
		
		// if upper block isn't useful - then move up
		if (!isUsefullAirTypeBlock(target.up(), world)) {
			//System.out.println("upper isn't useful air block - go up.");
			return locateGroundBlockPos(target.up(), world);			
		}

		// if upper is OK - but current isn't - then move down
		if (!isUsefulGroundBlock(target, world)) {
			//System.out.println("useful air block - not useful ground block - go down.");			
			return locateGroundBlockPos(target.down(), world);			
		}

		return target;
	}

	boolean isUsefulGroundBlock(BlockPos target, World world) {
		Block candidateBlock = BlockUtils.getBlockFromPosition(target, world);
		if(candidateBlock.getMaterial().isLiquid()) return false;
		//System.out.println("isn't liquid.");					
		if(candidateBlock.getMaterial() == Material.air) return false;
		//System.out.println("isn't air.");					
		return true;
	}

	boolean isUsefullAirTypeBlock(BlockPos target, World world) {
		Block candidateBlock = BlockUtils.getBlockFromPosition(target, world);
		if(candidateBlock.getMaterial().isLiquid()) return true;
		if(candidateBlock.getMaterial() == Material.air) return true;
		if(candidateBlock.getMaterial() == Material.plants) return true;
		if(candidateBlock.getMaterial() == Material.grass) return true;
		
		return false;
	}	
	@Override
	public String toString() {
		return super.toString() + ", strategy=" + strategy;
	}

}
