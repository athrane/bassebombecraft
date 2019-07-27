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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build road
 * segments.
 */
public class BuildRoad implements BlockClickedItemAction {

	static final ActionResultType USED_ITEM = ActionResultType.SUCCESS;
	static final ActionResultType DIDNT_USED_ITEM = ActionResultType.PASS;

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks

	private static final int MAX_ROAD_SEGMENTS = 3;
	static final int X_SIZE = 3;
	static final int Y_SIZE = 1;
	static final int Z_SIZE = 10;
	static final int X_OFFSET = -1;
	static final int Y_OFFSET_DOWN = 0;
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
	public BuildRoad() {
		super();
		repository = getBassebombeCraft().getBlockDirectivesRepository();
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (ticksExisted % STATE_UPDATE_FREQUENCY != 0)
			return DIDNT_USED_ITEM;

		// calculate if selected block is a ground block
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();		
		boolean isGroundBlock = isBelowPlayerYPosition(pos.getY(), player);

		// calculate structure
		Structure structure = null;
		if (isGroundBlock)
			structure = createRoad();
		else
			return DIDNT_USED_ITEM;

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
			BlockPos offset = new BlockPos(displacement, Y_OFFSET_DOWN, (Z_SIZE) * index);
			BlockPos size = new BlockPos(X_SIZE, Y_SIZE, Z_SIZE);
			composite.add(new ChildStructure(offset, size, Blocks.STONE_SLAB));

			// air above the path
			offset = new BlockPos(displacement, Y_OFFSET_DOWN + 1, (Z_SIZE) * index);
			size = new BlockPos(X_SIZE + 1, Y_SIZE + 3, Z_SIZE);
			composite.add(new ChildStructure(offset, size, Blocks.AIR));

			// lightpole - right side
			BlockPos offsetLightpole = new BlockPos(offset.getX() - 1, Y_OFFSET_DOWN, offset.getZ());
			BlockPos sizeLightpole = new BlockPos(1, 1, 1);
			composite.add(new ChildStructure(offsetLightpole, sizeLightpole, Blocks.OAK_FENCE));

			// torch
			BlockPos offsetTorch = new BlockPos(offsetLightpole.getX(), offsetLightpole.getY() + sizeLightpole.getY(),
					offsetLightpole.getZ());
			BlockPos sizeTorch = UNITY_BLOCK_SIZE;
			composite.add(new ChildStructure(offsetTorch, sizeTorch, Blocks.TORCH));

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
