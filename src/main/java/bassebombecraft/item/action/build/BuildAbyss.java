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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build an
 * abyss..
 */
public class BuildAbyss implements BlockClickedItemAction {

	private static final int HOLE_WIDTH_AND_DEPTH = 5;
	private static final int WATER_HEIGHT = 2;
	private static final int HOLE_HEIGHT = 50;

	static final InteractionResult USED_ITEM = InteractionResult.SUCCESS;
	static final InteractionResult DIDNT_USED_ITEM = InteractionResult.PASS;

	/**
	 * Ticks exists since first marker was set.
	 */
	int ticksExisted = 0;

	@Override
	public InteractionResult onItemUse(UseOnContext context) {

		// calculate structure
		Structure structure = createStructure();

		// calculate Y offset in structure
		Player player = context.getPlayer();
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// calculate set of block directives
		BlockPos pos = context.getClickedPos();
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
