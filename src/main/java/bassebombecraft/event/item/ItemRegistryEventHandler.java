package bassebombecraft.event.item;

import static bassebombecraft.ModConstants.BUILD_MINE_BOOK;
import static bassebombecraft.ModConstants.HUD_ITEM;

import bassebombecraft.item.basic.TerminatorEyeItem;
import bassebombecraft.item.baton.MobCommandersBaton;
import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BearBlasterBook;
import bassebombecraft.item.book.BeastmasterBook;
import bassebombecraft.item.book.BeastmasterMistBook;
import bassebombecraft.item.book.BuildAbyssBook;
import bassebombecraft.item.book.BuildRainbowRoadBook;
import bassebombecraft.item.book.BuildRoadBook;
import bassebombecraft.item.book.BuildSmallHoleBook;
import bassebombecraft.item.book.BuildStairsBook;
import bassebombecraft.item.book.BuildTowerBook;
import bassebombecraft.item.book.CobwebBook;
import bassebombecraft.item.book.CopyPasteBlocksBook;
import bassebombecraft.item.book.CreeperApocalypseBook;
import bassebombecraft.item.book.CreeperCannonBook;
import bassebombecraft.item.book.DecoyBook;
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
import bassebombecraft.item.book.ReceiveAggroBook;
import bassebombecraft.item.book.SetSpawnPointBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.SmallFireballRingBook;
import bassebombecraft.item.book.Spawn100ChickensBook;
import bassebombecraft.item.book.Spawn100RainingLlamasBook;
import bassebombecraft.item.book.SpawnCreeperArmyBook;
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
import bassebombecraft.item.composite.CompositeMagic;
import bassebombecraft.item.composite.projectile.ArrowProjectileItem;
import bassebombecraft.item.composite.projectile.EggProjectileItem;
import bassebombecraft.item.composite.projectile.FireballProjectileItem;
import bassebombecraft.item.composite.projectile.LargeFireballProjectileItem;
import bassebombecraft.item.composite.projectile.WitherSkullProjectileItem;
import bassebombecraft.item.composite.projectile.formation.CircleProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.FrontAndBackProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.RandomSingleProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.SingleProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.TrifurcatedProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.modifier.InaccuracyProjectileFormationModifierItem;
import bassebombecraft.item.composite.projectile.formation.modifier.OscillatingRotation180DProjectileFormationModifierItem;
import bassebombecraft.item.composite.projectile.formation.modifier.RandomProjectileFormationModifierItem;
import bassebombecraft.item.composite.projectile.modifier.CharmProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.DecoyProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.DigMobHoleProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.ExplodeProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.MeteorProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.RandomPathProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.TeleportInvokerProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.TeleportMobProjectileModifierItem;
import bassebombecraft.item.inventory.AngelIdolInventoryItem;
import bassebombecraft.item.inventory.AngryParrotsIdolInventoryItem;
import bassebombecraft.item.inventory.BlindnessIdolInventoryItem;
import bassebombecraft.item.inventory.CharmBeastIdolInventoryItem;
import bassebombecraft.item.inventory.ChickenizeIdolInventoryItem;
import bassebombecraft.item.inventory.DecreaseSizeIdolInventoryItem;
import bassebombecraft.item.inventory.EggProjectileIdolInventoryItem;
import bassebombecraft.item.inventory.FlameBlastIdolInventoryItem;
import bassebombecraft.item.inventory.FlowerIdolInventoryItem;
import bassebombecraft.item.inventory.IncreaseSizeIdolInventoryItem;
import bassebombecraft.item.inventory.KillerBeesIdolInventoryItem;
import bassebombecraft.item.inventory.LevitationIdolInventoryItem;
import bassebombecraft.item.inventory.LightningBoltIdolInventoryItem;
import bassebombecraft.item.inventory.LlamaSpitIdolInventoryItem;
import bassebombecraft.item.inventory.MassExtinctionEventIdolInventoryItem;
import bassebombecraft.item.inventory.MeteorIdolInventoryItem;
import bassebombecraft.item.inventory.MobsAggroIdolInventoryItem;
import bassebombecraft.item.inventory.MobsLevitationIdolInventoryItem;
import bassebombecraft.item.inventory.PinkynizeIdolInventoryItem;
import bassebombecraft.item.inventory.PlayerAggroIdolInventoryItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import bassebombecraft.item.inventory.RainIdolInventoryItem;
import bassebombecraft.item.inventory.RainbownizeIdolInventoryItem;
import bassebombecraft.item.inventory.ReaperIdolInventoryItem;
import bassebombecraft.item.inventory.ReflectIdolInventoryItem;
import bassebombecraft.item.inventory.RemoveBlockSpiralIdolInventoryItem;
import bassebombecraft.item.inventory.RespawnIdolInventoryItem;
import bassebombecraft.item.inventory.SaturationIdolInventoryItem;
import bassebombecraft.item.inventory.WarPigsIdolInventoryItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Event handler for registration of items.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistryEventHandler {

	/**
	 * The set of book items.
	 */
	static Item[] bookItems = { new TeleportBook(), new SetSpawnPointBook(), new SmallFireballBook(),
			new LargeFireballBook(), new SmallFireballRingBook(), new LingeringFlameBook(), new LingeringFuryBook(),
			new LavaSpiralMistBook(), new ToxicMistBook(), new WitherSkullBook(), new WitherMistBook(),
			new MovingWitherMistBook(), new MovingLavaMistBook(), new MovingLavaMultiMistBook(),
			new MovingIceMultiMistBook(), new MovingRainbowMistBook(), new MovingWaterMultiMistBook(),
			new HealingMistBook(), new MovingTntMistBook(), new SpawnFlamingChickenBook(), new SpawnSquidBook(),
			new BaconBazookaBook(), new CreeperCannonBook(), new PrimedCreeperCannonBook(), new BearBlasterBook(),
			new CreeperApocalypseBook(), new Spawn100ChickensBook(), new SpawnManyCowsBook(),
			new Spawn100RainingLlamasBook(), new SpawnKittenArmyBook(), new SpawnSkeletonArmyBook(),
			new SpawnCreeperArmyBook(), new SpawnGiantZombieBook(), new BeastmasterMistBook(), new BeastmasterBook(),
			new SpawnGuardianBook(), new SpawnDragonBook(), new MultipleArrowsBook(), new CobwebBook(),
			new IceBlockBook(), new LavaBlockBook(), new DigMobHoleBook(), new LightningBoltBook(),
			new LightningBoltMistBook(), new FallingAnvilBook(), new EmitHorizontalForceBook(),
			new EmitVerticalForceBook(), new EmitVerticalForceMistBook(), new BuildStairsBook(), new VacuumMistBook(),
			new CopyPasteBlocksBook(), new DuplicateBlockBook(), new BuildRoadBook(), new BuildRainbowRoadBook(),
			BUILD_MINE_BOOK, new BuildAbyssBook(), new BuildSmallHoleBook(), new NaturalizeBook(),
			new RainbownizeBook(), new BuildTowerBook(), new DecoyBook(), new ReceiveAggroBook() };

	/**
	 * The set of inventory items.
	 */
	static Item[] inventoryItems = { new RainIdolInventoryItem(), new ChickenizeIdolInventoryItem(),
			new AngelIdolInventoryItem(), new LevitationIdolInventoryItem(), new LightningBoltIdolInventoryItem(),
			new FlowerIdolInventoryItem(), new RainbownizeIdolInventoryItem(), new FlameBlastIdolInventoryItem(),
			new CharmBeastIdolInventoryItem(), new BlindnessIdolInventoryItem(), new PinkynizeIdolInventoryItem(),
			new PrimeMobIdolInventoryItem(), new LlamaSpitIdolInventoryItem(), new EggProjectileIdolInventoryItem(),
			new MeteorIdolInventoryItem(), new SaturationIdolInventoryItem(), new MobsAggroIdolInventoryItem(),
			new ReaperIdolInventoryItem(), new MassExtinctionEventIdolInventoryItem(),
			new MobsLevitationIdolInventoryItem(), new PlayerAggroIdolInventoryItem(),
			new AngryParrotsIdolInventoryItem(), new ReflectIdolInventoryItem(), new KillerBeesIdolInventoryItem(),
			new WarPigsIdolInventoryItem(), new DecreaseSizeIdolInventoryItem(), new IncreaseSizeIdolInventoryItem(),
			new RespawnIdolInventoryItem(), new RemoveBlockSpiralIdolInventoryItem() };

	/**
	 * The set of baton items.
	 */
	static Item[] batonItems = { new MobCommandersBaton() };

	/**
	 * The set of basic items.
	 */
	static Item[] basicItems = { new TerminatorEyeItem(), HUD_ITEM };

	/**
	 * The set of composite items.
	 */
	static Item[] compositeItems = { new CompositeMagic(), new SingleProjectileFormationItem(),
			new RandomSingleProjectileFormationItem(), new CircleProjectileFormationItem(),
			new TrifurcatedProjectileFormationItem(), new FrontAndBackProjectileFormationItem(),
			new FireballProjectileItem(), new LargeFireballProjectileItem(), new ArrowProjectileItem(),
			new WitherSkullProjectileItem(), new EggProjectileItem(), new RandomProjectileFormationModifierItem(),
			new InaccuracyProjectileFormationModifierItem(),
			new OscillatingRotation180DProjectileFormationModifierItem(), new TeleportInvokerProjectileModifierItem(),
			new TeleportMobProjectileModifierItem(), new CharmProjectileModifierItem(),
			new MeteorProjectileModifierItem(), new DecoyProjectileModifierItem(), new ExplodeProjectileModifierItem(),
			new DigMobHoleProjectileModifierItem(), new RandomPathProjectileModifierItem() };

	/**
	 * Handle {@linkplain RegistryEvent.Register<Item>} event to register items with
	 * forge.
	 * 
	 * @param event register item event.
	 */
	@SubscribeEvent
	public static void handleRegisterItem(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.registerAll(bookItems);
		registry.registerAll(inventoryItems);
		registry.registerAll(batonItems);
		registry.registerAll(basicItems);
		registry.registerAll(compositeItems);
	}

	/**
	 * Get inventory items.
	 * 
	 * @return inventory items.
	 */
	public static Item[] getInventoryItems() {
		return inventoryItems;
	}

}
