package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.UNITY_BLOCK_SIZE;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build a rainbow road.
 */
public class BuildRainbowRoad implements BlockClickedItemAction {

	static final boolean USED_ITEM = true;
	static final boolean DIDNT_USED_ITEM = true;

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks

	private static final int MAX_ROAD_SEGMENTS = 8;
	static final int X_SIZE = 3;
	static final int Y_SIZE = 1;
	static final int Z_SIZE = 8;
	static final int X_OFFSET = -1;
	static final int Y_OFFSET_DOWN = -1;
	static final Structure NULL_STRUCTURE = new CompositeStructure();

	/**
	 * Random generator.
	 */
	Random random = new Random();

	/**
	 * Ticks exists since first marker was set.
	 */
	int ticksExisted = 0;

	/**
	 * Process block directives repository.
	 */
	BlockDirectivesRepository repository;

	/**
	 * CreateRoad constructor.
	 */
	public BuildRainbowRoad() {
		super();
		repository = getBassebombeCraft().getBlockDirectivesRepository();
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {

		if (ticksExisted % STATE_UPDATE_FREQUENCY != 0)
			return DIDNT_USED_ITEM;

		// calculate if selected block is a ground block
		boolean isGroundBlock = isBelowPlayerYPosition(pos.getY(), playerIn);

		// calculate structure
		Structure structure = null;
		if (isGroundBlock)
			structure = createRoad();
		else
			return DIDNT_USED_ITEM;

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(playerIn);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(playerIn);

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
	 * Create structure.
	 * 
	 * @return created structure.
	 */
	Structure createRoad() {
		CompositeStructure composite = new CompositeStructure();
		int displacement = 0; // set by the first calculate displacement
		for (int index = 0; index < MAX_ROAD_SEGMENTS; index++) {
			displacement = calculateDisplacement(index, displacement);

			// create path
			BlockPos offset = new BlockPos(displacement, Y_OFFSET_DOWN, Z_SIZE * index);
			BlockPos size = new BlockPos(X_SIZE, Y_SIZE, 1);
			IBlockState state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.MAGENTA);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));
			
			offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE * index)+1);
			size = new BlockPos(X_SIZE, Y_SIZE, 1);
			state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PURPLE);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));

			offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE * index)+2);
			size = new BlockPos(X_SIZE, Y_SIZE, 1);
			state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));

			offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE * index)+3);
			size = new BlockPos(X_SIZE, Y_SIZE, 1);
			state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));

			offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE * index)+4);
			size = new BlockPos(X_SIZE, Y_SIZE, 1);
			state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIME);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));
			
			offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE * index)+5);
			size = new BlockPos(X_SIZE, Y_SIZE, 1);
			state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));

			offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE * index)+6);
			size = new BlockPos(X_SIZE, Y_SIZE, 1);
			state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));

			offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE * index)+7);
			size = new BlockPos(X_SIZE, Y_SIZE, 1);
			state = Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
			composite.add(new ChildStructure(offset, size, Blocks.wool, state));
			
			// air above the path
			offset = new BlockPos(displacement, Y_OFFSET_DOWN + 1, (Z_SIZE) * index);
			size = new BlockPos(X_SIZE + 1, Y_SIZE + 3, Z_SIZE);
			composite.add(new ChildStructure(offset, size, Blocks.air));

			// lightpole - right side
			BlockPos offsetLightpole = new BlockPos(offset.getX() - 1, Y_OFFSET_DOWN, offset.getZ());
			BlockPos sizeLightpole = new BlockPos(1, 1, 1);
			composite.add(new ChildStructure(offsetLightpole, sizeLightpole, Blocks.oak_fence));

			// torch
			BlockPos offsetTorch = new BlockPos(offsetLightpole.getX(), offsetLightpole.getY() + sizeLightpole.getY(),
					offsetLightpole.getZ());
			BlockPos sizeTorch = UNITY_BLOCK_SIZE;
			composite.add(new ChildStructure(offsetTorch, sizeTorch, Blocks.torch));

		}

		return composite;
	}

	/**
	 * Calculate road displacement.
	 * 
	 * @param index
	 * @param displacement
	 * @return
	 */
	int calculateDisplacement(int index, int displacement) {
		if (index == 0)
			return X_OFFSET;
		int displacementRandom = random.nextInt(3);
		if (displacementRandom == 1)
			return displacement + 1;
		if (displacementRandom == 2)
			return displacement - 1;
		return displacement;
	}

}
