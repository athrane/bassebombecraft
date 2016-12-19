package bassebombecraft.item;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.block.BlockUtils.createBlock;
import static bassebombecraft.block.BlockUtils.getBlockFromPosition;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;

import java.util.List;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQueryImpl;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.Structure;
import bassebombecraft.structure.StructureFactory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Generic staff implementation.
 */
public class GenericStaff extends Item {

	private StructureFactory structureFactory;

	public GenericStaff(String itemName, StructureFactory factory) {
		setUnlocalizedName(itemName);
		this.structureFactory = factory;
		registerForRendering(this);
	}

	/**
	 * Register item for rendering.
	 * 
	 * @param item
	 *            item to be registered.
	 */
	void registerForRendering(Item item) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation location;
		location = new ModelResourceLocation(MODID + ":" + getUnlocalizedName().substring(5), "inventory");
		renderItem.getItemModelMesher().register(item, 0, location);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return true;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityTarget, EntityLivingBase entityUser) {
		return false;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (apply(pos, player)) return EnumActionResult.SUCCESS;
		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos blockPosition, EntityPlayer player) {
		return super.onBlockStartBreak(itemstack, blockPosition, player);
	}

	/**
	 * Apply item logic.
	 * 
	 * @param blockPosition
	 *            block position.
	 * @param player
	 *            player.
	 */
	boolean apply(BlockPos blockPosition, EntityPlayer player) {

		// get block from block position
		World world = player.getEntityWorld();
		Block block = getBlockFromPosition(blockPosition, world);

		final boolean HitLiquids = false;

		// get item position
		RayTraceResult pos = this.rayTrace(world, player, HitLiquids);

		// validate item hit block
		if (pos == null)
			return false;
		if (pos.getBlockPos().getX() != blockPosition.getX())
			return false;
		if (pos.getBlockPos().getY() != blockPosition.getY())
			return false;
		if (pos.getBlockPos().getZ() != blockPosition.getZ())
			return false;

		// calculate if selected block is a ground block
		boolean isGroundBlock = isBelowPlayerYPosition(blockPosition.getY(), player);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(player);

		// create world query
		WorldQueryImpl worldQuery = new WorldQueryImpl(player, blockPosition);

		// create structure
		Structure structure = structureFactory.getInstance(isGroundBlock, block, worldQuery);

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// calculate set of block directives
		BlockPos offset = new BlockPos(blockPosition.getX(), yOffset, blockPosition.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, structure);

		// process directives
		for (BlockDirective blockDirective : directives) {
			createBlock(blockDirective, worldQuery);
		}
		
		return true;
	}

}
