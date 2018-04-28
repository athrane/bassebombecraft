package bassebombecraft.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Class for initializing items.
 */
public class ItemInitializer {

	/**
	 * Book item list.
	 */
	List<Item> bookItemList = new ArrayList<Item>();

	/**
	 * Inventory item list.
	 */
	List<Item> inventoryItemList = new ArrayList<Item>();

	/**
	 * Basic item map.
	 */
	Map<String,Item> basicItemMap = new HashMap<String, Item>();
	
	/**
	 * Initialize book items.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 * @param config
	 *            configuration object
	 * 
	 * @return inventory item list.
	 */
	public List<Item> initializeBooks(CreativeTabs targetTab) {

		// Initialise books

		/**				

		Item vacuumMistBook = new VacuumMistBook();
		registerBookItem(targetTab, vacuumMistBook, VacuumMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(vacuumMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RABBIT_FOOT, 'z', Items.GHAST_TEAR);

		// books of block manipulation

		Item copyPasteBlocksBook = new CopyPasteBlocksBook();
		registerBookItem(targetTab, copyPasteBlocksBook, CopyPasteBlocksBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(copyPasteBlocksBook), "xyz", "   ", "   ", 'x', Items.WRITTEN_BOOK,
				'y', Items.NETHER_STAR, 'z', Items.FERMENTED_SPIDER_EYE);

		Item duplicateBlockBook = new DuplicateBlockBook();
		registerBookItem(targetTab, duplicateBlockBook, DuplicateBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(duplicateBlockBook), "xyz", "   ", "   ", 'x', Items.WRITTEN_BOOK,
				'y', Items.NETHER_STAR, 'z', Items.ENDER_PEARL);

		// books of construction

		Item buildRoadBook = new BuildRoadBook();
		registerBookItem(targetTab, buildRoadBook, BuildRoadBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildRoadBook), "xy ", "   ", "   ", 'x', Items.WRITTEN_BOOK, 'y',
				Items.STONE_PICKAXE);

		Item buildRainbowRoadBook = new BuildRainbowRoadBook();
		registerBookItem(targetTab, buildRainbowRoadBook, BuildRainbowRoadBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildRainbowRoadBook), "xyy", "yyy", "yyy", 'x', Items.WRITTEN_BOOK,
				'y', Items.DYE);

		Item buildMineBook = new BuildMineBook();
		registerBookItem(targetTab, buildMineBook, BuildMineBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildMineBook), "xy ", "   ", "   ", 'x', Items.WRITTEN_BOOK, 'y',
				Items.DIAMOND_PICKAXE);

		Item buildAbyssBook = new BuildAbyssBook();
		registerBookItem(targetTab, buildAbyssBook, BuildAbyssBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildAbyssBook), "xyz", "   ", "   ", 'x', Items.WRITTEN_BOOK, 'y',
				Items.IRON_PICKAXE, 'z', Items.WATER_BUCKET);

		Item buildSmallHoleBook = new BuildSmallHoleBook();
		registerBookItem(targetTab, buildSmallHoleBook, BuildSmallHoleBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildSmallHoleBook), "xy ", "   ", "   ", 'x', Items.WRITTEN_BOOK,
				'y', Items.IRON_PICKAXE);

		Item naturalizeBook = new NaturalizeBook();
		registerBookItem(targetTab, naturalizeBook, NaturalizeBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(naturalizeBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.FLOWER_POT);

		Item rainbownizeBook = new RainbownizeBook();
		registerBookItem(targetTab, rainbownizeBook, RainbownizeBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainbownizeBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RECORD_11);

		Item buildTowerBook = new BuildTowerBook();
		registerBookItem(targetTab, buildTowerBook, BuildTowerBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildTowerBook), "x  ", " yy", " yy", 'x', Items.WRITTEN_BOOK, 'y',
				Blocks.SANDSTONE);
		 **/

		return bookItemList;
	}

	/**
	 * Initialize baton items.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 * @param config
	 *            configuration object
	 * 
	 * @return inventory item list.
	 */
	public void initializeBatons(CreativeTabs targetTab) {

		/**
		Item mobCommandersBaton = new MobCommandersBaton();
		registerBookItem(targetTab, mobCommandersBaton, MobCommandersBaton.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(mobCommandersBaton), "  x", " y ", "z  ", 'x', Blocks.SKULL, 'y', Items.BONE, 'z', Blocks.REDSTONE_BLOCK);
		**/
	}

