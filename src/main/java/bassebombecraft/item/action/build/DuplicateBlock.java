package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.HARVEST;
import static bassebombecraft.block.BlockUtils.getBlockFromPosition;
import static bassebombecraft.block.BlockUtils.getBlockStateFromPosition;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.geom.GeometryUtils.calculateYOffsetFromBlock;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import bassebombecraft.geom.WorldQueryImpl;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which duplicate a
 * block into 4x copies.
 */
public class DuplicateBlock implements BlockClickedItemAction {

	static final ActionResultType USED_ITEM = ActionResultType.SUCCESS;
	static final ActionResultType DIDNT_USED_ITEM = ActionResultType.PASS;

	static final int X_SIZE = 3;
	static final int Y_SIZE = 1;
	static final int Z_SIZE = 10;
	static final int X_OFFSET = -1;
	static final int Y_OFFSET_DOWN = 0;
	static final Structure NULL_STRUCTURE = new CompositeStructure();

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {

		// create world query
		PlayerEntity player = context.getPlayer();
		BlockPos pos = context.getPos();
		WorldQueryImpl worldQuery = new WorldQueryImpl(player, pos);

		// get world
		World world = context.getWorld();

		// calculate structure
		Block sourceBlock = getBlockFromPosition(worldQuery.getTargetBlockPosition(), world);
		Structure structure = createDuplicatedBlock(sourceBlock, worldQuery);

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// calculate set of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, player, structure, HARVEST);

		// add directives
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.addAll(directives);

		return USED_ITEM;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

	/**
	 * Create structure.
	 * 
	 * @return created structure.
	 */
	Structure createDuplicatedBlock(Block sourceBlock, WorldQuery worldQuery) {
		CompositeStructure composite = new CompositeStructure();

		// calculate offset from selected block
		BlockPos blockPosition = worldQuery.getTargetBlockPosition();
		int yOffset = calculateYOffsetFromBlock(worldQuery.getPlayer(), blockPosition);

		// calculate block
		BlockPos offset = new BlockPos(-1, yOffset, 0);
		BlockPos size = new BlockPos(2, 2, 2);

		// create TNT variant
		if (createTntVariant()) {
			composite.add(new ChildStructure(offset, size, Blocks.TNT));

			// add red stone for TNT
			offset = new BlockPos(0, yOffset + 2, 0);
			size = new BlockPos(1, 1, 1);
			composite.add(new ChildStructure(offset, size, Blocks.REDSTONE_BLOCK));
			return composite;
		}

		// create regular variant
		BlockState blockState = getBlockStateFromPosition(blockPosition, worldQuery.getWorld());
		composite.add(new ChildStructure(offset, size, sourceBlock, blockState));
		return composite;
	}

	/**
	 * Returns true if TNT variant of duplicated block should be created.
	 * 
	 * @return true if TNT variant of duplicated block should be created.
	 */
	boolean createTntVariant() {
		Random random = getBassebombeCraft().getRandom();
		int randomValue = random.nextInt(25);
		return (randomValue == 0);
	}

}
