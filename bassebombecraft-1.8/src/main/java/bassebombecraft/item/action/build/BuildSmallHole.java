package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;
import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static bassebombecraft.structure.ChildStructure.createIceStructure;
import static bassebombecraft.structure.ChildStructure.createWaterStructure;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which 
 * dig a small hole.
 */
public class BuildSmallHole implements BlockClickedItemAction {

	static final boolean USED_ITEM = true;
	static final boolean DIDNT_USED_ITEM = false;

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
	 * CreateRoad constructor.
	 */
	public BuildSmallHole() {
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
			structure = createHorizontalStructure();
		else
			structure = createVerticalStructure();
		
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