	/**
	 * Initialize inventory items.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 * @param config
	 *            configuration object
	 * 
	 * @return inventory item list.
	 */
	public List<Item> initializeInventoryItems(CreativeTabs targetTab) {

		/**
		Item rainIdolInventoryItem = new RainIdolInventoryItem();
		registerInventoryItem(targetTab, rainIdolInventoryItem, RainIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.CLAY_BALL,
				'y', Items.WATER_BUCKET);

		Item chickenizeIdolInventoryItem = new ChickenizeIdolInventoryItem();
		registerInventoryItem(targetTab, chickenizeIdolInventoryItem, ChickenizeIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(chickenizeIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.CHICKEN);

		Item angelIdolInventoryItem = new AngelIdolInventoryItem();
		registerInventoryItem(targetTab, angelIdolInventoryItem, AngelIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(angelIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.CLAY_BALL,
				'y', Items.GOLDEN_APPLE);

		Item levitationIdolInventoryItem = new LevitationIdolInventoryItem();
		registerInventoryItem(targetTab, levitationIdolInventoryItem, LevitationIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(levitationIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.FEATHER);

		Item mobsLevitationIdolInventoryItem = new MobsLevitationIdolInventoryItem();
		registerInventoryItem(targetTab, mobsLevitationIdolInventoryItem, MobsLevitationIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(mobsLevitationIdolInventoryItem), "xyy", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.FEATHER);

		Item lightningBoltIdolInventoryItem = new LightningBoltIdolInventoryItem();
		registerInventoryItem(targetTab, lightningBoltIdolInventoryItem, LightningBoltIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.GOLD_INGOT);

		Item flowerIdolInventoryItem = new FlowerIdolInventoryItem();
		registerInventoryItem(targetTab, flowerIdolInventoryItem, FlowerIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(flowerIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.CLAY_BALL,
				'y', Items.FLOWER_POT);

		Item rainbownizeIdolInventoryItem = new RainbownizeIdolInventoryItem();
		registerInventoryItem(targetTab, rainbownizeIdolInventoryItem, RainbownizeIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainbownizeIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.RECORD_11);

		Item flameBlastIdolInventoryItem = new FlameBlastIdolInventoryItem();
		registerInventoryItem(targetTab, flameBlastIdolInventoryItem, FlameBlastIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(flameBlastIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.FIRE_CHARGE);

		Item charmBeastIdolInventoryItem = new CharmBeastIdolInventoryItem();
		registerInventoryItem(targetTab, charmBeastIdolInventoryItem, CharmBeastIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(charmBeastIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.NAME_TAG);

		Item blindnessIdolInventoryItem = new BlindnessIdolInventoryItem();
		registerInventoryItem(targetTab, blindnessIdolInventoryItem, BlindnessIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(blindnessIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.COAL);

		Item pinkynizeIdolInventoryItem = new PinkynizeIdolInventoryItem();
		registerInventoryItem(targetTab, pinkynizeIdolInventoryItem, PinkynizeIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(pinkynizeIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Blocks.WOOL);

		Item primeMobIdolInventoryItem = new PrimeMobIdolInventoryItem();
		registerInventoryItem(targetTab, primeMobIdolInventoryItem, PrimeMobIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(primeMobIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.FIRE_CHARGE);

		Item llamaSpitIdolInventoryItem = new LlamaSpitIdolInventoryItem();
		registerInventoryItem(targetTab, llamaSpitIdolInventoryItem, LlamaSpitIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(llamaSpitIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.LEATHER);

		Item terminatorEyeIem = basicItemMap.get(TerminatorEyeItem.ITEM_NAME);
		Item eggProjectileIdolInventoryItem = new EggProjectileIdolInventoryItem();
		registerInventoryItem(targetTab, eggProjectileIdolInventoryItem, EggProjectileIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(eggProjectileIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', terminatorEyeIem);

		Item meteorIdolInventoryItem = new MeteorIdolInventoryItem();
		registerInventoryItem(targetTab, meteorIdolInventoryItem, MeteorIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(meteorIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.LAVA_BUCKET);
		**/
		
		/**
		 * Item pvpIdolInventoryItem = new PvpIdolInventoryItem();
		 * pvpIdolInventoryItem.setCreativeTab(targetTab);
		 * GameRegistry.registerItem(pvpIdolInventoryItem,
		 * PvpIdolInventoryItem.ITEM_NAME); logger.info("initializing item: " +
		 * PvpIdolInventoryItem.ITEM_NAME); GameRegistry.addShapedRecipe(new
		 * ItemStack(pvpIdolInventoryItem), "xy ", " ", " ", 'x',
		 * Items.clay_ball, 'y', Items.iron_sword);
		 **/

		return inventoryItemList;
	}


	/**
	 * Initialize basic items.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 * @param config
	 *            configuration object
	 * 
	 * @return basic item map.
	 */
	public Map initializeBasicItems(CreativeTabs targetTab) {

		//Item terminatorEyeItem = new TerminatorEyeItem();
		//registerBasicItem(targetTab, terminatorEyeItem, TerminatorEyeItem.ITEM_NAME);
		return basicItemMap;
	}
	
	/**
	 * Helper method for registration of book item.
	 * 
	 * @param targetTab
	 *            target tab for item.
	 * @param item
	 *            item to register.
	 * @param itemName
	 *            item name.
	 */
	void registerBookItem(CreativeTabs targetTab, Item item, String itemName) {
		item.setCreativeTab(targetTab);
		item.setRegistryName(itemName);
		//GameRegistry.register(item);
		bookItemList.add(item);
	}

	/**
	 * Helper method for registration of baton item.
	 * 
	 * @param targetTab
	 *            target tab for item.
	 * @param item
	 *            item to register.
	 * @param itemName
	 *            item name.
	 */
	void registerBatonItem(CreativeTabs targetTab, Item item, String itemName) {
		item.setCreativeTab(targetTab);
		item.setRegistryName(itemName);
		//GameRegistry.register(item);
	}

	/**
	 * Helper method for registration of basic item.
	 * 
	 * @param targetTab
	 *            target tab for item.
	 * @param item
	 *            item to register.
	 * @param itemName
	 *            item name.
	 */
	void registerBasicItem(CreativeTabs targetTab, Item item, String itemName) {
		item.setCreativeTab(targetTab);
		item.setRegistryName(itemName);
		//GameRegistry.register(item);
		basicItemMap.put(itemName, item);
	}
		
	/**
	 * Helper method for registration of inventory item.
	 * 
	 * @param targetTab
	 *            target tab for item.
	 * @param item
	 *            item to register.
	 * @param itemName
	 *            item name.
	 */
	void registerInventoryItem(CreativeTabs targetTab, Item item, String itemName) {
		item.setCreativeTab(targetTab);
		item.setRegistryName(itemName);
		//GameRegistry.register(item);
		inventoryItemList.add(item);
	}

	public static ItemInitializer getInstance() {
		return new ItemInitializer();
	}

}
