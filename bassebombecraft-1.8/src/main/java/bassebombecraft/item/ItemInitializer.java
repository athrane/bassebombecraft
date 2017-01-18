package bassebombecraft.item;

import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BeastmasterBook;
import bassebombecraft.item.book.BeastmasterMistBook;
import bassebombecraft.item.book.BuildAbyssBook;
import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.item.book.BuildRainbowRoadBook;
import bassebombecraft.item.book.BuildRoadBook;
import bassebombecraft.item.book.BuildSmallHoleBook;
import bassebombecraft.item.book.CobwebBook;
import bassebombecraft.item.book.CopyPasteBlocksBook;
import bassebombecraft.item.book.CreeperApocalypseBook;
import bassebombecraft.item.book.CreeperCannonBook;
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
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.Spawn100ChickensBook;
import bassebombecraft.item.book.Spawn100RainingLlamasBook;
import bassebombecraft.item.book.SpawnDragonBook;
import bassebombecraft.item.book.SpawnFlamingChickenBook;
import bassebombecraft.item.book.SpawnGuardianBook;
import bassebombecraft.item.book.SpawnManyCowsBook;
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
import bassebombecraft.item.inventory.MobsLevitationIdolInventoryItem;
import bassebombecraft.item.inventory.LevitationIdolInventoryItem;
import bassebombecraft.item.inventory.LightningBoltIdolInventoryItem;
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
	 * Initialize items.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 * @param config
	 *            configuration object
	 */
	public void initialize(CreativeTabs targetTab) {

		Item gardenStaff = new GardenStaff();
		registerItem(targetTab, gardenStaff, GardenStaff.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(gardenStaff), "  x", " / ", "/  ", 'x', Items.FLOWER_POT, '/',
				Items.IRON_SHOVEL);

		Item parkourStaff = new ParkourStaff();
		registerItem(targetTab, parkourStaff, ParkourStaff.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(parkourStaff), "  x", " / ", "/  ", 'x', Items.LEATHER_LEGGINGS, '/',
				Items.STICK);

		// Initialise books

		Item teleportBook = new TeleportBook();
		registerItem(targetTab, teleportBook, TeleportBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(teleportBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.ENDER_PEARL);

		Item smallFireballBook = new SmallFireballBook();
		registerItem(targetTab, smallFireballBook, SmallFireballBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(smallFireballBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.BLAZE_POWDER);

		Item largeFireballBook = new LargeFireballBook();
		registerItem(targetTab, largeFireballBook, LargeFireballBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(largeFireballBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.MAGMA_CREAM);

		Item lingeringFlameBook = new LingeringFlameBook();
		registerItem(targetTab, lingeringFlameBook, LingeringFlameBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lingeringFlameBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.BLAZE_POWDER, 'z', Items.GHAST_TEAR);

		Item lingeringFuryBook = new LingeringFuryBook();
		registerItem(targetTab, lingeringFuryBook, LingeringFuryBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lingeringFuryBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.MAGMA_CREAM, 'z', Items.GHAST_TEAR);

		Item lavaSpiralMistBook = new LavaSpiralMistBook();
		registerItem(targetTab, lavaSpiralMistBook, LavaSpiralMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lavaSpiralMistBook), "xyz", "xxx", "xxx", 'x', Items.BOOK, 'y',
				Items.LAVA_BUCKET, 'z', Items.GHAST_TEAR);

		Item toxicMistBook = new ToxicMistBook();
		registerItem(targetTab, toxicMistBook, ToxicMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(toxicMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.ROTTEN_FLESH, 'z', Items.GHAST_TEAR);

		Item witherBook = new WitherSkullBook();
		registerItem(targetTab, witherBook, WitherSkullBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(witherBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y', Items.COAL);

		Item witherMistBook = new WitherMistBook();
		registerItem(targetTab, witherMistBook, WitherMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(witherMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.COAL, 'z', Items.GHAST_TEAR);

		Item movingWitherMistBook = new MovingWitherMistBook();
		registerItem(targetTab, movingWitherMistBook, MovingWitherMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingWitherMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.FERMENTED_SPIDER_EYE, 'z', Items.GHAST_TEAR);

		Item movingLavaMistBook = new MovingLavaMistBook();
		registerItem(targetTab, movingLavaMistBook, MovingLavaMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingLavaMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.LAVA_BUCKET, 'z', Items.GHAST_TEAR);

		Item movingLavaMultiMistBook = new MovingLavaMultiMistBook();
		registerItem(targetTab, movingLavaMultiMistBook, MovingLavaMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingLavaMultiMistBook), "xyz", "yzy", "   ", 'x', Items.BOOK, 'y',
				Items.LAVA_BUCKET, 'z', Items.GHAST_TEAR);

		Item movingIceMultiMistBook = new MovingIceMultiMistBook();
		registerItem(targetTab, movingIceMultiMistBook, MovingIceMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingIceMultiMistBook), "xyz", "yzy", "   ", 'x', Items.BOOK, 'y',
				Items.SNOWBALL, 'z', Items.GHAST_TEAR);

		Item movingWaterMultiMistBook = new MovingWaterMultiMistBook();
		registerItem(targetTab, movingWaterMultiMistBook, MovingWaterMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingWaterMultiMistBook), "xyz", "yzy", "   ", 'x', Items.BOOK, 'y',
				Items.WATER_BUCKET, 'z', Items.GHAST_TEAR);

		Item movingRainbowMistBook = new MovingRainbowMistBook();
		registerItem(targetTab, movingRainbowMistBook, MovingRainbowMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingRainbowMistBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RECORD_13);

		Item healingMistBook = new HealingMistBook();
		registerItem(targetTab, healingMistBook, HealingMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(healingMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GOLDEN_APPLE, 'z', Items.GHAST_TEAR);

		Item movingTntMistBook = new MovingTntMistBook();
		registerItem(targetTab, movingTntMistBook, MovingTntMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingTntMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.FLINT, 'z', Items.TNT_MINECART);

		// books of creature spawning

		Item spawnFlamingChickenBook = new SpawnFlamingChickenBook();
		registerItem(targetTab, spawnFlamingChickenBook, SpawnFlamingChickenBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnFlamingChickenBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.COOKED_CHICKEN);

		Item spawnSquidBook = new SpawnSquidBook();
		registerItem(targetTab, spawnSquidBook, SpawnSquidBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnSquidBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.DYE);

		Item baconBazookaBook = new BaconBazookaBook();
		registerItem(targetTab, baconBazookaBook, BaconBazookaBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(baconBazookaBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.PORKCHOP);

		Item creeperCannonBook = new CreeperCannonBook();
		registerItem(targetTab, creeperCannonBook, CreeperCannonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(creeperCannonBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GUNPOWDER);

		Item primedCreeperCannonBook = new PrimedCreeperCannonBook();
		registerItem(targetTab, primedCreeperCannonBook, PrimedCreeperCannonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(primedCreeperCannonBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GUNPOWDER, 'z', Blocks.IRON_BLOCK);

		Item creeperApocalypseBook = new CreeperApocalypseBook();
		registerItem(targetTab, creeperApocalypseBook, CreeperApocalypseBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(creeperApocalypseBook), "xyz", "yzy", "zyz", 'x', Items.BOOK, 'y',
				Items.GUNPOWDER, 'z', Blocks.IRON_BLOCK);

		Item spawn100ChickensBook = new Spawn100ChickensBook();
		registerItem(targetTab, spawn100ChickensBook, Spawn100ChickensBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawn100ChickensBook), "xyy", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.CHICKEN);

		Item spawnManyCowsBook = new SpawnManyCowsBook();
		registerItem(targetTab, spawnManyCowsBook, SpawnManyCowsBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnManyCowsBook), "xyy", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.BEEF);

		Item spawn100RainingLlamasBook = new Spawn100RainingLlamasBook();
		registerItem(targetTab, spawn100RainingLlamasBook, Spawn100RainingLlamasBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawn100RainingLlamasBook), "xyy", "yyy", "yyy", 'x', Items.BOOK,
				'y', Items.LEATHER);

		Item beastmasterMistBook = new BeastmasterMistBook();
		registerItem(targetTab, beastmasterMistBook, BeastmasterMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(beastmasterMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.NAME_TAG, 'z', Items.GHAST_TEAR);

		Item beastmasterBook = new BeastmasterBook();
		registerItem(targetTab, beastmasterBook, BeastmasterBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(beastmasterBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.NAME_TAG);

		Item spawnGuardianBook = new SpawnGuardianBook();
		registerItem(targetTab, spawnGuardianBook, SpawnGuardianBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnGuardianBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.NAME_TAG, 'z', Items.IRON_INGOT);

		Item spawnDragonBook = new SpawnDragonBook();
		registerItem(targetTab, spawnDragonBook, SpawnDragonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnDragonBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Blocks.DRAGON_EGG, 'z', Items.DRAGON_BREATH);

		Item multipleArrowsBook = new MultipleArrowsBook();
		registerItem(targetTab, multipleArrowsBook, MultipleArrowsBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(multipleArrowsBook), "xyy", "yyy", "yyy", 'x', Items.BOOK, 'y',
				Items.ARROW);

		Item cobwebBook = new CobwebBook();
		registerItem(targetTab, cobwebBook, CobwebBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(cobwebBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y', Items.STRING,
				'z', Items.SPIDER_EYE);

		Item iceBlockBook = new IceBlockBook();
		registerItem(targetTab, iceBlockBook, IceBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(iceBlockBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.SNOWBALL);

		Item lavaBlockBook = new LavaBlockBook();
		registerItem(targetTab, lavaBlockBook, LavaBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lavaBlockBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.LAVA_BUCKET);

		Item lightningBoltBook = new LightningBoltBook();
		registerItem(targetTab, lightningBoltBook, LightningBoltBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltBook), "xyy", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GOLD_INGOT);

		Item lightningBoltMistBook = new LightningBoltMistBook();
		registerItem(targetTab, lightningBoltMistBook, LightningBoltMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.GOLD_INGOT, 'z', Items.GHAST_TEAR);

		Item fallingAnvilBook = new FallingAnvilBook();
		registerItem(targetTab, fallingAnvilBook, FallingAnvilBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(fallingAnvilBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Blocks.ANVIL);

		Item emitHorizontalForceBook = new EmitHorizontalForceBook();
		registerItem(targetTab, emitHorizontalForceBook, EmitHorizontalForceBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitHorizontalForceBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RABBIT_FOOT, 'z', Items.FERMENTED_SPIDER_EYE);

		Item emitVerticalForceBook = new EmitVerticalForceBook();
		registerItem(targetTab, emitVerticalForceBook, EmitVerticalForceBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitVerticalForceBook), "x  ", "y  ", "z  ", 'x', Items.BOOK, 'y',
				Items.RABBIT_FOOT, 'z', Items.FERMENTED_SPIDER_EYE);

		Item emitVerticalForceMistBook = new EmitVerticalForceMistBook();
		registerItem(targetTab, emitVerticalForceMistBook, EmitVerticalForceMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitVerticalForceMistBook), "x  ", "y  ", "z  ", 'x', Items.BOOK,
				'y', Items.RABBIT_FOOT, 'z', Items.GHAST_TEAR);

		Item vacuumMistBook = new VacuumMistBook();
		registerItem(targetTab, vacuumMistBook, VacuumMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(vacuumMistBook), "xyz", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RABBIT_FOOT, 'z', Items.GHAST_TEAR);

		// books of block manipulation

		Item copyPasteBlocksBook = new CopyPasteBlocksBook();
		registerItem(targetTab, copyPasteBlocksBook, CopyPasteBlocksBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(copyPasteBlocksBook), "xyz", "   ", "   ", 'x', Items.WRITTEN_BOOK,
				'y', Items.NETHER_STAR, 'z', Items.FERMENTED_SPIDER_EYE);

		Item duplicateBlockBook = new DuplicateBlockBook();
		registerItem(targetTab, duplicateBlockBook, DuplicateBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(duplicateBlockBook), "xyz", "   ", "   ", 'x', Items.WRITTEN_BOOK,
				'y', Items.NETHER_STAR, 'z', Items.ENDER_PEARL);

		// books of construction

		Item buildRoadBook = new BuildRoadBook();
		registerItem(targetTab, buildRoadBook, BuildRoadBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildRoadBook), "xy ", "   ", "   ", 'x', Items.WRITTEN_BOOK, 'y',
				Items.STONE_PICKAXE);

		Item buildRainbowRoadBook = new BuildRainbowRoadBook();
		registerItem(targetTab, buildRainbowRoadBook, BuildRainbowRoadBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildRainbowRoadBook), "xyy", "yyy", "yyy", 'x', Items.WRITTEN_BOOK,
				'y', Items.DYE);

		Item buildMineBook = new BuildMineBook();
		registerItem(targetTab, buildMineBook, BuildMineBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildMineBook), "xy ", "   ", "   ", 'x', Items.WRITTEN_BOOK, 'y',
				Items.DIAMOND_PICKAXE);

		Item buildAbyssBook = new BuildAbyssBook();
		registerItem(targetTab, buildAbyssBook, BuildAbyssBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildAbyssBook), "xyz", "   ", "   ", 'x', Items.WRITTEN_BOOK, 'y',
				Items.IRON_PICKAXE, 'z', Items.WATER_BUCKET);

		Item buildSmallHoleBook = new BuildSmallHoleBook();
		registerItem(targetTab, buildSmallHoleBook, BuildSmallHoleBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildSmallHoleBook), "xy ", "   ", "   ", 'x', Items.WRITTEN_BOOK,
				'y', Items.IRON_PICKAXE);

		Item naturalizeBook = new NaturalizeBook();
		registerItem(targetTab, naturalizeBook, NaturalizeBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(naturalizeBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.FLOWER_POT);

		Item rainbownizeBook = new RainbownizeBook();
		registerItem(targetTab, rainbownizeBook, RainbownizeBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainbownizeBook), "xy ", "   ", "   ", 'x', Items.BOOK, 'y',
				Items.RECORD_11);

		// Initialise inventory items

		Item rainIdolInventoryItem = new RainIdolInventoryItem();
		registerItem(targetTab, rainIdolInventoryItem, RainIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.CLAY_BALL,
				'y', Items.WATER_BUCKET);

		Item chickenizeIdolInventoryItem = new ChickenizeIdolInventoryItem();
		registerItem(targetTab, chickenizeIdolInventoryItem, ChickenizeIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(chickenizeIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.CHICKEN);

		Item angelIdolInventoryItem = new AngelIdolInventoryItem();
		registerItem(targetTab, angelIdolInventoryItem, AngelIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(angelIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.CLAY_BALL,
				'y', Items.GOLDEN_APPLE);

		Item levitationIdolInventoryItem = new LevitationIdolInventoryItem();
		registerItem(targetTab, levitationIdolInventoryItem, LevitationIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(levitationIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.FEATHER);

		Item mobsLevitationIdolInventoryItem = new MobsLevitationIdolInventoryItem();
		registerItem(targetTab, mobsLevitationIdolInventoryItem, MobsLevitationIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(mobsLevitationIdolInventoryItem), "xyy", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.FEATHER);

		Item lightningBoltIdolInventoryItem = new LightningBoltIdolInventoryItem();
		registerItem(targetTab, lightningBoltIdolInventoryItem, LightningBoltIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.GOLD_INGOT);

		Item flowerIdolInventoryItem = new FlowerIdolInventoryItem();
		registerItem(targetTab, flowerIdolInventoryItem, FlowerIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(flowerIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.CLAY_BALL,
				'y', Items.FLOWER_POT);

		Item rainbownizeIdolInventoryItem = new RainbownizeIdolInventoryItem();
		registerItem(targetTab, rainbownizeIdolInventoryItem, RainbownizeIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainbownizeIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.RECORD_11);

		Item flameBlastIdolInventoryItem = new FlameBlastIdolInventoryItem();
		registerItem(targetTab, flameBlastIdolInventoryItem, FlameBlastIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(flameBlastIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.FIRE_CHARGE);

		Item charmBeastIdolInventoryItem = new CharmBeastIdolInventoryItem();
		registerItem(targetTab, charmBeastIdolInventoryItem, CharmBeastIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(charmBeastIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.NAME_TAG);

		Item blindnessIdolInventoryItem = new BlindnessIdolInventoryItem();
		registerItem(targetTab, blindnessIdolInventoryItem, BlindnessIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(blindnessIdolInventoryItem), "xy ", "   ", "   ", 'x',
				Items.CLAY_BALL, 'y', Items.COAL);

		/**
		 * Item pvpIdolInventoryItem = new PvpIdolInventoryItem();
		 * pvpIdolInventoryItem.setCreativeTab(targetTab);
		 * GameRegistry.registerItem(pvpIdolInventoryItem,
		 * PvpIdolInventoryItem.ITEM_NAME); logger.info("initializing item: " +
		 * PvpIdolInventoryItem.ITEM_NAME); GameRegistry.addShapedRecipe(new
		 * ItemStack(pvpIdolInventoryItem), "xy ", " ", " ", 'x',
		 * Items.clay_ball, 'y', Items.iron_sword);
		 **/
	}

	/**
	 * Helper method for registration of item.
	 * 
	 * @param targetTab
	 *            target tab for item.
	 * @param item
	 *            item to register.
	 * @param itemName
	 *            item name.
	 */
	void registerItem(CreativeTabs targetTab, Item item, String itemName) {
		item.setCreativeTab(targetTab);
		item.setRegistryName(itemName);
		GameRegistry.register(item);
	}

	public static ItemInitializer getInstance() {
		return new ItemInitializer();
	}

}
