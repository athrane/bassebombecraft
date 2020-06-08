package bassebombecraft.item.action.build;

import static bassebombecraft.ModConstants.DONT_HARVEST;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static bassebombecraft.structure.ChildStructure.createWaterStructure;

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
 * Implementation of the {@linkplain BlockClickedItemAction} which build an
 * abyss..
 */
public class BuildAbyss implements BlockClickedItemAction {

	private static final int HOLE_WIDTH_AND_DEPTH = 5;
	private static final int WATER_HEIGHT = 2;
	private static final int HOLE_HEIGHT = 50;

	static final ActionResultType USED_ITEM = ActionResultType.SUCCESS;
	static final ActionResultType DIDNT_USED_ITEM = ActionResultType.PASS;

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks

	/**
	 * Ticks exists since first marker was set.
	 */
	int ticksExisted = 0;

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (ticksExisted % STATE_UPDATE_FREQUENCY != 0)
			return DIDNT_USED_ITEM;

		// calculate structure
		Structure structure = createStructure();

		// calculate Y offset in structure
		PlayerEntity player = context.getPlayer();
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// calculate set of block directives
		BlockPos pos = context.getPos();
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, player, structure, DONT_HARVEST);

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
	Structure createStructure() {
		CompositeStructure composite = new CompositeStructure();

		int zOffset = -1;
		for (int i = 0; i < HOLE_HEIGHT; i++) {
			int yOffset = -i;
			addLayer(composite, yOffset, zOffset);
		}

		BlockPos offset = new BlockPos(-2, -HOLE_HEIGHT, 0);
		BlockPos size = new BlockPos(HOLE_WIDTH_AND_DEPTH, WATER_HEIGHT, HOLE_WIDTH_AND_DEPTH);
		composite.add(createWaterStructure(offset, size));

		return composite;
	}

	/**
	 * Add layer to the abyss.
	 * 
	 * @param composite structure to which the layer is added.
	 * @param yOffset   y-offset where layer is added.
	 * @param zOffset   z-offset where layer is added.
	 */
	void addLayer(Structure composite, int yOffset, int zOffset) {

		BlockPos offset = new BlockPos(-2, yOffset, zOffset + 1);
		BlockPos size = new BlockPos(HOLE_WIDTH_AND_DEPTH, 1, HOLE_WIDTH_AND_DEPTH);
		composite.add(createAirStructure(offset, size));

		offset = new BlockPos(1 - 2, yOffset, zOffset + 1 + HOLE_WIDTH_AND_DEPTH);
		size = new BlockPos(HOLE_WIDTH_AND_DEPTH - 2, 1, 1);
		composite.add(createAirStructure(offset, size));

		offset = new BlockPos(1 - 2, yOffset, zOffset);
		size = new BlockPos(HOLE_WIDTH_AND_DEPTH - 2, 1, 1);
		composite.add(createAirStructure(offset, size));

		offset = new BlockPos(HOLE_WIDTH_AND_DEPTH - 2, yOffset, zOffset + 2);
		size = new BlockPos(1, 1, HOLE_WIDTH_AND_DEPTH - 2);
		composite.add(createAirStructure(offset, size));

		offset = new BlockPos(-3, yOffset, zOffset + 2);
		size = new BlockPos(1, 1, HOLE_WIDTH_AND_DEPTH - 2);
		composite.add(createAirStructure(offset, size));
	}

}
