package bassebombecraft.item.book;

import static bassebombecraft.ModConstants.ITEM_BOOK_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.config.ConfigUtils.resolveCoolDown;
import static bassebombecraft.config.VersionUtils.postItemUsage;
import static bassebombecraft.world.WorldUtils.isWorldAtClientSide;

import bassebombecraft.item.action.BlockClickedItemAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Generic Book implementation.
 * 
 * The action object is applied when a block is right clicked with this item.
 */
public class GenericBlockClickedBook extends Item {

	/**
	 * Item action.
	 */
	BlockClickedItemAction action;

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
	 *            item action object which is invoked when item is right clicked
	 *            with this item.
	 */
	public GenericBlockClickedBook(String name, BlockClickedItemAction action) {
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		// exit if invoked at client side
		if (isWorldAtClientSide(worldIn)) {
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}

		// post analytics
		postItemUsage(this.getUnlocalizedName());

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		return action.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		action.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

}
