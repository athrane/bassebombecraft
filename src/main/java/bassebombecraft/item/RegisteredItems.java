package bassebombecraft.item;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

import java.util.function.Supplier;

import bassebombecraft.item.basic.HudItem;
import bassebombecraft.item.basic.TerminatorEyeItem;
import bassebombecraft.item.baton.MobCommandersBaton;
import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BearBlasterBook;
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
import bassebombecraft.item.composite.CompositeMagicItem;
import bassebombecraft.item.composite.projectile.ArrowProjectileItem;
import bassebombecraft.item.composite.projectile.EggProjectileItem;
import bassebombecraft.item.composite.projectile.LargeFireballProjectileItem;
import bassebombecraft.item.composite.projectile.LightningProjectileItem;
import bassebombecraft.item.composite.projectile.LlamaProjectileItem;
import bassebombecraft.item.composite.projectile.SmallFireballProjectileItem;
import bassebombecraft.item.composite.projectile.WitherSkullProjectileItem;
import bassebombecraft.item.composite.projectile.formation.CircleProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.FrontAndBackProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.RandomSingleProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.SingleProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.TrifurcatedProjectileFormationItem;
import bassebombecraft.item.composite.projectile.formation.modifier.InaccuracyProjectileFormationModifierItem;
import bassebombecraft.item.composite.projectile.formation.modifier.OscillatingRotation180DProjectileFormationModifierItem;
import bassebombecraft.item.composite.projectile.formation.modifier.RandomProjectileFormationModifierItem;
import bassebombecraft.item.composite.projectile.modifier.BounceProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.CharmProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.ContagionProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.DecoyProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.DigMobHoleProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.DigProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.ElectrocuteProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.EmitHorizontalForceProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.EmitVerticalForceProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.ExplodeMobWhenKilledProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.ExplodeOnImpactProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.LightningProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.MeteorProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.ReceiveAggroProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.RespawnProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.SpawnAnvilProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.SpawnCobwebProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.SpawnFlamingChickenProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.SpawnIceBlockProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.SpawnLavaBlockProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.SpawnSquidProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.TeleportInvokerProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.TeleportMobProjectileModifierItem;
import bassebombecraft.item.composite.projectile.modifier.WildfireProjectileModifierItem;
import bassebombecraft.item.composite.projectile.path.AccelerateProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.CircleProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.DeaccelerateProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.DecreaseGravityProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.HomingProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.IncreaseGravityProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.RandomProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.SineProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.TeleportProjectilePathItem;
import bassebombecraft.item.composite.projectile.path.ZigZagProjectilePathItem;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered items.
 */
public class RegisteredItems {

	/**
	 * Deferred registry for registration of items.
	 */
	public static final DeferredRegister<Item> ITEMS_REGISTRY = create(ITEMS, MODID);

	/**
	 * Other items
	 */
	public static final RegistryObject<Item> BATON = register(MobCommandersBaton::new);
	public static final RegistryObject<Item> TERMINATOR_EYE = register(TerminatorEyeItem::new);
	public static final RegistryObject<Item> HUD = register(HudItem::new);

