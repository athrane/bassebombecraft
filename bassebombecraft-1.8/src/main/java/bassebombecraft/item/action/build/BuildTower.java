package bassebombecraft.item.action.build;

import static bassebombecraft.item.action.build.BuildUtils.*;
import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;
import static bassebombecraft.structure.ChildStructure.*;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build a
 * tower.
 */
public class BuildTower implements BlockClickedItemAction {

	static final EnumActionResult USED_ITEM = EnumActionResult.SUCCESS;
	static final EnumActionResult DIDNT_USED_ITEM = EnumActionResult.PASS;

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks

	/**
	 * Random generator.
	 */
	Random random = new Random(1);

	/**
	 * Process block directives repository.
	 */
	BlockDirectivesRepository repository;
	
	/**
	 * Minimum size reduction per layer.
	 */
	int minSizeReductionPerLayer;

	/**
	 * Maximum size reduction per layer.
	 */	
	int maxSizeReductionPerLayer;
	
	/**
	 * Number of layer in the tower.
	 */
	int numberLayers;

	/**
	 * Room height in blocks.
	 */
	int roomHeight;
	
	/** 
	 * floor width.
	 */
	int floorWidth;
	
	/** 
	 * floor height.
	 */	
	int floorDepth;
	
	/**
	 * Stairs material.
	 */
	StairsMaterial stairsMaterial;	
	
	/**
	 * BuildSmallHole constructor.
	 */
	public BuildTower() {
		super();
		repository = getBassebombeCraft().getBlockDirectivesRepository();
		
		floorWidth = 20;
		floorDepth = 20;			
		minSizeReductionPerLayer = 2;
		maxSizeReductionPerLayer = 4;
		numberLayers = 2;
		roomHeight = 8;
		
		IBlockState state = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING,
				EnumFacing.SOUTH);
		stairsMaterial = createInstance(state, Blocks.STONE_BRICK_STAIRS, Blocks.STONEBRICK);			
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		// calculate if selected block is a ground block
		boolean isGroundBlock = isBelowPlayerYPosition(pos.getY(), player);

		// calculate structure
		Structure structure = null;
		if (isGroundBlock)
			structure = createHorizontalStructure();
		else
			structure = createVerticalStructure();

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(player);

		// calculate set of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, structure);

		// add directives
		repository.addAll(directives);

