package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.HARVEST;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;
import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static bassebombecraft.structure.CompositeStructure.getInstance;

import java.util.List;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
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
 * Implementation of the {@linkplain BlockClickedItemAction} which digs a small
 * hole.
 */
public class BuildSmallHole implements BlockClickedItemAction {

	static final ActionResultType USED_ITEM = ActionResultType.SUCCESS;
	static final ActionResultType DIDNT_USED_ITEM = ActionResultType.PASS;

	/**
	 * Offset for horizontal hole.
	 */
	static final BlockPos HORIZONTAL_OFFSET = new BlockPos(-1, -1, -1);

	/**
	 * Size for horizontal hole.
	 */
	static final BlockPos HORIZONTAL_SIZE = new BlockPos(3, 1, 3);

	/**
	 * Offset for vertical hole.
	 */
	static final BlockPos VERTICAL_OFFSET = new BlockPos(-1, 0, 0);

	/**
	 * Size for vertical hole.
	 */
	static final BlockPos VERTICAL_SIZE = new BlockPos(3, 3, 1);

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {

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
	 * Create horizontal structure.
	 * 
	 * @return created structure.
	 */
	Structure createHorizontalStructure() {
		CompositeStructure composite = getInstance();
		composite.add(createAirStructure(HORIZONTAL_OFFSET, HORIZONTAL_SIZE));
		return composite;
	}

	/**
	 * Create vertical structure.
	 * 
	 * @return created structure.
	 */
	Structure createVerticalStructure() {
		CompositeStructure composite = getInstance();
		composite.add(createAirStructure(VERTICAL_OFFSET, VERTICAL_SIZE));
		return composite;
	}

}
