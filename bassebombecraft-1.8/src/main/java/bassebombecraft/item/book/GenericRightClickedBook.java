package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.ITEM_BOOK_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.config.ConfigUtils.resolveCoolDown;
import static bassebombecraft.config.VersionUtils.postItemUsage;

import bassebombecraft.item.action.RightClickedItemAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when the item is right clicked.
 */
public class GenericRightClickedBook extends Item {

	/**
	 * Item action.
	 */
	RightClickedItemAction action;

	/**
	 * Book item cooldown value.
	 */
	int coolDown;

	/**
	 * Generic book constructor.
	 * 
	 * @param name
	 *            item name.
	 * @param action
	 *            item action object which is invoked when item is right
	 *            clicked.
	 */
	public GenericRightClickedBook(String name, RightClickedItemAction action) {
		setUnlocalizedName(name);
		this.action = action;
		registerForRendering(this);

		// get cooldown or default value
		coolDown = resolveCoolDown(name, ITEM_BOOK_DEFAULT_COOLDOWN);
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		postItemUsage(this.getUnlocalizedName());

		// add cooldown
		CooldownTracker tracker = playerIn.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// apply action
		action.onRightClick(worldIn, playerIn);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
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
