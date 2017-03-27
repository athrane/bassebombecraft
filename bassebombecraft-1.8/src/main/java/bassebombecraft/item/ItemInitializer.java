package bassebombecraft.item;

import java.util.ArrayList;
import java.util.List;

import bassebombecraft.item.baton.MobCommandersBaton;
import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BeastmasterBook;
import bassebombecraft.item.book.BeastmasterMistBook;
import bassebombecraft.item.book.BuildAbyssBook;
import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.item.book.BuildRainbowRoadBook;
import bassebombecraft.item.book.BuildRoadBook;
import bassebombecraft.item.book.BuildSmallHoleBook;
import bassebombecraft.item.book.BuildStairsBook;
import bassebombecraft.item.book.BuildTowerBook;
import bassebombecraft.item.book.CobwebBook;
import bassebombecraft.item.book.CopyPasteBlocksBook;
import bassebombecraft.item.book.CreeperApocalypseBook;
import bassebombecraft.item.book.CreeperCannonBook;
import bassebombecraft.item.book.DigMobHoleBook;
import bassebombecraft.item.book.DuplicateBlockBook;
import bassebombecraft.item.book.EmitHorizontalForceBook;
import bassebombecraft.item.book.EmitVerticalForceBook;
import bassebombecraft.item.book.EmitVerticalForceMistBook;
import bassebombecraft.item.book.FallingAnvilBook;
import bassebombecraft.item.book.HealingMistBook;
import bassebombecraft.item.book.IceBlockBook;
import bassebombecraft.item.book.LargeFireballBook;
import bassebombecraft.item.book.LavaBlockBook;
import bassebombecraft.item.book.LavaSpiralMistBook;
import bassebombecraft.item.book.LightningBoltBook;
import bassebombecraft.item.book.LightningBoltMistBook;
import bassebombecraft.item.book.LingeringFlameBook;
import bassebombecraft.item.book.LingeringFuryBook;
import bassebombecraft.item.book.MovingIceMultiMistBook;
import bassebombecraft.item.book.MovingLavaMistBook;
import bassebombecraft.item.book.MovingLavaMultiMistBook;
import bassebombecraft.item.book.MovingRainbowMistBook;
import bassebombecraft.item.book.MovingTntMistBook;
import bassebombecraft.item.book.MovingWaterMultiMistBook;
import bassebombecraft.item.book.MovingWitherMistBook;
import bassebombecraft.item.book.MultipleArrowsBook;
import bassebombecraft.item.book.NaturalizeBook;
import bassebombecraft.item.book.PrimedCreeperCannonBook;
import bassebombecraft.item.book.RainbownizeBook;
import bassebombecraft.item.book.SetSpawnPointBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.Spawn100ChickensBook;
import bassebombecraft.item.book.Spawn100RainingLlamasBook;
import bassebombecraft.item.book.SpawnDragonBook;
import bassebombecraft.item.book.SpawnFlamingChickenBook;
import bassebombecraft.item.book.SpawnGiantZombieBook;
import bassebombecraft.item.book.SpawnGuardianBook;
import bassebombecraft.item.book.SpawnKittenArmyBook;
import bassebombecraft.item.book.SpawnManyCowsBook;
import bassebombecraft.item.book.SpawnSkeletonArmyBook;
import bassebombecraft.item.book.SpawnSquidBook;
import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.item.book.ToxicMistBook;
import bassebombecraft.item.book.VacuumMistBook;
import bassebombecraft.item.book.WitherMistBook;
import bassebombecraft.item.book.WitherSkullBook;
import bassebombecraft.item.inventory.AngelIdolInventoryItem;
import bassebombecraft.item.inventory.BlindnessIdolInventoryItem;
import bassebombecraft.item.inventory.CharmBeastIdolInventoryItem;
import bassebombecraft.item.inventory.ChickenizeIdolInventoryItem;
import bassebombecraft.item.inventory.FlameBlastIdolInventoryItem;
import bassebombecraft.item.inventory.FlowerIdolInventoryItem;
import bassebombecraft.item.inventory.LevitationIdolInventoryItem;
import bassebombecraft.item.inventory.LightningBoltIdolInventoryItem;
import bassebombecraft.item.inventory.MobsLevitationIdolInventoryItem;
import bassebombecraft.item.inventory.PinkynizeIdolInventoryItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import bassebombecraft.item.inventory.RainIdolInventoryItem;
import bassebombecraft.item.inventory.RainbownizeIdolInventoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

		// Initialise staff

		Item gardenStaff = new GardenStaff();
		registerBookItem(targetTab, gardenStaff, GardenStaff.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(gardenStaff), "  x", " / ", "/  ", 'x', Items.FLOWER_POT, '/',
				Items.IRON_SHOVEL);

		// Initialise books

		Item teleportBook = new TeleportBook();
		registerBookItem(targetTab, teleportBook, TeleportBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(teleportBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.ENDER_PEARL);

		Item setSpawnPointBook = new SetSpawnPointBook();
		registerBookItem(targetTab, setSpawnPointBook, SetSpawnPointBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(setSpawnPointBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Blocks.BED);
		
		Item smallFireballBook = new SmallFireballBook();
		registerBookItem(targetTab, smallFireballBook, SmallFireballBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(smallFireballBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.FIRE_CHARGE);

		Item largeFireballBook = new LargeFireballBook();
		registerBookItem(targetTab, largeFireballBook, LargeFireballBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(largeFireballBook), "xy ", "zz ", "   ", 'x', Items.BOOK, 'y',
				Items.FIRE_CHARGE, 'z', Items.GUNPOWDER);

		Item lingeringFlameBook = new LingeringFlameBook();
		registerBookItem(targetTab, lingeringFlameBook, LingeringFlameBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lingeringFlameBook), "xyz", "w  ", "   ", 'x', Items.BOOK, 'y',
				Items.BLAZE_POWDER, 'z', Items.GHAST_TEAR, 'w', Items.GUNPOWDER);

		Item lingeringFuryBook = new LingeringFuryBook();
		registerBookItem(targetTab, lingeringFuryBook, LingeringFuryBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lingeringFuryBook), "xyz", "w  ", "   ", 'x', Items.BOOK, 'y',
				Items.MAGMA_CREAM, 'z', Items.GHAST_TEAR, 'w', Blocks.TNT);

		Item lavaSpiralMistBook = new LavaSpiralMistBook();
		registerBookItem(targetTab, lavaSpiralMistBook, LavaSpiralMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lavaSpiralMistBook), "yyy", "yxy", "yyy", 'x', Items.BOOK, 'y',
				Items.MAGMA_CREAM );

		Item toxicMistBook = new ToxicMistBook();
		registerBookItem(targetTab, toxicMistBook, ToxicMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(toxicMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.ROTTEN_FLESH, 'z', Items.GHAST_TEAR);

		Item witherBook = new WitherSkullBook();
		registerBookItem(targetTab, witherBook, WitherSkullBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(witherBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y', Items.COAL);

		Item witherMistBook = new WitherMistBook();
		registerBookItem(targetTab, witherMistBook, WitherMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(witherMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.COAL, 'z', Items.GHAST_TEAR);

		Item movingWitherMistBook = new MovingWitherMistBook();
		registerBookItem(targetTab, movingWitherMistBook, MovingWitherMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingWitherMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.FERMENTED_SPIDER_EYE, 'z', Items.GHAST_TEAR);

		Item movingLavaMistBook = new MovingLavaMistBook();
		registerBookItem(targetTab, movingLavaMistBook, MovingLavaMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingLavaMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.LAVA_BUCKET, 'z', Items.GHAST_TEAR);

		Item movingLavaMultiMistBook = new MovingLavaMultiMistBook();
		registerBookItem(targetTab, movingLavaMultiMistBook, MovingLavaMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingLavaMultiMistBook), "xyz", "yzy", "   ", 'x', Items.BOOK, 'y',
				Items.LAVA_BUCKET, 'z', Items.GHAST_TEAR);

		Item movingIceMultiMistBook = new MovingIceMultiMistBook();
		registerBookItem(targetTab, movingIceMultiMistBook, MovingIceMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingIceMultiMistBook), "xyz", "yzy", "   ", 'x', Items.BOOK, 'y',
				Items.SNOWBALL, 'z', Items.GHAST_TEAR);

		Item movingWaterMultiMistBook = new MovingWaterMultiMistBook();
		registerBookItem(targetTab, movingWaterMultiMistBook, MovingWaterMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingWaterMultiMistBook), "xyz", "yzy", "   ", 'x', Items.BOOK, 'y',
				Items.WATER_BUCKET, 'z', Items.GHAST_TEAR);

		Item movingRainbowMistBook = new MovingRainbowMistBook();
		registerBookItem(targetTab, movingRainbowMistBook, MovingRainbowMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingRainbowMistBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RECORD_13);

		Item healingMistBook = new HealingMistBook();
		registerBookItem(targetTab, healingMistBook, HealingMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(healingMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GOLDEN_APPLE, 'z', Items.GHAST_TEAR);

		Item movingTntMistBook = new MovingTntMistBook();
		registerBookItem(targetTab, movingTntMistBook, MovingTntMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingTntMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.FLINT, 'z', Items.TNT_MINECART);
		
		// books of creature spawning

		Item spawnFlamingChickenBook = new SpawnFlamingChickenBook();
		registerBookItem(targetTab, spawnFlamingChickenBook, SpawnFlamingChickenBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnFlamingChickenBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.COOKED_CHICKEN);

		Item spawnSquidBook = new SpawnSquidBook();
		registerBookItem(targetTab, spawnSquidBook, SpawnSquidBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnSquidBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.DYE);

		Item baconBazookaBook = new BaconBazookaBook();
		registerBookItem(targetTab, baconBazookaBook, BaconBazookaBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(baconBazookaBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.PORKCHOP);

		Item creeperCannonBook = new CreeperCannonBook();
		registerBookItem(targetTab, creeperCannonBook, CreeperCannonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(creeperCannonBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GUNPOWDER);

		Item primedCreeperCannonBook = new PrimedCreeperCannonBook();
		registerBookItem(targetTab, primedCreeperCannonBook, PrimedCreeperCannonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(primedCreeperCannonBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GUNPOWDER, 'z', Blocks.IRON_BLOCK);

		Item creeperApocalypseBook = new CreeperApocalypseBook();
		registerBookItem(targetTab, creeperApocalypseBook, CreeperApocalypseBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(creeperApocalypseBook), "xyz", "yzy", "zyz", 'x', Items.BOOK, 'y',
				Items.GUNPOWDER, 'z', Blocks.IRON_BLOCK);

		Item spawn100ChickensBook = new Spawn100ChickensBook();
		registerBookItem(targetTab, spawn100ChickensBook, Spawn100ChickensBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawn100ChickensBook), "xyy", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.CHICKEN);

		Item spawnManyCowsBook = new SpawnManyCowsBook();
		registerBookItem(targetTab, spawnManyCowsBook, SpawnManyCowsBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnManyCowsBook), "xyy", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.BEEF);

		Item spawn100RainingLlamasBook = new Spawn100RainingLlamasBook();
		registerBookItem(targetTab, spawn100RainingLlamasBook, Spawn100RainingLlamasBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawn100RainingLlamasBook), "xyy", "yyy", "yyy", 'x', Items.BOOK,
				'y', Items.LEATHER);

		Item spawnKittenArmyBook = new SpawnKittenArmyBook();
		registerBookItem(targetTab, spawnKittenArmyBook, SpawnKittenArmyBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnKittenArmyBook), "xyy", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.FISH);

		Item spawnSkeletonArmyBook = new SpawnSkeletonArmyBook();
		registerBookItem(targetTab, spawnSkeletonArmyBook, SpawnSkeletonArmyBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnSkeletonArmyBook), "xyz", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.SKULL, 'z', Items.WOODEN_SWORD);

		Item spawnGiantZombieBook = new SpawnGiantZombieBook();
		registerBookItem(targetTab, spawnGiantZombieBook, SpawnGiantZombieBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnGiantZombieBook), "xyz", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.ROTTEN_FLESH, 'z', Blocks.SKULL);

		Item beastmasterMistBook = new BeastmasterMistBook();
		registerBookItem(targetTab, beastmasterMistBook, BeastmasterMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(beastmasterMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.NAME_TAG, 'z', Items.GHAST_TEAR);

		Item beastmasterBook = new BeastmasterBook();
		registerBookItem(targetTab, beastmasterBook, BeastmasterBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(beastmasterBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.NAME_TAG);

		Item spawnGuardianBook = new SpawnGuardianBook();
		registerBookItem(targetTab, spawnGuardianBook, SpawnGuardianBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnGuardianBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.NAME_TAG, 'z', Items.IRON_INGOT);

		Item spawnDragonBook = new SpawnDragonBook();
		registerBookItem(targetTab, spawnDragonBook, SpawnDragonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnDragonBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Blocks.DRAGON_EGG, 'z', Items.DRAGON_BREATH);

		Item multipleArrowsBook = new MultipleArrowsBook();
		registerBookItem(targetTab, multipleArrowsBook, MultipleArrowsBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(multipleArrowsBook), "xyy", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.ARROW);

		Item cobwebBook = new CobwebBook();
		registerBookItem(targetTab, cobwebBook, CobwebBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(cobwebBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y', Items.STRING,
				'z', Items.SPIDER_EYE);

		Item iceBlockBook = new IceBlockBook();
		registerBookItem(targetTab, iceBlockBook, IceBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(iceBlockBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.SNOWBALL);

		Item lavaBlockBook = new LavaBlockBook();
		registerBookItem(targetTab, lavaBlockBook, LavaBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lavaBlockBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.LAVA_BUCKET);

		Item digMobHoleBook = new DigMobHoleBook();
		registerBookItem(targetTab, digMobHoleBook, DigMobHoleBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(digMobHoleBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.IRON_SHOVEL);

		Item lightningBoltBook = new LightningBoltBook();
		registerBookItem(targetTab, lightningBoltBook, LightningBoltBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GOLD_INGOT);

		Item lightningBoltMistBook = new LightningBoltMistBook();
		registerBookItem(targetTab, lightningBoltMistBook, LightningBoltMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GOLD_INGOT, 'z', Items.GHAST_TEAR);

		Item fallingAnvilBook = new FallingAnvilBook();
		registerBookItem(targetTab, fallingAnvilBook, FallingAnvilBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(fallingAnvilBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Blocks.ANVIL);

		Item emitHorizontalForceBook = new EmitHorizontalForceBook();
		registerBookItem(targetTab, emitHorizontalForceBook, EmitHorizontalForceBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitHorizontalForceBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RABBIT_FOOT, 'z', Items.FERMENTED_SPIDER_EYE);

		Item emitVerticalForceBook = new EmitVerticalForceBook();
		registerBookItem(targetTab, emitVerticalForceBook, EmitVerticalForceBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitVerticalForceBook), "x  ", "y  ", "z  ", 'x', Items.BOOK, 'y',
				Items.RABBIT_FOOT, 'z', Items.FERMENTED_SPIDER_EYE);

		Item emitVerticalForceMistBook = new EmitVerticalForceMistBook();
		registerBookItem(targetTab, emitVerticalForceMistBook, EmitVerticalForceMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitVerticalForceMistBook), "x  ", "y  ", "z  ", 'x', Items.BOOK,
				'y', Items.RABBIT_FOOT, 'z', Items.GHAST_TEAR);

		Item buildStairsBook = new BuildStairsBook();
		registerBookItem(targetTab, buildStairsBook, BuildStairsBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildStairsBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.IRON_HOE, 'z', Blocks.ICE);

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
		GameRegistry.addShapedRecipe(new ItemStack(buildTowerBook), "xy ", "   ", "   ", 'x', Items.WRITTEN_BOOK, 'y',
				Items.DIAMOND_PICKAXE);

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

		Item mobCommandersBaton = new MobCommandersBaton();
		registerBookItem(targetTab, mobCommandersBaton, MobCommandersBaton.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(mobCommandersBaton), "  x", " y ", "z  ", 'x', Blocks.SKULL, 'y', Items.BONE, 'z', Blocks.REDSTONE_BLOCK);
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
		GameRegistry.register(item);
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
		GameRegistry.register(item);
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
		GameRegistry.register(item);
		inventoryItemList.add(item);
	}

	public static ItemInitializer getInstance() {
		return new ItemInitializer();
	}

}
