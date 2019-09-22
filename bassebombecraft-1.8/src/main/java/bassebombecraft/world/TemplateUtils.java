package bassebombecraft.world;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.VERSION;

import org.apache.commons.lang3.RandomStringUtils;

import bassebombecraft.BassebombeCraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

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
	 * @param world  world object.
	 * @param offset world structure offset.
	 * @param size   structure size.
	 * 
	 * @return resource location where structure was saved. is null if at client
	 *         side.
	 */
	public static ResourceLocation save(World world, BlockPos offset, BlockPos size) {

		// exit if at client side
		if (WorldUtils.isWorldAtClientSide(world))
			return null;

		String templateName = new StringBuilder().append(MODID).append("-").append(VERSION).append("-")
				.append(RandomStringUtils.randomAlphabetic(10)).toString();

		// get template manager
		ServerWorld worldserver = (ServerWorld) world;
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();

		// create template
		ResourceLocation location = new ResourceLocation(templateName);
		Template template = templatemanager.getTemplate(location);
		template.takeBlocksFromWorld(world, offset, size, DONT_TAKE_ENTITIES, Blocks.STRUCTURE_VOID);
		String author = BassebombeCraft.getBassebombeCraft().getUser();
		template.setAuthor(author);

		// save template
		templatemanager.writeToFile(location);

		return location;
	}

	/**
	 * Load structure from template.
	 * 
	 * @param world  world object.
	 * @param name   structure name to load.
	 * @param offset world offset where structure should be placed.
	 */
	public static void load(World world, String name, BlockPos offset) {

		// exit if at client side
		if (WorldUtils.isWorldAtClientSide(world))
			return;

		// get template manager
		ServerWorld worldserver = (ServerWorld) world;
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();

		// get template
		Template template = templatemanager.getTemplate(new ResourceLocation(name));

		// exit if template is undefined
		if (template == null) {

			// TODO: report exception

			return;
		}

		// get block state
		BlockState iblockstate = world.getBlockState(offset);
		world.notifyBlockUpdate(offset, iblockstate, iblockstate, 3);

		// define bounding box
		BlockPos boundingBoxSize = offset.add(template.getSize());

		MutableBoundingBox boundingBox = new MutableBoundingBox(offset, boundingBoxSize);

		// create placement settings
		PlacementSettings placementsettings = new PlacementSettings().setBoundingBox(boundingBox);

		// load template blocks
		template.addBlocksToWorld(world, offset, placementsettings);
	}
}