		return USED_ITEM;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	}

	/**
	 * Create horizontal structure.
	 * 
	 * @return created structure.
	 */
	Structure createHorizontalStructure() {
		CompositeStructure composite = new CompositeStructure();

		BlockPos offset = new BlockPos(0, 0, 0);
		int currentFloorWidth = this.floorWidth;
		int currentFloorDepth = this.floorDepth;
		
		for (int layer = 0; layer < numberLayers; layer++) {
			int height = roomHeight;
			
			// calculate layer size
			currentFloorWidth = calculateLayerSize(currentFloorWidth);
			currentFloorDepth = calculateLayerSize(currentFloorDepth) ;			
			System.out.println("currentFloorWidth="+currentFloorWidth);
			System.out.println("currentFloorDepth ="+currentFloorDepth );
			
			// calculate floor center
			int floorWidthDiv2 = currentFloorWidth / 2;
			int floorWidthDiv4 = currentFloorWidth / 4;
			int floorXCenter = floorWidthDiv4 + random.nextInt(floorWidthDiv2);

			int floorDepthDiv2 = currentFloorDepth / 2;
			int floorDepthDiv4 = currentFloorDepth / 4;
			int floorZCenter = floorDepthDiv4 + random.nextInt(floorDepthDiv2);

			
			System.out.println("floorWidthDiv2="+floorWidthDiv2);
			System.out.println("floorWidthDiv4 ="+floorWidthDiv4 );			
			System.out.println("floorXCenter="+floorXCenter);
			
			System.out.println("floorDepthDiv2="+floorDepthDiv2);
			System.out.println("floorDepthDiv4 ="+floorDepthDiv4 );			
			System.out.println("floorZCenter="+floorZCenter);
						
			// setup room #1
			BlockPos room1Offset = new BlockPos(0, offset.getY(), 0);
			BlockPos room1Size = new BlockPos(floorXCenter, height, floorZCenter);
			Block material = selectMaterial();
			composite.add(createRoom(room1Offset, room1Size, material));

			// add door to room #1 in layer #1
			if(layer == 0 ) {
				BlockPos doorOffset = new BlockPos(floorXCenter - floorWidthDiv4, 0, 0);				
				addOakFencedDoorEntryFront(composite, doorOffset);
			}
						
			// setup room #2
			BlockPos room2Offset = new BlockPos(floorXCenter, offset.getY(), 0);
			BlockPos room2Size = new BlockPos(currentFloorWidth - floorXCenter, height, floorZCenter);
			material = selectMaterial();
			composite.add(createRoom(room2Offset, room2Size, material));

			// setup room #3
			BlockPos room3Offset = new BlockPos(0, offset.getY(), floorZCenter);
			BlockPos room3Size = new BlockPos(floorXCenter, height, currentFloorDepth - floorZCenter);
			material = selectMaterial();
			composite.add(createRoom(room3Offset, room3Size, material));
						
			// setup room #4
			BlockPos room4Offset = new BlockPos(floorXCenter, offset.getY(), floorZCenter);
			BlockPos room4Size = new BlockPos(currentFloorWidth - floorXCenter, height, currentFloorDepth - floorZCenter);
			material = selectMaterial();
			composite.add(createRoom(room4Offset, room4Size, material));

			// add stair up in room 3 or 1
			BlockPos stairOffset = new BlockPos(1,0,2);							
			addSolidStairUp(roomHeight, stairsMaterial, composite, stairOffset);
						
			// calculate offset etc for next iteration
			offset = new BlockPos(0, offset.getY() + height, 0);
		}

		return composite;
	}

	/**
	 * Create vertical structure.
	 * 
	 * @return created structure.
	 */
	Structure createVerticalStructure() {
		CompositeStructure composite = new CompositeStructure();
		return composite;
		// NO-OP
	}

	Structure createRoom(BlockPos offset, BlockPos size, Block material) {
		CompositeStructure composite = new CompositeStructure();

		int height = size.getY();
		int width = size.getX();
		int depth = size.getZ();
		int xoffset = offset.getX();
		int yoffset = offset.getY();
		int zoffset = offset.getZ();
		BlockPos roomOffset = new BlockPos(xoffset, yoffset, zoffset);
		BlockPos roomSize = new BlockPos(width, height, depth);
		composite.add(new ChildStructure(roomOffset, roomSize, material));

		height = size.getY() - 2;
		width = size.getX() - 2;
		depth = size.getZ() - 2;
		xoffset = offset.getX() + 1;
		yoffset = offset.getY() + 1;
		zoffset = zoffset + 1;
		roomOffset = new BlockPos(xoffset, yoffset, zoffset);
		roomSize = new BlockPos(width, height, depth);
		composite.add(createAirStructure(roomOffset, roomSize));

		return composite;
	}

	/**
	 * Select random material.
	 * 
	 * @return random material
	 */
	Block selectMaterial() {
	return Blocks.SANDSTONE;
	
		/**
		int selection = random.nextInt(9);
		switch(selection) {

			case 0: return Blocks.BEDROCK;
			case 1: return Blocks.BRICK_BLOCK;
			case 2: return Blocks.NETHER_BRICK;
			case 3: return Blocks.SANDSTONE;
			case 4: return Blocks.STONE;
			case 5: return Blocks.STONEBRICK;
			case 6: return Blocks.MOSSY_COBBLESTONE;
			case 7: return Blocks.COBBLESTONE;
			default: return Blocks.OBSIDIAN;
		}
		**/
	}

	/**
	 * Calculate layer size in one dimension.
	 * 
	 * @param currentSize
	 * 
	 * @return layer size in one dimension
	 */
	int calculateLayerSize(int currentSize) {
		int delta = minSizeReductionPerLayer + random.nextInt(maxSizeReductionPerLayer - minSizeReductionPerLayer);
		return currentSize - delta;
	}
	
}
