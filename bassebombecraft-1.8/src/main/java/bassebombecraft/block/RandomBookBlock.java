package bassebombecraft.block;

import static bassebombecraft.ModConstants.MODID;

import java.util.Random;

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
import bassebombecraft.item.book.SpawnFlamingChickenBook;
import bassebombecraft.item.book.SpawnGuardianBook;
import bassebombecraft.item.book.SpawnManyCowsBook;
import bassebombecraft.item.book.SpawnSquidBook;
import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.item.book.ToxicMistBook;
import bassebombecraft.item.book.VacuumMistBook;
import bassebombecraft.item.book.WitherMistBook;
import bassebombecraft.item.book.WitherSkullBook;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Random book block which drops a random book when destroyed.
 */
public class RandomBookBlock extends Block {

	public final static String BLOCK_NAME = RandomBookBlock.class.getSimpleName();

	public RandomBookBlock(Material materialIn) {
		super(materialIn);
		setUnlocalizedName(BLOCK_NAME);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		int number = rand.nextInt(50);

		switch (number) {
		case 0:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, TeleportBook.ITEM_NAME));
		case 1:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, SmallFireballBook.ITEM_NAME));
		case 2:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, LargeFireballBook.ITEM_NAME));
		case 3:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, LingeringFlameBook.ITEM_NAME));
		case 5:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, LingeringFuryBook.ITEM_NAME));
		case 6:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, LavaSpiralMistBook.ITEM_NAME));
		case 7:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, ToxicMistBook.ITEM_NAME));
		case 8:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, WitherSkullBook.ITEM_NAME));
		case 9:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, WitherMistBook.ITEM_NAME));
		case 10:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MovingWitherMistBook.ITEM_NAME));
		case 11:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MovingLavaMistBook.ITEM_NAME));
		case 12:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MovingLavaMultiMistBook.ITEM_NAME));
		case 13:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MovingIceMultiMistBook.ITEM_NAME));
		case 14:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MovingWaterMultiMistBook.ITEM_NAME));
		case 15:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MovingRainbowMistBook.ITEM_NAME));
		case 16:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, HealingMistBook.ITEM_NAME));
		case 17:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, SpawnFlamingChickenBook.ITEM_NAME));
		case 18:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, SpawnSquidBook.ITEM_NAME));
		case 19:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BaconBazookaBook.ITEM_NAME));
		case 20:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, CreeperCannonBook.ITEM_NAME));
		case 21:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, PrimedCreeperCannonBook.ITEM_NAME));
		case 22:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, CreeperApocalypseBook.ITEM_NAME));
		case 23:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, Spawn100ChickensBook.ITEM_NAME));
		case 24:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, SpawnManyCowsBook.ITEM_NAME));
		case 25:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BeastmasterMistBook.ITEM_NAME));
		case 26:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BeastmasterBook.ITEM_NAME));
		case 27:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, SpawnGuardianBook.ITEM_NAME));
		case 28:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MultipleArrowsBook.ITEM_NAME));
		case 29:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, CobwebBook.ITEM_NAME));
		case 30:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, IceBlockBook.ITEM_NAME));
		case 31:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, LavaBlockBook.ITEM_NAME));
		case 32:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, FallingAnvilBook.ITEM_NAME));
		case 33:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, EmitHorizontalForceBook.ITEM_NAME));
		case 34:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, EmitVerticalForceBook.ITEM_NAME));
		case 35:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, EmitVerticalForceMistBook.ITEM_NAME));
		case 36:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, VacuumMistBook.ITEM_NAME));
		case 37:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, CopyPasteBlocksBook.ITEM_NAME));
		case 38:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, DuplicateBlockBook.ITEM_NAME));
		case 39:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BuildRoadBook.ITEM_NAME));
		case 40:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BuildRainbowRoadBook.ITEM_NAME));
		case 41:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BuildMineBook.ITEM_NAME));
		case 42:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BuildAbyssBook.ITEM_NAME));
		case 43:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, BuildSmallHoleBook.ITEM_NAME));
		case 44:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, NaturalizeBook.ITEM_NAME));
		case 45:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, RainbownizeBook.ITEM_NAME));
		case 46:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, LightningBoltBook.ITEM_NAME));
		case 47:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, LightningBoltMistBook.ITEM_NAME));
		case 48:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, Spawn100RainingLlamasBook.ITEM_NAME));
		case 49:
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, MovingTntMistBook.ITEM_NAME));

		default:
			return super.getItemDropped(state, rand, fortune);
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return 1;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 1;
	}

}
