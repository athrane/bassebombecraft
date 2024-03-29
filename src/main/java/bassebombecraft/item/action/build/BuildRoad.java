package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.*;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.ModConstants.UNITY_BLOCK_SIZE;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build road
 * segments.
 */
public class BuildRoad implements BlockClickedItemAction {

	static final InteractionResult USED_ITEM = InteractionResult.SUCCESS;
	static final InteractionResult DIDNT_USED_ITEM = InteractionResult.PASS;

	private static final int MAX_ROAD_SEGMENTS = 3;
	static final int X_SIZE = 3;
	static final int Y_SIZE = 1;
	static final int Z_SIZE = 10;
	static final int X_OFFSET = -1;
	static final int Y_OFFSET_DOWN = 0;
	static final Structure NULL_STRUCTURE = new CompositeStructure();

	@Override
	public InteractionResult onItemUse(UseOnContext context) {

		// calculate if selected block is a ground block
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		boolean isGroundBlock = isBelowPlayerYPosition(pos.getY(), player);

		// calculate structure
		Structure structure = null;
		if (isGroundBlock)
			structure = createRoad();
		else
			return DIDNT_USED_ITEM;

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// calculate set of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, player, structure, DONT_HARVEST);

		// add directives
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.addAll(directives);

		return USED_ITEM;
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
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
		Random random = getBassebombeCraft().getRandom();
		int displacementRandom = random.nextInt(3);
		if (displacementRandom == 1)
			return displacement + 1;
		if (displacementRandom == 2)
			return displacement - 1;
		return displacement;
	}

}