	/**
	 * Composite items.
	 */
	public static final RegistryObject<Item> COMPOSITE = register(CompositeMagicItem::new);
	public static final RegistryObject<Item> FORMATION1 = register(SingleProjectileFormationItem::new);
	public static final RegistryObject<Item> FORMATION2 = register(RandomSingleProjectileFormationItem::new);
	public static final RegistryObject<Item> FORMATION3 = register(CircleProjectileFormationItem::new);
	public static final RegistryObject<Item> FORMATION4 = register(TrifurcatedProjectileFormationItem::new);
	public static final RegistryObject<Item> FORMATION5 = register(FrontAndBackProjectileFormationItem::new);
	public static final RegistryObject<Item> FORMATION_MOD1 = register(RandomProjectileFormationModifierItem::new);
	public static final RegistryObject<Item> FORMATION_MOD2 = register(InaccuracyProjectileFormationModifierItem::new);
	public static final RegistryObject<Item> FORMATION_MOD3 = register(
			OscillatingRotation180DProjectileFormationModifierItem::new);
	public static final RegistryObject<Item> PROJECTILE1 = register(LlamaProjectileItem::new);
	public static final RegistryObject<Item> PROJECTILE2 = register(EggProjectileItem::new);
	public static final RegistryObject<Item> PROJECTILE3 = register(LightningProjectileItem::new);
	public static final RegistryObject<Item> PROJECTILE4 = register(LargeFireballProjectileItem::new);		
	public static final RegistryObject<Item> PROJECTILE5 = register(SmallFireballProjectileItem::new);			
	public static final RegistryObject<Item> PROJECTILE6 = register(ArrowProjectileItem::new);		
	public static final RegistryObject<Item> PROJECTILE7 = register(WitherSkullProjectileItem::new);			
	public static final RegistryObject<Item> PROJECTILE_PATH1 = register(RandomProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH2 = register(AccelerateProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH3 = register(DeaccelerateProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH4 = register(ZigZagProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH5 = register(SineProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH6 = register(CircleProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH7 = register(IncreaseGravityProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH8 = register(DecreaseGravityProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH9 = register(TeleportProjectilePathItem::new);
	public static final RegistryObject<Item> PROJECTILE_PATH10 = register(HomingProjectilePathItem::new);
	public static final RegistryObject<Item> MODIFIER1 = register(TeleportInvokerProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER2 = register(TeleportMobProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER3 = register(CharmProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER4 = register(MeteorProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER5 = register(DecoyProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER6 = register(ExplodeMobWhenKilledProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER7 = register(DigMobHoleProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER8 = register(DigProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER9 = register(ExplodeOnImpactProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER10 = register(SpawnCobwebProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER11 = register(SpawnIceBlockProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER12 = register(SpawnLavaBlockProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER13 = register(SpawnAnvilProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER14 = register(ReceiveAggroProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER15 = register(BounceProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER16 = register(EmitHorizontalForceProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER17 = register(EmitVerticalForceProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER18 = register(RespawnProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER19 = register(LightningProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER20 = register(SpawnSquidProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER21 = register(ElectrocuteProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER22 = register(SpawnFlamingChickenProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER23 = register(WildfireProjectileModifierItem::new);
	public static final RegistryObject<Item> MODIFIER24 = register(ContagionProjectileModifierItem::new);
	
	/**
	 * Books
	 */
	public static final RegistryObject<Item> TELEPORT_BOOK = register(TeleportBook::new);
	public static final RegistryObject<Item> FIREBALL_BOOK = register(SmallFireballBook::new);
	public static final RegistryObject<Item> FIREBALL2_BOOK = register(LargeFireballBook::new);
	public static final RegistryObject<Item> FIREBALL_RING_BOOK = register(SmallFireballRingBook::new);
	public static final RegistryObject<Item> LINGERING_FLAME_BOOK = register(LingeringFlameBook::new);
	public static final RegistryObject<Item> LINGERING_FURY_BOOK = register(LingeringFuryBook::new);
	public static final RegistryObject<Item> LAVA_SPIRAL_BOOK = register(LavaSpiralMistBook::new);
	public static final RegistryObject<Item> TOXIC_MIST_BOOK = register(ToxicMistBook::new);
	public static final RegistryObject<Item> WITHER_BOOK = register(WitherSkullBook::new);
	public static final RegistryObject<Item> WITHER_MIST_BOOK = register(WitherMistBook::new);
	public static final RegistryObject<Item> MOVING_WITHER_MIST_BOOK = register(MovingWitherMistBook::new);
	public static final RegistryObject<Item> MOVING_LAVA_MIST_BOOK = register(MovingLavaMistBook::new);
	public static final RegistryObject<Item> MULTI_MOVING_LAVA_MIST_BOOK = register(MovingLavaMultiMistBook::new);
	public static final RegistryObject<Item> MULTI_MOVING_ICE_MIST_BOOK = register(MovingIceMultiMistBook::new);
	public static final RegistryObject<Item> MULTI_MOVING_RAINBOW_MIST_BOOK = register(MovingRainbowMistBook::new);
	public static final RegistryObject<Item> MULTI_MOVING_WATER_MIST_BOOK = register(MovingWaterMultiMistBook::new);
	public static final RegistryObject<Item> HEAL_MIST_BOOK = register(HealingMistBook::new);
	public static final RegistryObject<Item> MULTI_MOVING_TNT_MIST_BOOK = register(MovingTntMistBook::new);
	public static final RegistryObject<Item> FLAMING_CHICKEN_BOOK = register(SpawnFlamingChickenBook::new);
	public static final RegistryObject<Item> SQUID_BOOK = register(SpawnSquidBook::new);
	public static final RegistryObject<Item> BACON_BOOK = register(BaconBazookaBook::new);
	public static final RegistryObject<Item> CREEPER_BOOK = register(CreeperCannonBook::new);
	public static final RegistryObject<Item> PRIMED_CREEPER_BOOK = register(PrimedCreeperCannonBook::new);
	public static final RegistryObject<Item> BEAR_BOOK = register(BearBlasterBook::new);
	public static final RegistryObject<Item> APOCALYPSE_BOOK = register(CreeperApocalypseBook::new);
	public static final RegistryObject<Item> CHICKEN1S_BOOK = register(Spawn100ChickensBook::new);
	public static final RegistryObject<Item> COWS_BOOK = register(SpawnManyCowsBook::new);
	public static final RegistryObject<Item> LLAMAS_BOOK = register(Spawn100RainingLlamasBook::new);
	public static final RegistryObject<Item> KITTENS_BOOK = register(SpawnKittenArmyBook::new);
	public static final RegistryObject<Item> SKELETONS_BOOK = register(SpawnSkeletonArmyBook::new);
	public static final RegistryObject<Item> CREEPERS_BOOK = register(SpawnCreeperArmyBook::new);
	public static final RegistryObject<Item> ZOMBIE_BOOK = register(SpawnGiantZombieBook::new);
	public static final RegistryObject<Item> BEASTMASTER_MIST_BOOK = register(BeastmasterMistBook::new);
	public static final RegistryObject<Item> BEASTMASTER_BOOK = register(BeastmasterBook::new);
	public static final RegistryObject<Item> GUARDIAN_BOOK = register(SpawnGuardianBook::new);
	public static final RegistryObject<Item> DRAGON_BOOK = register(SpawnDragonBook::new);
	public static final RegistryObject<Item> ARROWS_BOOK = register(MultipleArrowsBook::new);
	public static final RegistryObject<Item> COBWEB_BOOK = register(CobwebBook::new);
	public static final RegistryObject<Item> ICE_BLOCK_BOOK = register(IceBlockBook::new);
	public static final RegistryObject<Item> LAVA_BLOCK_BOOK = register(LavaBlockBook::new);
	public static final RegistryObject<Item> HOLE_BOOK = register(DigMobHoleBook::new);
	public static final RegistryObject<Item> LIGHTNING_BOOK = register(LightningBoltBook::new);
	public static final RegistryObject<Item> LIGHTNING_MIST_BOOK = register(LightningBoltMistBook::new);
	public static final RegistryObject<Item> ANVIL_BOOK = register(FallingAnvilBook::new);
	public static final RegistryObject<Item> HORZ_FORCE_BOOK = register(EmitHorizontalForceBook::new);
	public static final RegistryObject<Item> VERT_FORCE_BOOK = register(EmitVerticalForceBook::new);
	public static final RegistryObject<Item> VERT_FORCE_MIST_BOOK = register(EmitVerticalForceMistBook::new);
	public static final RegistryObject<Item> STAIRS_BOOK = register(BuildStairsBook::new);
	public static final RegistryObject<Item> VACUUM_MIST_BOOK = register(VacuumMistBook::new);
	public static final RegistryObject<Item> COPY_PASTE_BOOK = register(CopyPasteBlocksBook::new);
	public static final RegistryObject<Item> DUPLICATE_BOOK = register(DuplicateBlockBook::new);
	public static final RegistryObject<Item> ROAD_BOOK = register(BuildRoadBook::new);
	public static final RegistryObject<Item> RAINBOW_ROAD_BOOK = register(BuildRainbowRoadBook::new);
	public static final RegistryObject<Item> MINE_BOOK = register(BuildMineBook::new);
	public static final RegistryObject<Item> ABYSS_BOOK = register(BuildAbyssBook::new);
	public static final RegistryObject<Item> SMALL_HOLE_BOOK = register(BuildSmallHoleBook::new);
	public static final RegistryObject<Item> NATURALIZE_BOOK = register(NaturalizeBook::new);
	public static final RegistryObject<Item> RAINBOWNIZE_BOOK = register(RainbownizeBook::new);
	public static final RegistryObject<Item> TOWER_BOOK = register(BuildTowerBook::new);
	public static final RegistryObject<Item> DECOY_BOOK = register(DecoyBook::new);
	public static final RegistryObject<Item> AGGRO_BOOK = register(ReceiveAggroBook::new);

	/**
	 * Idols
	 */
	public static final RegistryObject<Item> RAIN_IDOL = register(RainIdolInventoryItem::new);
	public static final RegistryObject<Item> CHICKENIZE_IDOL = register(ChickenizeIdolInventoryItem::new);
	public static final RegistryObject<Item> ANGEL_IDOL = register(AngelIdolInventoryItem::new);
	public static final RegistryObject<Item> LEVITATION_IDOL = register(LevitationIdolInventoryItem::new);
	public static final RegistryObject<Item> LIGHTNING_IDOL = register(LightningBoltIdolInventoryItem::new);
	public static final RegistryObject<Item> FLOWER_IDOL = register(FlowerIdolInventoryItem::new);
	public static final RegistryObject<Item> RAINBOWNIZE_IDOL = register(RainbownizeIdolInventoryItem::new);
	public static final RegistryObject<Item> FLAMEBLAST_IDOL = register(FlameBlastIdolInventoryItem::new);
	public static final RegistryObject<Item> CHARM_IDOL = register(CharmBeastIdolInventoryItem::new);
	public static final RegistryObject<Item> BLINDNESS_IDOL = register(BlindnessIdolInventoryItem::new);
	public static final RegistryObject<Item> PINKYNIZE_IDOL = register(PinkynizeIdolInventoryItem::new);
	public static final RegistryObject<Item> PRIME_IDOL = register(PrimeMobIdolInventoryItem::new);
	public static final RegistryObject<Item> LLAMA_IDOL = register(LlamaSpitIdolInventoryItem::new);
	public static final RegistryObject<Item> EGG_IDOL = register(EggProjectileIdolInventoryItem::new);
	public static final RegistryObject<Item> METEOR_IDOL = register(MeteorIdolInventoryItem::new);
	public static final RegistryObject<Item> SATURATION_IDOL = register(SaturationIdolInventoryItem::new);
	public static final RegistryObject<Item> MOBAGGRO_IDOL = register(MobsAggroIdolInventoryItem::new);
	public static final RegistryObject<Item> REAPER_IDOL = register(ReaperIdolInventoryItem::new);
	public static final RegistryObject<Item> MASSEXTINCT_IDOL = register(MassExtinctionEventIdolInventoryItem::new);
	public static final RegistryObject<Item> MOBSLEVITATION_IDOL = register(MobsLevitationIdolInventoryItem::new);
	public static final RegistryObject<Item> PLAYERAGGRO_IDOL = register(PlayerAggroIdolInventoryItem::new);
	public static final RegistryObject<Item> PARROTS_IDOL = register(AngryParrotsIdolInventoryItem::new);
	public static final RegistryObject<Item> REFLECT_IDOL = register(ReflectIdolInventoryItem::new);
	public static final RegistryObject<Item> BEES_IDOL = register(KillerBeesIdolInventoryItem::new);
	public static final RegistryObject<Item> PIGS_IDOL = register(WarPigsIdolInventoryItem::new);
	public static final RegistryObject<Item> DECSIZE_IDOL = register(DecreaseSizeIdolInventoryItem::new);
	public static final RegistryObject<Item> INCSIZE_IDOL = register(IncreaseSizeIdolInventoryItem::new);
	public static final RegistryObject<Item> BLOCK_IDOL = register(RemoveBlockSpiralIdolInventoryItem::new);
	public static final RegistryObject<Item> RESPAWN_IDOL = register(RespawnIdolInventoryItem::new);

	/**
	 * Register item.
	 * 
	 * @param splItem supplier used to create item instance..
	 * 
	 * @return registry object.
	 */
	static RegistryObject<Item> register(Supplier<Item> splItem) {
		String key = splItem.get().getClass().getSimpleName().toLowerCase();
		return ITEMS_REGISTRY.register(key, splItem);
	}

}
