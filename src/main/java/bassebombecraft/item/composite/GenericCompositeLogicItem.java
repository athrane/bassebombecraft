package bassebombecraft.item.composite;

import static bassebombecraft.BassebombeCraft.getItemGroup;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.item.ItemUtils.doCommonItemInitialization;
import static bassebombecraft.operator.Operators2.run;
import static bassebombecraft.world.WorldUtils.isLogicalClient;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import bassebombecraft.config.ItemConfig;
import bassebombecraft.operator.DefaultPorts;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Generic item implementation for execution of composite logic.
 * 
 * The logic is applied when the item is right clicked.
 **/
public class GenericCompositeLogicItem extends Item {

	/**
	 * Item signature.
	 */
	Item[] signatures = new Item[10];

	/**
	 * Operator ports.
	 */
	Ports ports;

	/**
	 * Operators.
	 */
	Operator2 ops;

	/**
	 * Book item cooldown value.
	 */
	int coolDown;

	/**
	 * Item tooltip.
	 */
	String tooltip;

	/**
	 * constructor.
	 * 
	 * @param name   item name.
	 * @param config item configuration.
	 */
	public GenericCompositeLogicItem(String name, ItemConfig config) {
		super(new Item.Properties().group(getItemGroup()));
		doCommonItemInitialization(this, name);

		// create ports
		this.ports = DefaultPorts.getInstance();

		// get cooldown and tooltip
		coolDown = config.cooldown.get();
		tooltip = config.tooltip.get();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

		// exit if invoked at client side
		if (isLogicalClient(world)) {
			return super.onItemRightClick(world, player, hand);
		}

		// post analytics
		getProxy().postItemUsage(this.getRegistryName().toString(), player.getGameProfile().getName());

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(this, coolDown);

		// configure
		if (hasSignatureChanged(player))
			configureState(player);

		// execute operators
		ports.setLivingEntity1(player);
		ports.setWorld(world);
		run(ports, ops);

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * 
	 * @param player
	 */
	void configureState(PlayerEntity player) {
		PlayerInventory inventory = player.inventory;

		ArrayList<Operator2> opList = new ArrayList<Operator2>();

		// determine if inventory contains composites
		int inventoryIndex = inventoryContainComposites(player);

		// configure operators
		configureOperator(inventory, opList, inventoryIndex);
		configureOperator(inventory, opList, inventoryIndex + 1);
		configureOperator(inventory, opList, inventoryIndex + 2);

		// create array
		Operator2[] opArray = opList.toArray(new Operator2[2]);

		// create sequence operator
		this.ops = new Sequence2(opArray);

	}

	/**
	 * Scan the inventory for composite items. Returns index of the first occurrence
	 * of a composite item. Return -1 if no composite items is found.
	 * 
	 * @param inventory player inventor
	 * 
	 * @return Returns index of the first occurrence of a composite item. Return -1
	 *         if no composite items is found.
	 */
	int inventoryContainComposites(PlayerEntity player) {
		PlayerInventory inventory = player.inventory;
		
		for(int index = 0; index < inventory.getSizeInventory(); index++) {
			ItemStack inventoryStack = inventory.getStackInSlot(index);
			
			// skip item stack if empty
			if(inventoryStack.isEmpty()) continue;
			
			// get item
			Item inventoryItem = inventoryStack.getItem();
			
			// exit loop if composite item
			if (inventoryItem instanceof GenericCompositeNullItem) return index;
			
		}
		
		// no composite values where found
		return -1;
	}

	/**
	 * 
	 * @param inventory
	 * @param opList
	 * @param index
	 */
	void configureOperator(PlayerInventory inventory, ArrayList<Operator2> opList, int index) {

		// get hotbar item
		ItemStack hotbarSlot = inventory.getStackInSlot(index);
		Item hotbarItem = hotbarSlot.getItem();

		// add operator for current slot
		if (hotbarItem instanceof GenericCompositeNullItem) {

			// type cast
			GenericCompositeNullItem compositeNullItem = (GenericCompositeNullItem) hotbarItem;

			// add operator
			Operator2 operator = compositeNullItem.createOperator();
			opList.add(operator);
		}
	}

	/**
	 * 
	 * @param inventory
	 * @return
	 */
	boolean hasSignatureChanged(PlayerEntity player) {
		PlayerInventory inventory = player.inventory;

		ItemStack hotbarSlot = inventory.getStackInSlot(1);
		Item hotbarItem = hotbarSlot.getItem();
		Item signatureItem = signatures[0];

		if (signatureItem == null) {
			if (hotbarItem == null)
				return false;

			// set signature
			signatures[0] = hotbarItem;
			return true;
		}

		int signatureHash = signatureItem.hashCode();
		int hotbarItemHash = hotbarItem.hashCode();

		if (signatureHash == hotbarItemHash)
			return false;

		// set signature
		signatures[0] = hotbarItem;
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		String message = new StringBuilder().append(this.tooltip).append(" (hc=").append(this.hashCode())
				.append(" ,op=").append(this.ops).append(")").toString();
		ITextComponent text = new TranslationTextComponent(TextFormatting.GREEN + message);
		tooltip.add(text);
	}

}
