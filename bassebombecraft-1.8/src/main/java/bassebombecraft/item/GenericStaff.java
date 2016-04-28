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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
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
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return true;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityTarget, EntityLivingBase entityUser) {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		return apply(pos, playerIn);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
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
	 * @param player
	 */
	boolean apply(BlockPos blockPosition, EntityPlayer player) {

		// get block from block position
		World world = player.getEntityWorld();
		Block block = getBlockFromPosition(blockPosition, world);
				
		final boolean HitLiquids = false;

		// get item position
		MovingObjectPosition pos = getMovingObjectPositionFromPlayer(world, player, HitLiquids);

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
