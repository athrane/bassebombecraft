package bassebombecraft.world;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.VERSION;

import org.apache.commons.lang3.RandomStringUtils;

import bassebombecraft.BassebombeCraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.structure.StructureBoundingBox;

/**
 * Helper class for using templates
 */
public class TemplateUtils {

	/**
	 * Don't include entities in templates.
	 */
	static final boolean DONT_TAKE_ENTITIES = false;

	/**
	 * Save structure as template.
	 * 
	 * @param world
	 *            world object.
	 * @param offset
	 *            world structure offset.
	 * @param size
	 *            structure size.
	 *            
	 * @return resource location where structure was saved. is null if at client side.
	 */
	public static ResourceLocation save(World world, BlockPos offset, BlockPos size) {

		// exit if at client side
		if (WorldUtils.isWorldAtClientSide(world))
			return null;

		String templateName = new StringBuilder().append(MODID).append("-").append(VERSION).append("-")
				.append(RandomStringUtils.randomAlphabetic(10)).toString();

		// get server
		MinecraftServer minecraftserver = world.getMinecraftServer();

		// get template manager
		WorldServer worldserver = (WorldServer) world;
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();

		// create template
		ResourceLocation location = new ResourceLocation(templateName);
		Template template = templatemanager.getTemplate(minecraftserver, location);
		template.takeBlocksFromWorld(world, offset, size, DONT_TAKE_ENTITIES, Blocks.STRUCTURE_VOID);
		String author = BassebombeCraft.getBassebombeCraft().getUser();
		template.setAuthor(author);
				
		// save template
		templatemanager.writeTemplate(minecraftserver, location);

		return location;
	}

	/**
	 * Load structure from template.
	 * 
	 * @param world
	 *            world object.
	 * @param name
	 *            structure name to load.
	 * @param offset
	 *            world offset where structure should be placed.
	 */
	public static void load(World world, String name, BlockPos offset) {

		// exit if at client side
		if (WorldUtils.isWorldAtClientSide(world))
			return;

		// get server
		MinecraftServer minecraftserver = world.getMinecraftServer();

		// get template manager
		WorldServer worldserver = (WorldServer) world;
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();

		// get template
		Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(name));

		// exit if template is undefined
		if (template == null) {

			// TODO: report exception

			return;
		}

		// get block state
		IBlockState iblockstate = world.getBlockState(offset);
		world.notifyBlockUpdate(offset, iblockstate, iblockstate, 3);

		// define bounding box
		BlockPos boundingBoxSize = offset.add(template.getSize());
		
		StructureBoundingBox boundingBox = new StructureBoundingBox(offset, boundingBoxSize);
		
		// create placement settings
		PlacementSettings placementsettings = new PlacementSettings().setBoundingBox(boundingBox);

		// load template blocks
		template.addBlocksToWorld(world, offset, placementsettings);
	}
}
