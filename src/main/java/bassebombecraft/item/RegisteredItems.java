package bassebombecraft.item;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

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
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
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
	public static final RegistryObject<Item> BATON = register(MobCommandersBaton.class);
	public static final RegistryObject<Item> TERMINATOR_EYE = register(TerminatorEyeItem.class);
	public static final RegistryObject<Item> HUD = register(HudItem.class);

	/**
	 * Composite items.
	 */
	public static final RegistryObject<Item> COMPOSITE = register(CompositeMagicItem.class);
	public static final RegistryObject<Item> FORMATION1 = register(SingleProjectileFormationItem.class);
	public static final RegistryObject<Item> FORMATION2 = register(RandomSingleProjectileFormationItem.class);
	public static final RegistryObject<Item> FORMATION3 = register(CircleProjectileFormationItem.class);
	public static final RegistryObject<Item> FORMATION4 = register(TrifurcatedProjectileFormationItem.class);
	public static final RegistryObject<Item> FORMATION5 = register(FrontAndBackProjectileFormationItem.class);
	public static final RegistryObject<Item> FORMATION_MOD1 = register(RandomProjectileFormationModifierItem.class);
	public static final RegistryObject<Item> FORMATION_MOD2 = register(InaccuracyProjectileFormationModifierItem.class);
	public static final RegistryObject<Item> FORMATION_MOD3 = register(OscillatingRotation180DProjectileFormationModifierItem.class);
	public static final RegistryObject<Item> PROJECTILE1 = register(LlamaProjectileItem.class);
	public static final RegistryObject<Item> PROJECTILE2 = register(EggProjectileItem.class);
	public static final RegistryObject<Item> PROJECTILE3 = register(LightningProjectileItem.class);
	public static final RegistryObject<Item> PROJECTILE4 = register(LargeFireballProjectileItem.class);
	public static final RegistryObject<Item> PROJECTILE5 = register(SmallFireballProjectileItem.class);
	public static final RegistryObject<Item> PROJECTILE6 = register(ArrowProjectileItem.class);
	public static final RegistryObject<Item> PROJECTILE7 = register(WitherSkullProjectileItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH1 = register(RandomProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH2 = register(AccelerateProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH3 = register(DeaccelerateProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH4 = register(ZigZagProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH5 = register(SineProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH6 = register(CircleProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH7 = register(IncreaseGravityProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH8 = register(DecreaseGravityProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH9 = register(TeleportProjectilePathItem.class);
	public static final RegistryObject<Item> PROJECTILE_PATH10 = register(HomingProjectilePathItem.class);
	public static final RegistryObject<Item> MODIFIER1 = register(TeleportInvokerProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER2 = register(TeleportMobProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER3 = register(CharmProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER4 = register(MeteorProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER5 = register(DecoyProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER6 = register(ExplodeMobWhenKilledProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER7 = register(DigMobHoleProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER8 = register(DigProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER9 = register(ExplodeOnImpactProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER10 = register(SpawnCobwebProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER11 = register(SpawnIceBlockProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER12 = register(SpawnLavaBlockProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER13 = register(SpawnAnvilProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER14 = register(ReceiveAggroProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER15 = register(BounceProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER16 = register(EmitHorizontalForceProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER17 = register(EmitVerticalForceProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER18 = register(RespawnProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER19 = register(LightningProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER20 = register(SpawnSquidProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER21 = register(ElectrocuteProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER22 = register(SpawnFlamingChickenProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER23 = register(WildfireProjectileModifierItem.class);
	public static final RegistryObject<Item> MODIFIER24 = register(ContagionProjectileModifierItem.class);
	
	/**
	 * Books
	 */
	public static final RegistryObject<Item> TELEPORT_BOOK = register(TeleportBook.class);
	public static final RegistryObject<Item> FIREBALL_BOOK = register(SmallFireballBook.class);
	public static final RegistryObject<Item> FIREBALL2_BOOK = register(LargeFireballBook.class);
	public static final RegistryObject<Item> FIREBALL_RING_BOOK = register(SmallFireballRingBook.class);
	public static final RegistryObject<Item> LINGERING_FLAME_BOOK = register(LingeringFlameBook.class);
	public static final RegistryObject<Item> LINGERING_FURY_BOOK = register(LingeringFuryBook.class);
	public static final RegistryObject<Item> LAVA_SPIRAL_BOOK = register(LavaSpiralMistBook.class);
	public static final RegistryObject<Item> TOXIC_MIST_BOOK = register(ToxicMistBook.class);
	public static final RegistryObject<Item> WITHER_BOOK = register(WitherSkullBook.class);
	public static final RegistryObject<Item> WITHER_MIST_BOOK = register(WitherMistBook.class);
	public static final RegistryObject<Item> MOVING_WITHER_MIST_BOOK = register(MovingWitherMistBook.class);
	public static final RegistryObject<Item> MOVING_LAVA_MIST_BOOK = register(MovingLavaMistBook.class);
	public static final RegistryObject<Item> MULTI_MOVING_LAVA_MIST_BOOK = register(MovingLavaMultiMistBook.class);
	public static final RegistryObject<Item> MULTI_MOVING_ICE_MIST_BOOK = register(MovingIceMultiMistBook.class);
	public static final RegistryObject<Item> MULTI_MOVING_RAINBOW_MIST_BOOK = register(MovingRainbowMistBook.class);
	public static final RegistryObject<Item> MULTI_MOVING_WATER_MIST_BOOK = register(MovingWaterMultiMistBook.class);
	public static final RegistryObject<Item> HEAL_MIST_BOOK = register(HealingMistBook.class);
	public static final RegistryObject<Item> MULTI_MOVING_TNT_MIST_BOOK = register(MovingTntMistBook.class);
	public static final RegistryObject<Item> FLAMING_CHICKEN_BOOK = register(SpawnFlamingChickenBook.class);
	public static final RegistryObject<Item> SQUID_BOOK = register(SpawnSquidBook.class);
	public static final RegistryObject<Item> BACON_BOOK = register(BaconBazookaBook.class);
	public static final RegistryObject<Item> CREEPER_BOOK = register(CreeperCannonBook.class);
	public static final RegistryObject<Item> PRIMED_CREEPER_BOOK = register(PrimedCreeperCannonBook.class);
	public static final RegistryObject<Item> BEAR_BOOK = register(BearBlasterBook.class);
	public static final RegistryObject<Item> APOCALYPSE_BOOK = register(CreeperApocalypseBook.class);
	public static final RegistryObject<Item> CHICKEN1S_BOOK = register(Spawn100ChickensBook.class);
	public static final RegistryObject<Item> COWS_BOOK = register(SpawnManyCowsBook.class);
	public static final RegistryObject<Item> LLAMAS_BOOK = register(Spawn100RainingLlamasBook.class);
	public static final RegistryObject<Item> KITTENS_BOOK = register(SpawnKittenArmyBook.class);
	public static final RegistryObject<Item> SKELETONS_BOOK = register(SpawnSkeletonArmyBook.class);
	public static final RegistryObject<Item> CREEPERS_BOOK = register(SpawnCreeperArmyBook.class);
	public static final RegistryObject<Item> ZOMBIE_BOOK = register(SpawnGiantZombieBook.class);
	public static final RegistryObject<Item> BEASTMASTER_MIST_BOOK = register(BeastmasterMistBook.class);
	public static final RegistryObject<Item> BEASTMASTER_BOOK = register(BeastmasterBook.class);
	public static final RegistryObject<Item> GUARDIAN_BOOK = register(SpawnGuardianBook.class);
	public static final RegistryObject<Item> DRAGON_BOOK = register(SpawnDragonBook.class);
	public static final RegistryObject<Item> ARROWS_BOOK = register(MultipleArrowsBook.class);
	public static final RegistryObject<Item> COBWEB_BOOK = register(CobwebBook.class);
	public static final RegistryObject<Item> ICE_BLOCK_BOOK = register(IceBlockBook.class);
	public static final RegistryObject<Item> LAVA_BLOCK_BOOK = register(LavaBlockBook.class);
	public static final RegistryObject<Item> HOLE_BOOK = register(DigMobHoleBook.class);
	public static final RegistryObject<Item> LIGHTNING_BOOK = register(LightningBoltBook.class);
	public static final RegistryObject<Item> LIGHTNING_MIST_BOOK = register(LightningBoltMistBook.class);
	public static final RegistryObject<Item> ANVIL_BOOK = register(FallingAnvilBook.class);
	public static final RegistryObject<Item> HORZ_FORCE_BOOK = register(EmitHorizontalForceBook.class);
	public static final RegistryObject<Item> VERT_FORCE_BOOK = register(EmitVerticalForceBook.class);
	public static final RegistryObject<Item> VERT_FORCE_MIST_BOOK = register(EmitVerticalForceMistBook.class);
	public static final RegistryObject<Item> STAIRS_BOOK = register(BuildStairsBook.class);
	public static final RegistryObject<Item> VACUUM_MIST_BOOK = register(VacuumMistBook.class);
	public static final RegistryObject<Item> COPY_PASTE_BOOK = register(CopyPasteBlocksBook.class);
	public static final RegistryObject<Item> DUPLICATE_BOOK = register(DuplicateBlockBook.class);
	public static final RegistryObject<Item> ROAD_BOOK = register(BuildRoadBook.class);
	public static final RegistryObject<Item> RAINBOW_ROAD_BOOK = register(BuildRainbowRoadBook.class);
	public static final RegistryObject<Item> MINE_BOOK = register(BuildMineBook.class);
	public static final RegistryObject<Item> ABYSS_BOOK = register(BuildAbyssBook.class);
	public static final RegistryObject<Item> SMALL_HOLE_BOOK = register(BuildSmallHoleBook.class);
	public static final RegistryObject<Item> NATURALIZE_BOOK = register(NaturalizeBook.class);
	public static final RegistryObject<Item> RAINBOWNIZE_BOOK = register(RainbownizeBook.class);
	public static final RegistryObject<Item> TOWER_BOOK = register(BuildTowerBook.class);
	public static final RegistryObject<Item> DECOY_BOOK = register(DecoyBook.class);
	public static final RegistryObject<Item> AGGRO_BOOK = register(ReceiveAggroBook.class);

	/**
	 * Idols
	 */
	public static final RegistryObject<Item> RAIN_IDOL = register(RainIdolInventoryItem.class);
	public static final RegistryObject<Item> CHICKENIZE_IDOL = register(ChickenizeIdolInventoryItem.class);
	public static final RegistryObject<Item> ANGEL_IDOL = register(AngelIdolInventoryItem.class);
	public static final RegistryObject<Item> LEVITATION_IDOL = register(LevitationIdolInventoryItem.class);
	public static final RegistryObject<Item> LIGHTNING_IDOL = register(LightningBoltIdolInventoryItem.class);
	public static final RegistryObject<Item> FLOWER_IDOL = register(FlowerIdolInventoryItem.class);
	public static final RegistryObject<Item> RAINBOWNIZE_IDOL = register(RainbownizeIdolInventoryItem.class);
	public static final RegistryObject<Item> FLAMEBLAST_IDOL = register(FlameBlastIdolInventoryItem.class);
	public static final RegistryObject<Item> CHARM_IDOL = register(CharmBeastIdolInventoryItem.class);
	public static final RegistryObject<Item> BLINDNESS_IDOL = register(BlindnessIdolInventoryItem.class);
	public static final RegistryObject<Item> PINKYNIZE_IDOL = register(PinkynizeIdolInventoryItem.class);
	public static final RegistryObject<Item> PRIME_IDOL = register(PrimeMobIdolInventoryItem.class);
	public static final RegistryObject<Item> LLAMA_IDOL = register(LlamaSpitIdolInventoryItem.class);
	public static final RegistryObject<Item> EGG_IDOL = register(EggProjectileIdolInventoryItem.class);
	public static final RegistryObject<Item> METEOR_IDOL = register(MeteorIdolInventoryItem.class);
	public static final RegistryObject<Item> SATURATION_IDOL = register(SaturationIdolInventoryItem.class);
	public static final RegistryObject<Item> MOBAGGRO_IDOL = register(MobsAggroIdolInventoryItem.class);
	public static final RegistryObject<Item> REAPER_IDOL = register(ReaperIdolInventoryItem.class);
	public static final RegistryObject<Item> MASSEXTINCT_IDOL = register(MassExtinctionEventIdolInventoryItem.class);
	public static final RegistryObject<Item> MOBSLEVITATION_IDOL = register(MobsLevitationIdolInventoryItem.class);
	public static final RegistryObject<Item> PLAYERAGGRO_IDOL = register(PlayerAggroIdolInventoryItem.class);
	public static final RegistryObject<Item> PARROTS_IDOL = register(AngryParrotsIdolInventoryItem.class);
	public static final RegistryObject<Item> REFLECT_IDOL = register(ReflectIdolInventoryItem.class);
	public static final RegistryObject<Item> BEES_IDOL = register(KillerBeesIdolInventoryItem.class);
	public static final RegistryObject<Item> PIGS_IDOL = register(WarPigsIdolInventoryItem.class);
	public static final RegistryObject<Item> DECSIZE_IDOL = register(DecreaseSizeIdolInventoryItem.class);
	public static final RegistryObject<Item> INCSIZE_IDOL = register(IncreaseSizeIdolInventoryItem.class);
	public static final RegistryObject<Item> BLOCK_IDOL = register(RemoveBlockSpiralIdolInventoryItem.class);
	public static final RegistryObject<Item> RESPAWN_IDOL = register(RespawnIdolInventoryItem.class);

	/**
	 * Register item using the class literal. The registry key is derived from the
	 * class simple name and the item is instantiated lazily via reflection inside
	 * the {@link net.minecraftforge.registries.DeferredRegister} supplier, ensuring
	 * no eager construction occurs during class initialisation.
	 *
	 * @param <T>       item type
	 * @param itemClass class of the item to register; must expose a no-arg constructor
	 * @return registry object
	 */
	static <T extends Item> RegistryObject<Item> register(Class<T> itemClass) {
		String key = itemClass.getSimpleName().toLowerCase();
		return ITEMS_REGISTRY.register(key, () -> {
			try {
				return itemClass.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to instantiate item: " + itemClass.getName(), e);
			}
		});
	}

}
