package bassebombecraft.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.item.action.mist.block.RainbowSpiralMist;
import bassebombecraft.item.book.FallingAnvilBook;
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
import bassebombecraft.item.book.HealingMistBook;
import bassebombecraft.item.book.IceBlockBook;
import bassebombecraft.item.book.LargeFireballBook;
import bassebombecraft.item.book.LavaBlockBook;
import bassebombecraft.item.book.LavaSpiralMistBook;
import bassebombecraft.item.book.LightningBoltBook;
import bassebombecraft.item.book.LingeringFlameBook;
import bassebombecraft.item.book.LingeringFuryBook;
import bassebombecraft.item.book.MovingIceMultiMistBook;
import bassebombecraft.item.book.MovingLavaMistBook;
import bassebombecraft.item.book.MovingLavaMultiMistBook;
import bassebombecraft.item.book.MovingRainbowMistBook;
import bassebombecraft.item.book.MovingWaterMultiMistBook;
import bassebombecraft.item.book.MovingWitherMistBook;
import bassebombecraft.item.book.MultipleArrowsBook;
import bassebombecraft.item.book.NaturalizeBook;
import bassebombecraft.item.book.PrimedCreeperCannonBook;
import bassebombecraft.item.book.RainbownizeBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.Spawn100ChickensBook;
import bassebombecraft.item.book.SpawnFlamingChickenBook;
import bassebombecraft.item.book.SpawnGuardianBook;
import bassebombecraft.item.book.SpawnManyCowsBook;
import bassebombecraft.item.book.SpawnSquidBook;
import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.item.book.LightningBoltMistBook;
import bassebombecraft.item.book.ToxicMistBook;
import bassebombecraft.item.book.VacuumMistBook;
import bassebombecraft.item.book.WitherMistBook;
import bassebombecraft.item.book.WitherSkullBook;
import bassebombecraft.item.inventory.AngelIdolInventoryItem;
import bassebombecraft.item.inventory.ChickenizeIdolInventoryItem;
import bassebombecraft.item.inventory.FlameBlastIdolInventoryItem;
import bassebombecraft.item.inventory.FlowerIdolInventoryItem;
import bassebombecraft.item.inventory.LightningBoltIdolInventoryItem;
import bassebombecraft.item.inventory.PvpIdolInventoryItem;
import bassebombecraft.item.inventory.RainIdolInventoryItem;
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
	 * Logger.
	 */
	static Logger logger = LogManager.getLogger(BassebombeCraft.class);

	/**
	 * Initialize items.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 */
	public void initialize(CreativeTabs targetTab) {
		// Config config = ConfigFactory.load(MODID);

		Item gardenStaff = new GardenStaff();
		gardenStaff.setCreativeTab(targetTab);
		GameRegistry.registerItem(gardenStaff, GardenStaff.ITEM_NAME);
		logger.info("initializing item: " + GardenStaff.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(gardenStaff), "  x", " / ", "/  ", 'x', Items.flower_pot, '/',
				Items.iron_shovel);

		Item explosionStaff = new ExplosionStaff();
		explosionStaff.setCreativeTab(targetTab);
		GameRegistry.registerItem(explosionStaff, ExplosionStaff.ITEM_NAME);
		logger.info("initializing item: " + ExplosionStaff.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(explosionStaff), "  x", " / ", "/  ", 'x', Items.flint, '/',
				Items.stick);

		Item parkourStaff = new ParkourStaff();
		parkourStaff.setCreativeTab(targetTab);
		GameRegistry.registerItem(parkourStaff, ParkourStaff.ITEM_NAME);
		logger.info("initializing item: " + ParkourStaff.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(parkourStaff), "  x", " / ", "/  ", 'x', Items.leather_leggings, '/',
				Items.stick);

		// Initialise books
		
		Item teleportBook = new TeleportBook();
		teleportBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(teleportBook, TeleportBook.ITEM_NAME);
		logger.info("initializing item: " + TeleportBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(teleportBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.ender_pearl);			
		
		Item smallFireballBook = new SmallFireballBook();
		smallFireballBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(smallFireballBook, SmallFireballBook.ITEM_NAME);
		logger.info("initializing item: " + SmallFireballBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(smallFireballBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.blaze_powder);

		Item largeFireballBook = new LargeFireballBook();
		largeFireballBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(largeFireballBook, LargeFireballBook.ITEM_NAME);
		logger.info("initializing item: " + LargeFireballBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(largeFireballBook), "xyy", "   ", "   ", 'x', Items.book, 'y',
				Items.magma_cream);

		Item lingeringFlameBook = new LingeringFlameBook();
		lingeringFlameBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(lingeringFlameBook, LingeringFlameBook.ITEM_NAME);
		logger.info("initializing item: " + LingeringFlameBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lingeringFlameBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.blaze_powder, 'z', Items.ghast_tear);

		Item lingeringFuryBook = new LingeringFuryBook();
		lingeringFuryBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(lingeringFuryBook, LingeringFuryBook.ITEM_NAME);
		logger.info("initializing item: " + LingeringFuryBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lingeringFuryBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.magma_cream, 'z', Items.ghast_tear);

		Item lavaSpiralMistBook = new LavaSpiralMistBook();
		lavaSpiralMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(lavaSpiralMistBook, LavaSpiralMistBook.ITEM_NAME);
		logger.info("initializing item: " + LavaSpiralMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lavaSpiralMistBook), "xyz", "xxx", "xxx", 'x', Items.book, 'y',
				Items.lava_bucket, 'z', Items.ghast_tear);
		
		Item toxicMistBook = new ToxicMistBook();
		toxicMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(toxicMistBook, ToxicMistBook.ITEM_NAME);
		logger.info("initializing item: " + ToxicMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(toxicMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.rotten_flesh, 'z', Items.ghast_tear);

		Item witherBook = new WitherSkullBook();
		witherBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(witherBook, WitherSkullBook.ITEM_NAME);
		logger.info("initializing item: " + WitherSkullBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(witherBook), "xyy", "   ", "   ", 'x', Items.book, 'y', Items.coal);

		Item witherMistBook = new WitherMistBook();
		witherMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(witherMistBook, WitherMistBook.ITEM_NAME);
		logger.info("initializing item: " + WitherMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(witherMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.coal, 'z', Items.ghast_tear);

		Item movingWitherMistBook = new MovingWitherMistBook();
		movingWitherMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(movingWitherMistBook, MovingWitherMistBook.ITEM_NAME);
		logger.info("initializing item: " + MovingWitherMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingWitherMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.fermented_spider_eye, 'z', Items.ghast_tear);

		Item movingLavaMistBook = new MovingLavaMistBook();
		movingLavaMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(movingLavaMistBook, MovingLavaMistBook.ITEM_NAME);
		logger.info("initializing item: " + MovingLavaMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingLavaMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.lava_bucket, 'z', Items.ghast_tear);

		Item movingLavaMultiMistBook = new MovingLavaMultiMistBook();
		movingLavaMultiMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(movingLavaMultiMistBook, MovingLavaMultiMistBook.ITEM_NAME);
		logger.info("initializing item: " + MovingLavaMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingLavaMultiMistBook), "xyz", "yzy", "   ", 'x', Items.book, 'y',
				Items.lava_bucket, 'z', Items.ghast_tear);

		Item movingIceMultiMistBook = new MovingIceMultiMistBook();
		movingIceMultiMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(movingIceMultiMistBook, MovingIceMultiMistBook.ITEM_NAME);
		logger.info("initializing item: " + MovingIceMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingIceMultiMistBook), "xyz", "yzy", "   ", 'x', Items.book, 'y',
				Items.snowball, 'z', Items.ghast_tear);

		Item movingWaterMultiMistBook = new MovingWaterMultiMistBook();
		movingWaterMultiMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(movingWaterMultiMistBook, MovingWaterMultiMistBook.ITEM_NAME);
		logger.info("initializing item: " + MovingWaterMultiMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingWaterMultiMistBook), "xyz", "yzy", "   ", 'x', Items.book, 'y',
				Items.water_bucket, 'z', Items.ghast_tear);
		
		Item movingRainbowMistBook = new MovingRainbowMistBook();
		movingRainbowMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(movingRainbowMistBook, MovingRainbowMistBook.ITEM_NAME);
		logger.info("initializing item: " + MovingRainbowMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(movingRainbowMistBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.record_13);
						
		Item healingMistBook = new HealingMistBook();
		healingMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(healingMistBook, HealingMistBook.ITEM_NAME);
		logger.info("initializing item: " + HealingMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(healingMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.golden_apple, 'z', Items.ghast_tear);

		// books of creature spawning
		
		Item spawnFlamingChickenBook = new SpawnFlamingChickenBook();
		spawnFlamingChickenBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(spawnFlamingChickenBook, SpawnFlamingChickenBook.ITEM_NAME);
		logger.info("initializing item: " + SpawnFlamingChickenBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnFlamingChickenBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.cooked_chicken);

		Item spawnSquidBook = new SpawnSquidBook();
		spawnSquidBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(spawnSquidBook, SpawnSquidBook.ITEM_NAME);
		logger.info("initializing item: " + SpawnSquidBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnSquidBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.dye);

		Item baconBazookaBook = new BaconBazookaBook();
		baconBazookaBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(baconBazookaBook, BaconBazookaBook.ITEM_NAME);
		logger.info("initializing item: " + BaconBazookaBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(baconBazookaBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.porkchop);

		Item creeperCannonBook = new CreeperCannonBook();
		creeperCannonBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(creeperCannonBook, CreeperCannonBook.ITEM_NAME);
		logger.info("initializing item: " + CreeperCannonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(creeperCannonBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.gunpowder);

		Item primedCreeperCannonBook = new PrimedCreeperCannonBook();
		primedCreeperCannonBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(primedCreeperCannonBook, PrimedCreeperCannonBook.ITEM_NAME);
		logger.info("initializing item: " + PrimedCreeperCannonBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(primedCreeperCannonBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.gunpowder,'z', Blocks.iron_block);

		Item creeperApocalypseBook = new CreeperApocalypseBook();
		creeperApocalypseBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(creeperApocalypseBook, CreeperApocalypseBook.ITEM_NAME);
		logger.info("initializing item: " + CreeperApocalypseBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(creeperApocalypseBook ), "xyz", "yzy", "zyz", 'x', Items.book, 'y',
				Items.gunpowder,'z', Blocks.iron_block);
		
		Item spawn100ChickensBook = new Spawn100ChickensBook();
		spawn100ChickensBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(spawn100ChickensBook, Spawn100ChickensBook.ITEM_NAME);
		logger.info("initializing item: " + Spawn100ChickensBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawn100ChickensBook), "xyy", "yyy", "yyy", 'x', Items.book, 'y',
				Items.chicken);

		Item spawnManyCowsBook = new SpawnManyCowsBook();
		spawnManyCowsBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(spawnManyCowsBook, SpawnManyCowsBook.ITEM_NAME);
		logger.info("initializing item: " + SpawnManyCowsBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnManyCowsBook), "xyy", "yyy", "yyy", 'x', Items.book, 'y',
				Items.beef);

		Item beastmasterMistBook = new BeastmasterMistBook();
		beastmasterMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(beastmasterMistBook, BeastmasterMistBook.ITEM_NAME);
		logger.info("initializing item: " + BeastmasterMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(beastmasterMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.name_tag, 'z', Items.ghast_tear);

		Item beastmasterBook = new BeastmasterBook();
		beastmasterBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(beastmasterBook, BeastmasterBook.ITEM_NAME);
		logger.info("initializing item: " + BeastmasterBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(beastmasterBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.name_tag);

		Item spawnGuardianBook = new SpawnGuardianBook();
		spawnGuardianBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(spawnGuardianBook, SpawnGuardianBook.ITEM_NAME);
		logger.info("initializing item: " + SpawnGuardianBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(spawnGuardianBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.name_tag, 'z', Items.iron_ingot);

		Item arrowBook = new MultipleArrowsBook();
		arrowBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(arrowBook, MultipleArrowsBook.ITEM_NAME);
		logger.info("initializing item: " + MultipleArrowsBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(arrowBook), "xyy", "yyy", "yyy", 'x', Items.book, 'y', Items.arrow);

		Item cobwebBook = new CobwebBook();
		cobwebBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(cobwebBook, CobwebBook.ITEM_NAME);
		logger.info("initializing item: " + CobwebBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(cobwebBook), "xyz", "   ", "   ", 'x', Items.book, 'y', Items.string,
				'z', Items.spider_eye);

		Item iceBlockBook = new IceBlockBook();
		iceBlockBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(iceBlockBook, IceBlockBook.ITEM_NAME);
		logger.info("initializing item: " + IceBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(iceBlockBook), "xyy", "   ", "   ", 'x', Items.book, 'y',
				Items.snowball);
		
		Item lavaBlockBook = new LavaBlockBook();
		lavaBlockBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(lavaBlockBook, LavaBlockBook.ITEM_NAME);
		logger.info("initializing item: " + LavaBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lavaBlockBook), "xyy", "   ", "   ", 'x', Items.book, 'y',
				Items.lava_bucket);

		Item lightningBoltBook = new LightningBoltBook();
		lightningBoltBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(lightningBoltBook, LightningBoltBook.ITEM_NAME);
		logger.info("initializing item: " + LightningBoltBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltBook), "xyy", "   ", "   ", 'x', Items.book, 'y',
				Items.gold_ingot);
		
		Item thunderMistBook = new LightningBoltMistBook();
		thunderMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(thunderMistBook, LightningBoltMistBook.ITEM_NAME);
		logger.info("initializing item: " + LightningBoltMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(thunderMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.gold_ingot, 'z', Items.ghast_tear);		
		
		Item fallingAnvilBook = new FallingAnvilBook();
		fallingAnvilBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(fallingAnvilBook, FallingAnvilBook.ITEM_NAME);
		logger.info("initializing item: " + FallingAnvilBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(fallingAnvilBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Blocks.anvil);
		
		Item emitHorizontalForceBook = new EmitHorizontalForceBook();
		emitHorizontalForceBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(emitHorizontalForceBook, EmitHorizontalForceBook.ITEM_NAME);
		logger.info("initializing item: " + EmitHorizontalForceBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitHorizontalForceBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.rabbit_foot, 'z', Items.fermented_spider_eye);

		Item emitVerticalForceBook = new EmitVerticalForceBook();
		emitVerticalForceBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(emitVerticalForceBook, EmitVerticalForceBook.ITEM_NAME);
		logger.info("initializing item: " + EmitVerticalForceBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitVerticalForceBook), "x  ", "y  ", "z  ", 'x', Items.book, 'y',
				Items.rabbit_foot, 'z', Items.fermented_spider_eye);

		Item emitVerticalForceMistBook = new EmitVerticalForceMistBook();
		emitVerticalForceMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(emitVerticalForceMistBook, EmitVerticalForceMistBook.ITEM_NAME);
		logger.info("initializing item: " + EmitVerticalForceMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(emitVerticalForceMistBook), "x  ", "y  ", "z  ", 'x', Items.book, 'y',
				Items.rabbit_foot, 'z', Items.ghast_tear);
		
		Item vacuumMistBook = new VacuumMistBook();
		vacuumMistBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(vacuumMistBook, VacuumMistBook.ITEM_NAME);
		logger.info("initializing item: " + VacuumMistBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(vacuumMistBook), "xyz", "   ", "   ", 'x', Items.book, 'y',
				Items.rabbit_foot, 'z', Items.ghast_tear);

		// books of block manipulation

		Item copyPasteBlocksBook = new CopyPasteBlocksBook();
		copyPasteBlocksBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(copyPasteBlocksBook, CopyPasteBlocksBook.ITEM_NAME);
		logger.info("initializing item: " + CopyPasteBlocksBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(copyPasteBlocksBook), "xyz", "   ", "   ", 'x', Items.written_book,
				'y', Items.nether_star, 'z', Items.fermented_spider_eye);

		Item duplicateBlockBook = new DuplicateBlockBook();
		duplicateBlockBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(duplicateBlockBook, DuplicateBlockBook.ITEM_NAME);
		logger.info("initializing item: " + DuplicateBlockBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(duplicateBlockBook), "xyz", "   ", "   ", 'x', Items.written_book,
				'y', Items.nether_star, 'z', Items.ender_pearl);

		// books of construction

		Item buildRoadBook = new BuildRoadBook();
		buildRoadBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(buildRoadBook, BuildRoadBook.ITEM_NAME);
		logger.info("initializing item: " + BuildRoadBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildRoadBook), "xy ", "   ", "   ", 'x', Items.written_book, 'y',
				Items.stone_pickaxe);

		Item buildRainbowRoadBook = new BuildRainbowRoadBook();
		buildRainbowRoadBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(buildRainbowRoadBook, BuildRainbowRoadBook.ITEM_NAME);
		logger.info("initializing item: " + BuildRainbowRoadBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildRainbowRoadBook), "xyy", "yyy", "yyy", 'x', Items.written_book,
				'y', Items.dye);

		Item buildMineBook = new BuildMineBook();
		buildMineBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(buildMineBook, BuildMineBook.ITEM_NAME);
		logger.info("initializing item: " + BuildMineBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildMineBook), "xy ", "   ", "   ", 'x', Items.written_book, 'y',
				Items.diamond_pickaxe);

		Item buildAbyssBook = new BuildAbyssBook();
		buildAbyssBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(buildAbyssBook, BuildAbyssBook.ITEM_NAME);
		logger.info("initializing item: " + BuildAbyssBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildAbyssBook), "xyz", "   ", "   ", 'x', Items.written_book, 'y',
				Items.iron_pickaxe, 'z', Items.water_bucket);

		Item buildSmallHoleBook = new BuildSmallHoleBook();
		buildSmallHoleBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(buildSmallHoleBook, BuildSmallHoleBook.ITEM_NAME);
		logger.info("initializing item: " + BuildSmallHoleBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(buildSmallHoleBook), "xy ", "   ", "   ", 'x', Items.written_book,
				'y', Items.iron_pickaxe);

		Item naturalizeBook = new NaturalizeBook();
		naturalizeBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(naturalizeBook, NaturalizeBook.ITEM_NAME);
		logger.info("initializing item: " + NaturalizeBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(naturalizeBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.flower_pot);

		Item rainbownizeBook = new RainbownizeBook();
		rainbownizeBook.setCreativeTab(targetTab);
		GameRegistry.registerItem(rainbownizeBook, RainbownizeBook.ITEM_NAME);
		logger.info("initializing item: " + RainbownizeBook.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainbownizeBook), "xy ", "   ", "   ", 'x', Items.book, 'y',
				Items.record_11);
		
		// Initialise inventory items
		
		Item rainIdolInventoryItem = new RainIdolInventoryItem();
		rainIdolInventoryItem.setCreativeTab(targetTab);
		GameRegistry.registerItem(rainIdolInventoryItem, RainIdolInventoryItem.ITEM_NAME);
		logger.info("initializing item: " + RainIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(rainIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.clay_ball, 'y',
				Items.water_bucket);			

		Item chickenizeIdolInventoryItem = new ChickenizeIdolInventoryItem();
		chickenizeIdolInventoryItem.setCreativeTab(targetTab);
		GameRegistry.registerItem(chickenizeIdolInventoryItem, ChickenizeIdolInventoryItem.ITEM_NAME);
		logger.info("initializing item: " + ChickenizeIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(chickenizeIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.clay_ball, 'y',
				Items.chicken);			

		Item angelIdolInventoryItem = new AngelIdolInventoryItem();
		angelIdolInventoryItem.setCreativeTab(targetTab);
		GameRegistry.registerItem(angelIdolInventoryItem, AngelIdolInventoryItem.ITEM_NAME);
		logger.info("initializing item: " + AngelIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(angelIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.clay_ball, 'y',
				Items.golden_apple);			

		Item lightningBoltIdolInventoryItem = new LightningBoltIdolInventoryItem();
		lightningBoltIdolInventoryItem.setCreativeTab(targetTab);
		GameRegistry.registerItem(lightningBoltIdolInventoryItem, LightningBoltIdolInventoryItem.ITEM_NAME);
		logger.info("initializing item: " + LightningBoltIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(lightningBoltIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.clay_ball, 'y',
				Items.gold_ingot);			

		Item flowerIdolInventoryItem = new FlowerIdolInventoryItem();
		flowerIdolInventoryItem.setCreativeTab(targetTab);
		GameRegistry.registerItem(flowerIdolInventoryItem , FlowerIdolInventoryItem.ITEM_NAME);
		logger.info("initializing item: " + FlowerIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(flowerIdolInventoryItem ), "xy ", "   ", "   ", 'x', Items.clay_ball, 'y',
				Items.flower_pot);			

		Item flameBlastIdolInventoryItem = new FlameBlastIdolInventoryItem();
		flameBlastIdolInventoryItem.setCreativeTab(targetTab);
		GameRegistry.registerItem(flameBlastIdolInventoryItem, FlameBlastIdolInventoryItem.ITEM_NAME);
		logger.info("initializing item: " + FlameBlastIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(flameBlastIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.clay_ball, 'y',
				Items.fire_charge);			

		/**
		Item pvpIdolInventoryItem = new PvpIdolInventoryItem();
		pvpIdolInventoryItem.setCreativeTab(targetTab);
		GameRegistry.registerItem(pvpIdolInventoryItem, PvpIdolInventoryItem.ITEM_NAME);
		logger.info("initializing item: " + PvpIdolInventoryItem.ITEM_NAME);
		GameRegistry.addShapedRecipe(new ItemStack(pvpIdolInventoryItem), "xy ", "   ", "   ", 'x', Items.clay_ball, 'y',
				Items.iron_sword);			
		**/
	}

	public static ItemInitializer getInstance() {
		return new ItemInitializer();
	}

}
