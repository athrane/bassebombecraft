package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.MODID;

import bassebombecraft.item.action.BlockClickedItemAction;
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
import net.minecraft.world.World;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when a block is right clicked wih this item.
 */
public class GenericBlockClickedBook extends Item {

	/**
	 * Item action.
	 */
	BlockClickedItemAction action;

	/**
	 * Generic book constructor.
	 * 
	 * @param name
	 *            item name.
	 * @param action
	 *            item action object which is invoked when item is right clicked
	 *            with this item.
	 */
	public GenericBlockClickedBook(String name, BlockClickedItemAction action) {
		setUnlocalizedName(name);
		this.action = action;
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
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityTarget, EntityLivingBase entityUser) {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		
		// only apply the action at server side since we updates the world
		if (isWorldAtClientSide(worldIn))
			return true;
		
		return action.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
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

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		action.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	/**
	 * return true if world is located at client side.
	 * 
	 * @return true if world is located at client side.
	 */
	boolean isWorldAtClientSide(World world) {
		return world.isRemote;
	}

}
