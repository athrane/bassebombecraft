package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;
import static bassebombecraft.structure.ChildStructure.createAirStructure;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.player.PlayerDirection;
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
 * Implementation of the {@linkplain BlockClickedItemAction} which dig a small
 * hole.
 */
public class BuildSmallHole implements BlockClickedItemAction {

	static final ActionResultType USED_ITEM = ActionResultType.SUCCESS;
	static final ActionResultType DIDNT_USED_ITEM = ActionResultType.PASS;

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks

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
	 * BuildSmallHole constructor.
	 */
	public BuildSmallHole() {
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

		BlockPos offset = new BlockPos(-1, -1, -1);
		BlockPos size = new BlockPos(3, 1, 3);
		composite.add(createAirStructure(offset, size));

		return composite;
	}

	/**
	 * Create vertical structure.
	 * 
	 * @return created structure.
	 */
	Structure createVerticalStructure() {
		CompositeStructure composite = new CompositeStructure();

		BlockPos offset = new BlockPos(-1, 0, 0);
		BlockPos size = new BlockPos(3, 3, 1);
		composite.add(createAirStructure(offset, size));

		return composite;
	}

}
